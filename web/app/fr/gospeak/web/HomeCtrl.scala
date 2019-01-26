package fr.gospeak.web

import fr.gospeak.core.domain.User
import fr.gospeak.core.services.GospeakDb
import fr.gospeak.web.HomeCtrl._
import fr.gospeak.web.auth.AuthService
import fr.gospeak.web.domain.{HeaderInfo, NavLink}
import fr.gospeak.web.utils.UICtrl
import play.api.mvc._

class HomeCtrl(cc: ControllerComponents, db: GospeakDb, auth: AuthService) extends UICtrl(cc) {
  def index(): Action[AnyContent] = Action { implicit req: Request[AnyContent] =>
    implicit val user: Option[User] = auth.userAware()
    Ok(html.index()(header))
  }
}

object HomeCtrl {
  val publicNav: Seq[NavLink] = Seq(
    NavLink("Cfps", cfps.routes.CfpCtrl.list()),
    NavLink("Groups", groups.routes.GroupCtrl.list()),
    NavLink("Speakers", speakers.routes.SpeakerCtrl.list()))

  val header: HeaderInfo = HeaderInfo(
    brand = NavLink("Gospeak", routes.HomeCtrl.index()),
    links = publicNav,
    rightLinks = Seq(NavLink("login", auth.routes.AuthCtrl.login())))
}
