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
class CATEGORIES private() extends Table.SqlTable("PUBLIC", "categories", Some("c")) {
  val ID: SqlField[Category.Id, CATEGORIES] = new SqlField[Category.Id, CATEGORIES](this, "id") // INT NOT NULL
  val NAME: SqlField[String, CATEGORIES] = new SqlField[String, CATEGORIES](this, "name") // VARCHAR(50) NOT NULL

  override def getFields: List[SqlField[_, CATEGORIES]] = List(ID, NAME)

  override def getSorts: List[Sort] = List(Sort("name", "name", NonEmptyList.of(NAME.desc, ID.asc)))
}

private[database] object CATEGORIES {
  val table = new CATEGORIES() // unique table instance, should be accessed through `gospeak.libs.sql.testingutils.database.Tables` object
}
