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
class USER_REQUESTS private(getAlias: Option[String] = Some("ur")) extends Table.SqlTable("PUBLIC", "user_requests", getAlias) {
  type Self = USER_REQUESTS

  val ID: SqlFieldRaw[UserRequest.Id, USER_REQUESTS] = SqlField(this, "id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 1)
  val KIND: SqlFieldRaw[String, USER_REQUESTS] = SqlField(this, "kind", "VARCHAR(30) NOT NULL", JdbcType.VarChar, nullable = false, 2)
  val GROUP_ID: SqlFieldRef[Group.Id, USER_REQUESTS, GROUPS] = SqlField(this, "group_id", "CHAR(36)", JdbcType.Char, nullable = true, 3, GROUPS.table.ID)
  val CFP_ID: SqlFieldRef[Cfp.Id, USER_REQUESTS, CFPS] = SqlField(this, "cfp_id", "CHAR(36)", JdbcType.Char, nullable = true, 4, CFPS.table.ID)
  val EVENT_ID: SqlFieldRef[Event.Id, USER_REQUESTS, EVENTS] = SqlField(this, "event_id", "CHAR(36)", JdbcType.Char, nullable = true, 5, EVENTS.table.ID)
  val TALK_ID: SqlFieldRef[Talk.Id, USER_REQUESTS, TALKS] = SqlField(this, "talk_id", "CHAR(36)", JdbcType.Char, nullable = true, 6, TALKS.table.ID)
  val PROPOSAL_ID: SqlFieldRef[Proposal.Id, USER_REQUESTS, PROPOSALS] = SqlField(this, "proposal_id", "CHAR(36)", JdbcType.Char, nullable = true, 7, PROPOSALS.table.ID)
  val EXTERNAL_EVENT_ID: SqlFieldRef[ExternalEvent.Id, USER_REQUESTS, EXTERNAL_EVENTS] = SqlField(this, "external_event_id", "CHAR(36)", JdbcType.Char, nullable = true, 19, EXTERNAL_EVENTS.table.ID)
  val EXTERNAL_CFP_ID: SqlFieldRef[ExternalCfp.Id, USER_REQUESTS, EXTERNAL_CFPS] = SqlField(this, "external_cfp_id", "CHAR(36)", JdbcType.Char, nullable = true, 20, EXTERNAL_CFPS.table.ID)
  val EXTERNAL_PROPOSAL_ID: SqlFieldRef[ExternalProposal.Id, USER_REQUESTS, EXTERNAL_PROPOSALS] = SqlField(this, "external_proposal_id", "CHAR(36)", JdbcType.Char, nullable = true, 21, EXTERNAL_PROPOSALS.table.ID)
  val EMAIL: SqlFieldRaw[EmailAddress, USER_REQUESTS] = SqlField(this, "email", "VARCHAR(120)", JdbcType.VarChar, nullable = true, 8)
  val PAYLOAD: SqlFieldRaw[String, USER_REQUESTS] = SqlField(this, "payload", "VARCHAR(8192)", JdbcType.VarChar, nullable = true, 9)
  val DEADLINE: SqlFieldRaw[Instant, USER_REQUESTS] = SqlField(this, "deadline", "TIMESTAMP NOT NULL", JdbcType.Timestamp, nullable = false, 10)
  val CREATED_AT: SqlFieldRaw[Instant, USER_REQUESTS] = SqlField(this, "created_at", "TIMESTAMP NOT NULL", JdbcType.Timestamp, nullable = false, 11)
  val CREATED_BY: SqlFieldRef[User.Id, USER_REQUESTS, USERS] = SqlField(this, "created_by", "CHAR(36)", JdbcType.Char, nullable = true, 12, USERS.table.ID)
  val ACCEPTED_AT: SqlFieldRaw[Instant, USER_REQUESTS] = SqlField(this, "accepted_at", "TIMESTAMP", JdbcType.Timestamp, nullable = true, 13)
  val ACCEPTED_BY: SqlFieldRef[User.Id, USER_REQUESTS, USERS] = SqlField(this, "accepted_by", "CHAR(36)", JdbcType.Char, nullable = true, 14, USERS.table.ID)
  val REJECTED_AT: SqlFieldRaw[Instant, USER_REQUESTS] = SqlField(this, "rejected_at", "TIMESTAMP", JdbcType.Timestamp, nullable = true, 15)
  val REJECTED_BY: SqlFieldRef[User.Id, USER_REQUESTS, USERS] = SqlField(this, "rejected_by", "CHAR(36)", JdbcType.Char, nullable = true, 16, USERS.table.ID)
  val CANCELED_AT: SqlFieldRaw[Instant, USER_REQUESTS] = SqlField(this, "canceled_at", "TIMESTAMP", JdbcType.Timestamp, nullable = true, 17)
  val CANCELED_BY: SqlFieldRef[User.Id, USER_REQUESTS, USERS] = SqlField(this, "canceled_by", "CHAR(36)", JdbcType.Char, nullable = true, 18, USERS.table.ID)

  override def getFields: List[SqlField[_, USER_REQUESTS]] = List(ID, KIND, GROUP_ID, CFP_ID, EVENT_ID, TALK_ID, PROPOSAL_ID, EXTERNAL_EVENT_ID, EXTERNAL_CFP_ID, EXTERNAL_PROPOSAL_ID, EMAIL, PAYLOAD, DEADLINE, CREATED_AT, CREATED_BY, ACCEPTED_AT, ACCEPTED_BY, REJECTED_AT, REJECTED_BY, CANCELED_AT, CANCELED_BY)

  override def getSorts: List[Sort] = List(Sort("created", "created", NonEmptyList.of(CREATED_AT.desc)))

  override def searchOn: List[SqlField[_, USER_REQUESTS]] = List(ID, EMAIL, GROUP_ID, CREATED_BY)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): USER_REQUESTS = new USER_REQUESTS(Some(alias))
}

private[database] object USER_REQUESTS {
  val table = new USER_REQUESTS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}