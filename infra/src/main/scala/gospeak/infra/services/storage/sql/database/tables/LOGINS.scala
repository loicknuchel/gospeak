package gospeak.infra.services.storage.sql.database.tables

import java.time.LocalDateTime

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
class LOGINS private(getAlias: Option[String] = Some("lg")) extends Table.SqlTable("PUBLIC", "logins", getAlias) {
  type Self = LOGINS

  val PROVIDER_ID: SqlFieldRaw[User.ProviderId, LOGINS] = SqlField(this, "provider_id", "VARCHAR(30) NOT NULL", JdbcType.VarChar, nullable = false, 1)
  val PROVIDER_KEY: SqlFieldRaw[User.ProviderKey, LOGINS] = SqlField(this, "provider_key", "VARCHAR(100) NOT NULL", JdbcType.VarChar, nullable = false, 2)
  val USER_ID: SqlFieldRef[User.Id, LOGINS, USERS] = SqlField(this, "user_id", "CHAR(36) NOT NULL", JdbcType.Char, nullable = false, 3, USERS.table.ID)

  override def getFields: List[SqlField[_, LOGINS]] = List(PROVIDER_ID, PROVIDER_KEY, USER_ID)

  override def getSorts: List[Sort] = List()

  override def searchOn: List[SqlField[_, LOGINS]] = List(PROVIDER_ID, PROVIDER_KEY, USER_ID)

  override def getFilters: List[Filter] = List()

  def alias(alias: String): LOGINS = new LOGINS(Some(alias))
}

private[database] object LOGINS {
  val table = new LOGINS() // table instance, should be accessed through `gospeak.infra.services.storage.sql.database.Tables` object
}
