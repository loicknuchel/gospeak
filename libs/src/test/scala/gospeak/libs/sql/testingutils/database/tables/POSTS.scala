package gospeak.libs.sql.testingutils.database.tables

import java.time.Instant

import gospeak.libs.sql.dsl.Table._
import gospeak.libs.sql.dsl._
import gospeak.libs.sql.testingutils.Entities._

/**
 * Hello
 *
 * Class generated by gospeak.libs.sql.generator.writer.ScalaWriter
 */
class POSTS private(getAlias: Option[String] = Some("p")) extends Table.SqlTable("PUBLIC", "posts", getAlias) {
  type Self = POSTS

  val ID: SqlField[Post.Id, POSTS] = new SqlField[Post.Id, POSTS](this, "id") // INT NOT NULL
  val TITLE: SqlField[String, POSTS] = new SqlField[String, POSTS](this, "title") // VARCHAR(50) NOT NULL
  val TEXT: SqlField[String, POSTS] = new SqlField[String, POSTS](this, "text") // VARCHAR(4096) NOT NULL
  val DATE: SqlField[Instant, POSTS] = new SqlField[Instant, POSTS](this, "date") // TIMESTAMP NOT NULL
  val AUTHOR: SqlFieldRef[User.Id, POSTS, USERS] = new SqlFieldRef[User.Id, POSTS, USERS](this, "author", USERS.table.ID) // INT NOT NULL
  val CATEGORY: SqlFieldRefOpt[Category.Id, POSTS, CATEGORIES] = new SqlFieldRefOpt[Category.Id, POSTS, CATEGORIES](this, "category", CATEGORIES.table.ID) // INT

  override def getFields: List[SqlField[_, POSTS]] = List(ID, TITLE, TEXT, DATE, AUTHOR, CATEGORY)

  override def getSorts: List[Sort] = List()

  override def searchOn: List[SqlField[_, POSTS]] = List(ID, TITLE, TEXT, DATE, AUTHOR, CATEGORY)

  def alias(alias: String): POSTS = new POSTS(Some(alias))

  def AUTHORJoin: Table.JoinTable = join(USERS.table).on(_.AUTHOR is _.ID)

  def CATEGORYJoin: Table.JoinTable = join(CATEGORIES.table).on(_.CATEGORY isOpt _.ID)
}

private[database] object POSTS {
  val table = new POSTS() // table instance, should be accessed through `gospeak.libs.sql.testingutils.database.Tables` object
}
