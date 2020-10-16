package gospeak.libs.sql.testingutils.database.tables

import cats.data.NonEmptyList
import gospeak.libs.sql.dsl.Table._
import gospeak.libs.sql.dsl._
import gospeak.libs.sql.testingutils.Entities._

/**
 * Hello
 *
 * Class generated by gospeak.libs.sql.generator.writer.ScalaWriter
 */
class CATEGORIES private(getAlias: Option[String] = Some("c")) extends Table.SqlTable("PUBLIC", "categories", getAlias) {
  type Self = CATEGORIES

  val ID: SqlField[Category.Id, CATEGORIES] = SqlField(this, "id", "INT NOT NULL", JdbcType.Integer, nullable = false, 1)
  val NAME: SqlField[String, CATEGORIES] = SqlField(this, "name", "VARCHAR(50) NOT NULL", JdbcType.VarChar, nullable = false, 2)

  override def getFields: List[SqlField[_, CATEGORIES]] = List(ID, NAME)

  override def getSorts: List[Sort] = List(Sort("name", "name", NonEmptyList.of(NAME.desc, ID.asc)))

  override def searchOn: List[SqlField[_, CATEGORIES]] = List(NAME)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): CATEGORIES = new CATEGORIES(Some(alias))
}

private[database] object CATEGORIES {
  val table = new CATEGORIES() // table instance, should be accessed through `gospeak.libs.sql.testingutils.database.Tables` object
}
