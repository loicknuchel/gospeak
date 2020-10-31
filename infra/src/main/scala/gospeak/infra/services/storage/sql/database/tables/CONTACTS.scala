package gospeak.infra.services.storage.sql.database.tables

import java.time.{Instant, LocalDateTime}

import cats.data.NonEmptyList
import fr.loicknuchel.safeql.Table._
import fr.loicknuchel.safeql._
import gospeak.core.domain._
import gospeak.core.domain.messages.Message
import gospeak.core.domain.utils.SocialAccounts.SocialAccount._
import gospeak.core.services.meetup.domain.{MeetupEvent, MeetupGroup, MeetupUser, MeetupVenue}
import gospeak.core.services.slack.domain.SlackToken
import gospeak.libs.scala.domain._

import scala.concurrent.duration.FiniteDuration

/**
 * Generated file, do not update it!
 *
 * Regenerate it using Gospeak CLI (`gospeak.web.GsCLI` class) to keep it in sync with the database state.
 *
 * --
 *
 * Class generated by fr.loicknuchel.safeql.gen.writer.ScalaWriter
 */
class CONTACTS private(getAlias: Option[String] = Some("ct")) extends Table.SqlTable("PUBLIC", "contacts", getAlias) {
  type Self = CONTACTS

  val ID: SqlFieldRaw[Contact.Id, CONTACTS] = SqlField(this, "id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 1)
  val PARTNER_ID: SqlFieldRef[Partner.Id, CONTACTS, PARTNERS] = SqlField(this, "partner_id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 2, PARTNERS.table.ID)
  val FIRST_NAME: SqlFieldRaw[Contact.FirstName, CONTACTS] = SqlField(this, "first_name", "VARCHAR(120) NOT NULL", JdbcType.VarChar, nullable = false, 3)
  val LAST_NAME: SqlFieldRaw[Contact.LastName, CONTACTS] = SqlField(this, "last_name", "VARCHAR(120) NOT NULL", JdbcType.VarChar, nullable = false, 4)
  val EMAIL: SqlFieldRaw[EmailAddress, CONTACTS] = SqlField(this, "email", "VARCHAR(120) NOT NULL", JdbcType.VarChar, nullable = false, 5)
  val NOTES: SqlFieldRaw[Markdown, CONTACTS] = SqlField(this, "notes", "VARCHAR(4096) NOT NULL", JdbcType.VarChar, nullable = false, 6)
  val CREATED_AT: SqlFieldRaw[Instant, CONTACTS] = SqlField(this, "created_at", "TIMESTAMP NOT NULL", JdbcType.Timestamp, nullable = false, 7)
  val CREATED_BY: SqlFieldRef[User.Id, CONTACTS, USERS] = SqlField(this, "created_by", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 8, USERS.table.ID)
  val UPDATED_AT: SqlFieldRaw[Instant, CONTACTS] = SqlField(this, "updated_at", "TIMESTAMP NOT NULL", JdbcType.Timestamp, nullable = false, 9)
  val UPDATED_BY: SqlFieldRef[User.Id, CONTACTS, USERS] = SqlField(this, "updated_by", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 10, USERS.table.ID)

  override def getFields: List[SqlField[_, CONTACTS]] = List(ID, PARTNER_ID, FIRST_NAME, LAST_NAME, EMAIL, NOTES, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY)

  override def getSorts: List[Sort] = List(Sort("name", "name", NonEmptyList.of(LAST_NAME.asc, FIRST_NAME.asc)))

  override def searchOn: List[SqlField[_, CONTACTS]] = List(ID, FIRST_NAME, LAST_NAME, EMAIL)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): CONTACTS = new CONTACTS(Some(alias))
}

private[database] object CONTACTS {
  val table = new CONTACTS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}