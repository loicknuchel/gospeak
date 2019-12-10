package fr.gospeak.core.services.storage

import cats.effect.IO
import fr.gospeak.core.domain.utils.OrgaCtx
import fr.gospeak.core.domain.{Group, Partner}
import fr.gospeak.libs.scalautils.domain.{Done, Page}

trait PartnerRepo extends OrgaPartnerRepo with SuggestPartnerRepo

trait OrgaPartnerRepo {
  def create(data: Partner.Data)(implicit ctx: OrgaCtx): IO[Partner]

  def edit(partner: Partner.Slug, data: Partner.Data)(implicit ctx: OrgaCtx): IO[Done]

  def remove(partner: Partner.Slug)(implicit ctx: OrgaCtx): IO[Done]

  def list(params: Page.Params)(implicit ctx: OrgaCtx): IO[Page[Partner]]

  def listFull(params: Page.Params)(implicit ctx: OrgaCtx): IO[Page[Partner.Full]]

  def list(partners: Seq[Partner.Id]): IO[Seq[Partner]]

  def find(partner: Partner.Id)(implicit ctx: OrgaCtx): IO[Option[Partner]]

  def find(partner: Partner.Slug)(implicit ctx: OrgaCtx): IO[Option[Partner]]

  def find(group: Group.Id, partner: Partner.Slug): IO[Option[Partner]]
}

trait SuggestPartnerRepo {
  def list(group: Group.Id, params: Page.Params): IO[Page[Partner]]

  def list(group: Group.Id): IO[Seq[Partner]]
}
