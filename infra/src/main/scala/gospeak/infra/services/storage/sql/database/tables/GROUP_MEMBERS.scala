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
class GROUP_MEMBERS private(getAlias: Option[String] = Some("gm")) extends Table.SqlTable("PUBLIC", "group_members", getAlias) {
  type Self = GROUP_MEMBERS

  val GROUP_ID: SqlFieldRef[Group.Id, GROUP_MEMBERS, GROUPS] = SqlField(this, "group_id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 1, GROUPS.table.ID)
  val USER_ID: SqlFieldRef[User.Id, GROUP_MEMBERS, USERS] = SqlField(this, "user_id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 2, USERS.table.ID)
  val ROLE: SqlFieldRaw[String, GROUP_MEMBERS] = SqlField(this, "role", "VARCHAR(10) NOT NULL", JdbcType.VarChar, nullable = false, 3)
  val PRESENTATION: SqlFieldRaw[String, GROUP_MEMBERS] = SqlField(this, "presentation", "VARCHAR(4096)", JdbcType.VarChar, nullable = true, 4)
  val JOINED_AT: SqlFieldRaw[Instant, GROUP_MEMBERS] = SqlField(this, "joined_at", "TIMESTAMP NOT NULL", JdbcType.Timestamp, nullable = false, 5)
  val LEAVED_AT: SqlFieldRaw[Instant, GROUP_MEMBERS] = SqlField(this, "leaved_at", "TIMESTAMP", JdbcType.Timestamp, nullable = true, 6)

  override def getFields: List[SqlField[_, GROUP_MEMBERS]] = List(GROUP_ID, USER_ID, ROLE, PRESENTATION, JOINED_AT, LEAVED_AT)

  override def getSorts: List[Sort] = List(Sort("joined", "join date", NonEmptyList.of(JOINED_AT.asc)))

  override def searchOn: List[SqlField[_, GROUP_MEMBERS]] = List(ROLE, PRESENTATION)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): GROUP_MEMBERS = new GROUP_MEMBERS(Some(alias))
}

private[database] object GROUP_MEMBERS {
  val table = new GROUP_MEMBERS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}
