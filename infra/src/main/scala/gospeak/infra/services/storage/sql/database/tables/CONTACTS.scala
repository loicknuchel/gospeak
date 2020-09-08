package gospeak.infra.services.storage.sql.database.tables

import java.time.{Instant, LocalDateTime}

import cats.data.NonEmptyList
import gospeak.core.domain._
import gospeak.core.domain.messages.Message
import gospeak.core.domain.utils.SocialAccounts.SocialAccount._
import gospeak.core.services.meetup.domain.{MeetupEvent, MeetupGroup, MeetupUser, MeetupVenue}
import gospeak.core.services.slack.domain.SlackToken
import gospeak.libs.scala.domain._
import gospeak.libs.sql.dsl.Table._
import gospeak.libs.sql.dsl._

import scala.concurrent.duration.FiniteDuration

/**
 * Generated file, do not update it!
 *
 * Regenerate it using Gospeak CLI (`gospeak.web.GsCLI` class) to keep it in sync with the database state.
 *
 * --
 *
 * Class generated by gospeak.libs.sql.generator.writer.ScalaWriter
 */
class CONTACTS private(getAlias: Option[String] = Some("ct")) extends Table.SqlTable("PUBLIC", "contacts", getAlias) {
  type Self = CONTACTS

  val ID: SqlField[Contact.Id, CONTACTS] = new SqlField[Contact.Id, CONTACTS](this, "id") // CHAR(36) NOT NULL
  val PARTNER_ID: SqlFieldRef[Partner.Id, CONTACTS, PARTNERS] = new SqlFieldRef[Partner.Id, CONTACTS, PARTNERS](this, "partner_id", PARTNERS.table.ID) // CHAR(36) NOT NULL
  val FIRST_NAME: SqlField[Contact.FirstName, CONTACTS] = new SqlField[Contact.FirstName, CONTACTS](this, "first_name") // VARCHAR(120) NOT NULL
  val LAST_NAME: SqlField[Contact.LastName, CONTACTS] = new SqlField[Contact.LastName, CONTACTS](this, "last_name") // VARCHAR(120) NOT NULL
  val EMAIL: SqlField[EmailAddress, CONTACTS] = new SqlField[EmailAddress, CONTACTS](this, "email") // VARCHAR(120) NOT NULL
  val NOTES: SqlField[Markdown, CONTACTS] = new SqlField[Markdown, CONTACTS](this, "notes") // VARCHAR(4096) NOT NULL
  val CREATED_AT: SqlField[Instant, CONTACTS] = new SqlField[Instant, CONTACTS](this, "created_at") // TIMESTAMP NOT NULL
  val CREATED_BY: SqlFieldRef[User.Id, CONTACTS, USERS] = new SqlFieldRef[User.Id, CONTACTS, USERS](this, "created_by", USERS.table.ID) // CHAR(36) NOT NULL
  val UPDATED_AT: SqlField[Instant, CONTACTS] = new SqlField[Instant, CONTACTS](this, "updated_at") // TIMESTAMP NOT NULL
  val UPDATED_BY: SqlFieldRef[User.Id, CONTACTS, USERS] = new SqlFieldRef[User.Id, CONTACTS, USERS](this, "updated_by", USERS.table.ID) // CHAR(36) NOT NULL

  override def getFields: List[SqlField[_, CONTACTS]] = List(ID, PARTNER_ID, FIRST_NAME, LAST_NAME, EMAIL, NOTES, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY)

  override def getSorts: List[Sort] = List(Sort("name", "name", NonEmptyList.of(LAST_NAME.asc, FIRST_NAME.asc)))

  override def searchOn: List[SqlField[_, CONTACTS]] = List(ID, FIRST_NAME, LAST_NAME, EMAIL)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): CONTACTS = new CONTACTS(Some(alias))
}

private[database] object CONTACTS {
  val table = new CONTACTS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}