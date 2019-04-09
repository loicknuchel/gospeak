package fr.gospeak.web.pages.speaker.proposals

import java.time.Instant

import cats.data.OptionT
import cats.effect.IO
import com.mohiva.play.silhouette.api.Silhouette
import fr.gospeak.core.domain._
import fr.gospeak.core.services._
import fr.gospeak.libs.scalautils.domain.{Page, Slides, Video}
import fr.gospeak.web.auth.domain.CookieEnv
import fr.gospeak.web.domain.Breadcrumb
import fr.gospeak.web.pages.speaker.TalkCtrl
import fr.gospeak.web.pages.speaker.proposals.ProposalCtrl._
import fr.gospeak.web.utils.{GenericForm, UICtrl}
import play.api.mvc._

class ProposalCtrl(cc: ControllerComponents,
                   silhouette: Silhouette[CookieEnv],
                   userRepo: UserRepo,
                   cfpRepo: CfpRepo,
                   eventRepo: EventRepo,
                   talkRepo: TalkRepo,
                   proposalRepo: ProposalRepo) extends UICtrl(cc, silhouette) {

  import silhouette._

  def list(talk: Talk.Slug, params: Page.Params): Action[AnyContent] = SecuredAction.async { implicit req =>
    (for {
      talkElt <- OptionT(talkRepo.find(req.identity.user.id, talk))
      proposals <- OptionT.liftF(proposalRepo.list(talkElt.id, params))
      events <- OptionT.liftF(eventRepo.list(proposals.items.flatMap(_._2.event)))
      h = TalkCtrl.header(talkElt.slug)
      b = listBreadcrumb(req.identity.user.name, talk -> talkElt.title)
    } yield Ok(html.list(talkElt, proposals, events)(h, b))).value.map(_.getOrElse(talkNotFound(talk))).unsafeToFuture()
  }

  def detail(talk: Talk.Slug, proposal: Proposal.Id): Action[AnyContent] = SecuredAction.async { implicit req =>
    (for {
      talkElt <- OptionT(talkRepo.find(req.identity.user.id, talk))
      proposalElt <- OptionT(proposalRepo.find(proposal))
      cfpElt <- OptionT(cfpRepo.find(proposalElt.cfp))
      speakers <- OptionT.liftF(userRepo.list(proposalElt.speakers.toList))
      events <- OptionT.liftF(eventRepo.list(proposalElt.event.toSeq))
      h = TalkCtrl.header(talkElt.slug)
      b = breadcrumb(req.identity.user.name, talk -> talkElt.title, proposal -> cfpElt.name)
    } yield Ok(html.detail(talkElt, cfpElt, proposalElt, speakers, events, GenericForm.embed)(h, b))).value.map(_.getOrElse(proposalNotFound(talk, proposal))).unsafeToFuture()
  }

  def doAddSlides(talk: Talk.Slug, proposal: Proposal.Id): Action[AnyContent] = SecuredAction.async { implicit req =>
    val now = Instant.now()
    val next = Redirect(routes.ProposalCtrl.detail(talk, proposal))
    GenericForm.embed.bindFromRequest.fold(
      formWithErrors => IO.pure(next.flashing(formWithErrors.errors.map(e => "error" -> e.format): _*)),
      data => Slides.from(data) match {
        case Left(err) => IO.pure(next.flashing(err.errors.map(e => "error" -> e.value): _*))
        case Right(slides) => proposalRepo.editSlides(proposal)(slides, now, req.identity.user.id).map(_ => next)
      }
    ).unsafeToFuture()
  }

  def doAddVideo(talk: Talk.Slug, proposal: Proposal.Id): Action[AnyContent] = SecuredAction.async { implicit req =>
    val now = Instant.now()
    val next = Redirect(routes.ProposalCtrl.detail(talk, proposal))
    GenericForm.embed.bindFromRequest.fold(
      formWithErrors => IO.pure(next.flashing(formWithErrors.errors.map(e => "error" -> e.format): _*)),
      data => Video.from(data) match {
        case Left(err) => IO.pure(next.flashing(err.errors.map(e => "error" -> e.value): _*))
        case Right(video) => proposalRepo.editVideo(proposal)(video, now, req.identity.user.id).map(_ => next)
      }
    ).unsafeToFuture()
  }
}

object ProposalCtrl {
  def listBreadcrumb(user: User.Name, talk: (Talk.Slug, Talk.Title)): Breadcrumb =
    talk match {
      case (talkSlug, _) => TalkCtrl.breadcrumb(user, talk).add("Proposals" -> routes.ProposalCtrl.list(talkSlug))
    }

  def breadcrumb(user: User.Name, talk: (Talk.Slug, Talk.Title), proposal: (Proposal.Id, Cfp.Name)): Breadcrumb =
    (talk, proposal) match {
      case ((talkSlug, _), (proposalId, cfpName)) =>
        listBreadcrumb(user, talk).add(cfpName.value -> routes.ProposalCtrl.detail(talkSlug, proposalId))
    }
}