package gospeak.libs.sql.dsl

import cats.data.NonEmptyList
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.fragment.Fragment.const0
import gospeak.libs.sql.dsl.Exceptions.{ConflictingTableFields, UnknownTableFields}
import gospeak.libs.sql.dsl.Query.Inner.{GroupByClause, OrderByClause, WhereClause}
import gospeak.libs.sql.dsl.Query.{Delete, Insert, Select, Update}
import gospeak.libs.sql.dsl.Table.Inner._
import gospeak.libs.sql.dsl.Table.{Join, JoinTable, Sort}

import scala.language.dynamics

sealed trait Table {
  def getFields: List[Field[_]]

  def getSorts: List[Sort]

  def field[A](name: String): Field[A]

  def has(f: Field[_]): Boolean

  def fr: Fragment

  // TODO do not lose previous type (chain predefined joins, still use typed fields...)
  def join[T <: Table](table: T): JoinClause[this.type, T] = JoinClause(this, Join.Kind.Inner, table)

  def join[T <: Table](table: T, kind: Join.Kind.type => Join.Kind): JoinClause[this.type, T] = JoinClause(this, kind(Join.Kind), table)

  def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](ref: SqlFieldRef[A, T, T2]): JoinTable = join(ref.references.table).on(ref.is(ref.references))

  def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](ref: SqlFieldRef[A, T, T2], kind: Join.Kind.type => Join.Kind): JoinTable = join(ref.references.table, kind).on(ref.is(ref.references))

  def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](ref: SqlFieldRefOpt[A, T, T2]): JoinTable = join(ref.references.table).on(ref.isOpt(ref.references))

  def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](ref: SqlFieldRefOpt[A, T, T2], kind: Join.Kind.type => Join.Kind): JoinTable = join(ref.references.table, kind).on(ref.isOpt(ref.references))

  def select: Select.Builder[this.type] = {
    val hasAggregation = getFields.exists {
      case _: AggField[_] => true
      case _ => false
    }
    Select.Builder(
      table = this,
      fields = getFields,
      where = WhereClause(None),
      groupBy = GroupByClause(if (hasAggregation) getFields.collect { case f: SqlField[_, _] => f } else List()),
      orderBy = OrderByClause(getSorts.headOption.map(_.fields.toList).getOrElse(List())))
  }
}

object Table {

  abstract class SqlTable(val getSchema: String,
                          val getName: String,
                          val getAlias: Option[String]) extends Table with Dynamic {
    override def getFields: List[SqlField[_, SqlTable]] // to specialize return type as SqlField

    override def field[A](name: String): SqlField[A, this.type] =
      getFields.filter(_.name == name) match {
        case List() => throw UnknownTableFields(this, NonEmptyList.of(TableField(name)))
        case List(field) => field.asInstanceOf[SqlField[A, this.type]]
        case fields => throw ConflictingTableFields(this, name, NonEmptyList.fromListUnsafe(fields))
      }

    override def has(f: Field[_]): Boolean = f match {
      case f: SqlField[_, Table.SqlTable] => f.table.getSchema == getSchema && f.table.getName == getName
      case _ => false
    }

    override def fr: Fragment = const0(getName + getAlias.map(" " + _).getOrElse(""))

    def selectDynamic[A](name: String): SqlField[A, this.type] = field[A](name)

    def dropFields(p: SqlField[_, SqlTable] => Boolean): SqlTable = {
      val that = this
      new SqlTable(getSchema, getName, getAlias) {
        override def getFields: List[SqlField[_, SqlTable]] = that.getFields.filterNot(p)

        override def getSorts: List[Sort] = that.getSorts
      }
    }

    def dropFields(fields: List[SqlField[_, SqlTable]]): SqlTable = dropFields(fields.contains(_))

    def dropFields(fields: SqlField[_, SqlTable]*): SqlTable = dropFields(fields.contains(_))

    def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](f: this.type => SqlFieldRef[A, T, T2]): JoinTable = joinOn(f(this))

    def joinOn[A, T <: Table.SqlTable, T2 <: Table.SqlTable](f: this.type => SqlFieldRef[A, T, T2], kind: Join.Kind.type => Join.Kind): JoinTable = joinOn(f(this), kind)

    def joinOnOpt[A, T <: Table.SqlTable, T2 <: Table.SqlTable](f: this.type => SqlFieldRefOpt[A, T, T2]): JoinTable = joinOn(f(this))

    def joinOnOpt[A, T <: Table.SqlTable, T2 <: Table.SqlTable](f: this.type => SqlFieldRefOpt[A, T, T2], kind: Join.Kind.type => Join.Kind): JoinTable = joinOn(f(this), kind)

    def insert: Insert.Builder[this.type] = Insert.Builder(this)

    def update: Update.Builder[this.type] = Update.Builder(this, List())

    def delete: Delete.Builder[this.type] = Delete.Builder(this)

    override def toString: String = s"SqlTable($getSchema, $getName, $getAlias, $getFields)"
  }

  case class JoinTable(table: Table.SqlTable,
                       joins: List[Join[Table.SqlTable]],
                       getFields: List[Field[_]],
                       getSorts: List[Sort]) extends Table with Dynamic {
    override def field[A](name: String): SqlField[A, SqlTable] =
      (table :: joins.map(_.table)).flatMap(_.getFields).filter(_.name == name) match {
        case List() => throw UnknownTableFields(this, NonEmptyList.of(TableField(name)))
        case List(field) => field.asInstanceOf[SqlField[A, SqlTable]]
        case fields => throw ConflictingTableFields(this, name, NonEmptyList.fromListUnsafe(fields))
      }

    override def has(f: Field[_]): Boolean = table.has(f) || joins.exists(_.table.has(f))

    override def fr: Fragment = const0(table.getName + table.getAlias.map(" " + _).getOrElse("")) ++ joins.foldLeft(fr0"")(_ ++ fr0" " ++ _.fr)

    def selectDynamic[A](name: String): SqlField[A, SqlTable] = field[A](name)

    def fields(fields: List[Field[_]]): JoinTable = Exceptions.check(fields, this, copy(getFields = fields))

    def fields(fields: Field[_]*): JoinTable = this.fields(fields.toList)

    def addFields(fields: Field[_]*): JoinTable = this.fields(this.getFields ++ fields.toList)

    def dropFields(p: Field[_] => Boolean): JoinTable = copy(getFields = getFields.filterNot(p))

    def dropFields(fields: List[Field[_]]): JoinTable = dropFields(fields.contains(_))

    def dropFields(fields: Field[_]*): JoinTable = dropFields(fields.contains(_))
  }

  case class UnionTable(select1: Select[_],
                        select2: Select[_],
                        alias: Option[String],
                        getFields: List[TableField[_]],
                        getSorts: List[Sort]) extends Table with Dynamic {
    override def field[A](name: String): TableField[A] =
      getFields.filter(_.name == name) match {
        case List() => throw UnknownTableFields(this, NonEmptyList.of(TableField(name)))
        case List(field) => field.asInstanceOf[TableField[A]]
        case fields => throw ConflictingTableFields(this, name, NonEmptyList.fromListUnsafe(fields))
      }

    override def has(f: Field[_]): Boolean = f match {
      case f: TableField[_] => getFields.exists(_.name == f.name)
      case _ => false
    }

    override def fr: Fragment = fr0"((" ++ select1.fr ++ fr0") UNION (" ++ select2.fr ++ fr0"))" ++ const0(alias.map(" " + _).getOrElse(""))

    def selectDynamic[A](name: String): TableField[A] = field[A](name)
  }

  case class Join[+T <: Table.SqlTable](kind: Join.Kind, table: T, on: Cond) {
    def fr: Fragment = const0(s"${kind.value} ") ++ table.fr ++ const0(" ON ") ++ on.fr
  }

  object Join {

    sealed abstract class Kind(val value: String)

    object Kind {

      case object Inner extends Kind("INNER JOIN")

      case object LeftOuter extends Kind("LEFT OUTER JOIN")

    }

  }

  case class Sort(slug: String, label: String, fields: NonEmptyList[Field.Order[_]])

  object Sort {
    def apply(slug: String, field: Field.Order[_], other: Field.Order[_]*): Sort = new Sort(slug, slug, NonEmptyList.of(field, other: _*))

    def apply(slug: String, label: String, field: Field.Order[_], other: Field.Order[_]*): Sort = new Sort(slug, label, NonEmptyList.of(field, other: _*))
  }

  object Inner {

    case class JoinClause[T <: Table, U <: Table](left: T, kind: Join.Kind, right: U) {
      def on(cond: Cond): JoinTable =
        Exceptions.check(cond, (left, right) match {
          case (l: SqlTable, r: SqlTable) => JoinTable(
            table = l,
            joins = List(Join(kind, r, cond)),
            getFields = l.getFields ++ r.getFields,
            getSorts = l.getSorts)
          case (l: SqlTable, r: JoinTable) => JoinTable(
            table = l,
            joins = Join(kind, r.table, cond) :: r.joins,
            getFields = l.getFields ++ r.getFields,
            getSorts = l.getSorts)
          case (l: SqlTable, r: UnionTable) => ???
          case (l: JoinTable, r: SqlTable) => JoinTable(
            table = l.table,
            joins = l.joins :+ Join(kind, r, cond),
            getFields = l.getFields ++ r.getFields,
            getSorts = l.getSorts)
          case (l: JoinTable, r: JoinTable) => JoinTable(
            table = l.table,
            joins = l.joins ++ (Join(kind, r.table, cond) :: r.joins),
            getFields = l.getFields ++ r.getFields,
            getSorts = l.getSorts)
          case (l: JoinTable, r: UnionTable) => ???
          case (l: UnionTable, r: SqlTable) => ???
          case (l: UnionTable, r: JoinTable) => ???
          case (l: UnionTable, r: UnionTable) => ???
        })

      def on(cond: U => Cond): JoinTable = on(cond(right))

      def on(cond: (T, U) => Cond): JoinTable = on(cond(left, right))
    }

  }

}
