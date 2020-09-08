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
class USER_REQUESTS private(getAlias: Option[String] = Some("ur")) extends Table.SqlTable("PUBLIC", "user_requests", getAlias) {
  type Self = USER_REQUESTS

  val ID: SqlField[UserRequest.Id, USER_REQUESTS] = new SqlField[UserRequest.Id, USER_REQUESTS](this, "id") // CHAR(36) NOT NULL
  val KIND: SqlField[String, USER_REQUESTS] = new SqlField[String, USER_REQUESTS](this, "kind") // VARCHAR(30) NOT NULL
  val GROUP_ID: SqlFieldRefOpt[Group.Id, USER_REQUESTS, GROUPS] = new SqlFieldRefOpt[Group.Id, USER_REQUESTS, GROUPS](this, "group_id", GROUPS.table.ID) // CHAR(36)
  val CFP_ID: SqlFieldRefOpt[Cfp.Id, USER_REQUESTS, CFPS] = new SqlFieldRefOpt[Cfp.Id, USER_REQUESTS, CFPS](this, "cfp_id", CFPS.table.ID) // CHAR(36)
  val EVENT_ID: SqlFieldRefOpt[Event.Id, USER_REQUESTS, EVENTS] = new SqlFieldRefOpt[Event.Id, USER_REQUESTS, EVENTS](this, "event_id", EVENTS.table.ID) // CHAR(36)
  val TALK_ID: SqlFieldRefOpt[Talk.Id, USER_REQUESTS, TALKS] = new SqlFieldRefOpt[Talk.Id, USER_REQUESTS, TALKS](this, "talk_id", TALKS.table.ID) // CHAR(36)
  val PROPOSAL_ID: SqlFieldRefOpt[Proposal.Id, USER_REQUESTS, PROPOSALS] = new SqlFieldRefOpt[Proposal.Id, USER_REQUESTS, PROPOSALS](this, "proposal_id", PROPOSALS.table.ID) // CHAR(36)
  val EXTERNAL_EVENT_ID: SqlFieldRefOpt[ExternalEvent.Id, USER_REQUESTS, EXTERNAL_EVENTS] = new SqlFieldRefOpt[ExternalEvent.Id, USER_REQUESTS, EXTERNAL_EVENTS](this, "external_event_id", EXTERNAL_EVENTS.table.ID) // CHAR(36)
  val EXTERNAL_CFP_ID: SqlFieldRefOpt[ExternalCfp.Id, USER_REQUESTS, EXTERNAL_CFPS] = new SqlFieldRefOpt[ExternalCfp.Id, USER_REQUESTS, EXTERNAL_CFPS](this, "external_cfp_id", EXTERNAL_CFPS.table.ID) // CHAR(36)
  val EXTERNAL_PROPOSAL_ID: SqlFieldRefOpt[ExternalProposal.Id, USER_REQUESTS, EXTERNAL_PROPOSALS] = new SqlFieldRefOpt[ExternalProposal.Id, USER_REQUESTS, EXTERNAL_PROPOSALS](this, "external_proposal_id", EXTERNAL_PROPOSALS.table.ID) // CHAR(36)
  val EMAIL: SqlFieldOpt[EmailAddress, USER_REQUESTS] = new SqlFieldOpt[EmailAddress, USER_REQUESTS](this, "email") // VARCHAR(120)
  val PAYLOAD: SqlFieldOpt[String, USER_REQUESTS] = new SqlFieldOpt[String, USER_REQUESTS](this, "payload") // VARCHAR(8192)
  val DEADLINE: SqlField[Instant, USER_REQUESTS] = new SqlField[Instant, USER_REQUESTS](this, "deadline") // TIMESTAMP NOT NULL
  val CREATED_AT: SqlField[Instant, USER_REQUESTS] = new SqlField[Instant, USER_REQUESTS](this, "created_at") // TIMESTAMP NOT NULL
  val CREATED_BY: SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS] = new SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS](this, "created_by", USERS.table.ID) // CHAR(36)
  val ACCEPTED_AT: SqlFieldOpt[Instant, USER_REQUESTS] = new SqlFieldOpt[Instant, USER_REQUESTS](this, "accepted_at") // TIMESTAMP
  val ACCEPTED_BY: SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS] = new SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS](this, "accepted_by", USERS.table.ID) // CHAR(36)
  val REJECTED_AT: SqlFieldOpt[Instant, USER_REQUESTS] = new SqlFieldOpt[Instant, USER_REQUESTS](this, "rejected_at") // TIMESTAMP
  val REJECTED_BY: SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS] = new SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS](this, "rejected_by", USERS.table.ID) // CHAR(36)
  val CANCELED_AT: SqlFieldOpt[Instant, USER_REQUESTS] = new SqlFieldOpt[Instant, USER_REQUESTS](this, "canceled_at") // TIMESTAMP
  val CANCELED_BY: SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS] = new SqlFieldRefOpt[User.Id, USER_REQUESTS, USERS](this, "canceled_by", USERS.table.ID) // CHAR(36)

  override def getFields: List[SqlField[_, USER_REQUESTS]] = List(ID, KIND, GROUP_ID, CFP_ID, EVENT_ID, TALK_ID, PROPOSAL_ID, EXTERNAL_EVENT_ID, EXTERNAL_CFP_ID, EXTERNAL_PROPOSAL_ID, EMAIL, PAYLOAD, DEADLINE, CREATED_AT, CREATED_BY, ACCEPTED_AT, ACCEPTED_BY, REJECTED_AT, REJECTED_BY, CANCELED_AT, CANCELED_BY)

  override def getSorts: List[Sort] = List(Sort("created", "created", NonEmptyList.of(CREATED_AT.desc)))

  override def searchOn: List[SqlField[_, USER_REQUESTS]] = List(ID, EMAIL, GROUP_ID, CREATED_BY)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): USER_REQUESTS = new USER_REQUESTS(Some(alias))
}

private[database] object USER_REQUESTS {
  val table = new USER_REQUESTS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}