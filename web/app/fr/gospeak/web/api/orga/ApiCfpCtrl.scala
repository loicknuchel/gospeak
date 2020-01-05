package fr.gospeak.web.api.orga

import cats.data.OptionT
import com.mohiva.play.silhouette.api.Silhouette
import fr.gospeak.core.domain.{Cfp, Group}
import fr.gospeak.core.services.storage.{OrgaCfpRepo, OrgaGroupRepo, OrgaUserRepo}
import fr.gospeak.libs.scalautils.domain.Page
import fr.gospeak.web.AppConf
import fr.gospeak.web.api.domain.ApiCfp
import fr.gospeak.web.api.domain.utils.ApiResult
import fr.gospeak.web.auth.domain.CookieEnv
import fr.gospeak.web.utils.ApiCtrl
import play.api.mvc.{Action, AnyContent, ControllerComponents}

class ApiCfpCtrl(cc: ControllerComponents,
                 silhouette: Silhouette[CookieEnv],
                 conf: AppConf,
                 userRepo: OrgaUserRepo,
                 val groupRepo: OrgaGroupRepo,
                 cfpRepo: OrgaCfpRepo) extends ApiCtrl(cc, silhouette, conf) with ApiCtrl.OrgaAction {
  def list(group: Group.Slug, params: Page.Params): Action[AnyContent] = OrgaAction[Seq[ApiCfp.Orga]](group) { implicit req =>
    for {
      cfps <- cfpRepo.list(params)
      users <- userRepo.list(cfps.items.flatMap(_.users))
    } yield ApiResult.of(cfps, (c: Cfp) => ApiCfp.orga(c, users))
  }

  def detail(group: Group.Slug, cfp: Cfp.Slug): Action[AnyContent] = OrgaAction[ApiCfp.Orga](group) { implicit req =>
    (for {
      cfpElt <- OptionT(cfpRepo.find(cfp))
      users <- OptionT.liftF(userRepo.list(cfpElt.users))
      res = ApiResult.of(ApiCfp.orga(cfpElt, users))
    } yield res).value.map(_.getOrElse(cfpNotFound(cfp)))
  }

  // TODO def userRatings(group: Group.Slug, cfp: Cfp.Slug)
}
