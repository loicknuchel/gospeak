package gospeak.libs.sql.testingutils.database.tables

import gospeak.libs.sql.dsl._

/**
 * Class generated by gospeak.libs.sql.generator.writer.ScalaWriter
 */
class CATEGORIES private() extends Table.SqlTable("PUBLIC", "categories", Some("c")) {
  val ID: Field[gospeak.libs.sql.testingutils.Entities.Category.Id, CATEGORIES] = new Field[gospeak.libs.sql.testingutils.Entities.Category.Id, CATEGORIES](this, "id") // INT NOT NULL
  val NAME: Field[String, CATEGORIES] = new Field[String, CATEGORIES](this, "name") // VARCHAR(50) NOT NULL

  override def getFields: List[Field[_, CATEGORIES]] = List(ID, NAME)
}

private[database] object CATEGORIES {
  val table = new CATEGORIES() // unique table instance, should be accessed through `gospeak.libs.sql.testingutils.database.Tables` object
}
