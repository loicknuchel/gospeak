package gospeak.libs.sql.testingutils.database.tables

import java.time.{Instant, LocalDate}

import gospeak.libs.sql.dsl.Table._
import gospeak.libs.sql.dsl._
import gospeak.libs.sql.testingutils.Entities._


/**
 * Hello
 *
 * Class generated by gospeak.libs.sql.generator.writer.ScalaWriter
 */
class KINDS private() extends Table.SqlTable("PUBLIC", "kinds", None) {
  val CHAR: SqlFieldOpt[String, KINDS] = new SqlFieldOpt[String, KINDS](this, "char") // CHAR(4)
  val VARCHAR: SqlFieldOpt[String, KINDS] = new SqlFieldOpt[String, KINDS](this, "varchar") // VARCHAR(50)
  val TIMESTAMP: SqlFieldOpt[Instant, KINDS] = new SqlFieldOpt[Instant, KINDS](this, "timestamp") // TIMESTAMP
  val DATE: SqlFieldOpt[LocalDate, KINDS] = new SqlFieldOpt[LocalDate, KINDS](this, "date") // DATE
  val BOOLEAN: SqlFieldOpt[Boolean, KINDS] = new SqlFieldOpt[Boolean, KINDS](this, "boolean") // BOOLEAN
  val INT: SqlFieldOpt[Int, KINDS] = new SqlFieldOpt[Int, KINDS](this, "int") // INT
  val BIGINT: SqlFieldOpt[Long, KINDS] = new SqlFieldOpt[Long, KINDS](this, "bigint") // BIGINT
  val DOUBLE: SqlFieldOpt[Double, KINDS] = new SqlFieldOpt[Double, KINDS](this, "double") // DOUBLE PRECISION
  val A_LONG_NAME: SqlFieldOpt[Int, KINDS] = new SqlFieldOpt[Int, KINDS](this, "a_long_name") // INT

  override def getFields: List[SqlField[_, KINDS]] = List(CHAR, VARCHAR, TIMESTAMP, DATE, BOOLEAN, INT, BIGINT, DOUBLE, A_LONG_NAME)

  override def getSorts: List[Sort] = List()
}

private[database] object KINDS {
  val table = new KINDS() // unique table instance, should be accessed through `gospeak.libs.sql.testingutils.database.Tables` object
}
