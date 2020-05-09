package gospeak.infra.services.storage.sql

import java.time.Instant

import cats.effect.IO
import doobie.implicits._
import gospeak.core.domain._
import gospeak.core.domain.utils.UserAwareCtx
import gospeak.core.services.storage.VideoRepo
import gospeak.infra.services.storage.sql.VideoRepoSql._
import gospeak.infra.services.storage.sql.utils.DoobieUtils.Mappings._
import gospeak.infra.services.storage.sql.utils.DoobieUtils.{Field, Insert, Select, SelectPage, Sort, Update}
import gospeak.infra.services.storage.sql.utils.GenericRepo
import gospeak.libs.scala.Extensions._
import gospeak.libs.scala.domain.{Done, Page, Url}

class VideoRepoSql(protected[sql] val xa: doobie.Transactor[IO]) extends GenericRepo with VideoRepo {
  override def create(video: Video.Data, now: Instant): IO[Video] = insert(Video(video, now)).run(xa)

  override def edit(video: Video.Data, now: Instant): IO[Done] = update(video, now).run(xa)

  override def find(videoId: String): IO[Option[Video]] = selectOne(videoId).runOption(xa)

  override def list(params: Page.Params)(implicit ctx: UserAwareCtx): IO[Page[Video]] = selectPage(params).run(xa)

  override def count(v: Url.Videos): IO[Long] = v match {
    case c: Url.YouTube.Channel => countChannelId(c.containerId).runOption(xa).map(_.getOrElse(0))
    case p: Url.YouTube.Playlist => countPlaylistId(p.containerId).runOption(xa).map(_.getOrElse(0))
    case c: Url.Vimeo.Channel => countChannelId(c.containerId).runOption(xa).map(_.getOrElse(0))
    case p: Url.Vimeo.Showcase => countPlaylistId(p.containerId).runOption(xa).map(_.getOrElse(0))
  }
}

object VideoRepoSql {
  private val _ = urlMeta // for intellij not remove DoobieUtils.Mappings import
  private val table = Tables.videos
  private val tableSelect = table.dropField(_.platform).get.dropField(_.id).get

  private[sql] def insert(e: Video): Insert[Video] = {
    val values = fr0"${e.url.platform}, ${e.url}, ${e.url.videoId}, ${e.channel.id}, ${e.channel.name}, ${e.playlist.map(_.id)}, ${e.playlist.map(_.name)}, ${e.title}, ${e.description}, ${e.tags}, ${e.publishedAt}, ${e.duration}, ${e.lang}, ${e.views}, ${e.likes}, ${e.dislikes}, ${e.comments}, ${e.updatedAt}"
    table.insert[Video](e, _ => values)
  }

  private[sql] def update(data: Video.Data, now: Instant): Update = {
    val fields = fr0"title=${data.title}, description=${data.description}, tags=${data.tags}, duration=${data.duration}, lang=${data.lang}, views=${data.views}, likes=${data.likes}, dislikes=${data.dislikes}, comments=${data.comments}, updated_at=$now"
    table.update(fields, fr0"WHERE id=${data.url.videoId}")
  }

  private[sql] def selectOne(videoId: String): Select[Video] =
    tableSelect.select[Video](fr0"WHERE vi.id=$videoId")

  private[sql] def selectPage(params: Page.Params)(implicit ctx: UserAwareCtx): SelectPage[Video, UserAwareCtx] =
    tableSelect.selectPage[Video, UserAwareCtx](params)

  private[sql] def countChannelId(channelId: String): Select[Long] =
    tableSelect.select[Long](Seq(Field("COUNT(*)", "")), fr0"WHERE vi.channel_id=$channelId GROUP BY vi.channel_id", Sort("channel_id", "vi"))

  private[sql] def countPlaylistId(playlistId: String): Select[Long] =
    tableSelect.select[Long](Seq(Field("COUNT(*)", "")), fr0"WHERE vi.playlist_id=$playlistId GROUP BY vi.playlist_id", Sort("playlist_id", "vi"))
}
