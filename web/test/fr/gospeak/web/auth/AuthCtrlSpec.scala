package fr.gospeak.web.auth

import fr.gospeak.core.testingutils.Generators._
import fr.gospeak.infra.services.InMemoryEmailSrv
import fr.gospeak.web.auth.AuthForms.SignupData
import fr.gospeak.web.auth.services.{AuthRepo, AuthSrv}
import fr.gospeak.web.testingutils.CtrlSpec
import org.scalatest.BeforeAndAfterEach
import play.api.http.Status
import play.api.mvc.{AnyContentAsFormUrlEncoded, RequestHeader, Result}
import play.api.test.Helpers._

import scala.concurrent.Future

class AuthCtrlSpec extends CtrlSpec with BeforeAndAfterEach {
  private val _ = aEmailAddress // to keep the `fr.gospeak.core.testingutils.Generators._` import
  private val authRepo = new AuthRepo(db)
  private val authSrv = AuthSrv(conf.auth.cookie, silhouette, db, authRepo, clock)
  private val emailSrv = new InMemoryEmailSrv()
  private val ctrl = new AuthCtrl(cc, silhouette, db, authSrv, emailSrv)
  private val redirect: Option[String] = None
  private val signupData = random[SignupData]

  override def beforeEach(): Unit = {
    db.createTables().unsafeRunSync()
    emailSrv.sentEmails.clear()
  }

  override def afterEach(): Unit = db.dropTables().unsafeRunSync()

  describe("AuthCtrl") {
    describe("signup") {
      it("should return form when not authenticated") {
        val res = ctrl.signup(redirect).apply(unsecuredReq)
        status(res) shouldBe Status.OK
      }
      it("should redirect to user home when authenticated") {
        val res = ctrl.signup(redirect).apply(securedReq)
        status(res) shouldBe Status.SEE_OTHER
      }
      it("should create a user and credentials then login") {
        val res = AuthCtrlSpec.doSignup(signupData)(unsecuredReq)(ctrl)
        status(res) shouldBe Status.SEE_OTHER
        headers(res) should contain key "Location"
        contentAsString(res) shouldBe ""
        emailSrv.sentEmails should have length 1
      }
      it("should find the user, create credentials then login") {

      }
      it("should fail if credentials already exist") {

      }
    }
    describe("login") {
      it("should return form when not authenticated") {
        val res = ctrl.login(redirect).apply(unsecuredReq)
        status(res) shouldBe Status.OK
      }
      it("should redirect to user home when authenticated") {
        val res = ctrl.login(redirect).apply(securedReq)
        status(res) shouldBe Status.SEE_OTHER
      }
    }
    describe("forgotPassword") {
      it("should return form when not authenticated") {
        val res = ctrl.forgotPassword(redirect).apply(unsecuredReq)
        status(res) shouldBe Status.OK
      }
      it("should redirect to user home when authenticated") {
        val res = ctrl.forgotPassword(redirect).apply(securedReq)
        status(res) shouldBe Status.SEE_OTHER
      }
    }
  }
}

object AuthCtrlSpec {
  def doSignup(data: SignupData, redirect: Option[String] = None)(req: RequestHeader)(ctrl: AuthCtrl): Future[Result] = {
    ctrl.doSignup(redirect).apply(req.withBody(AnyContentAsFormUrlEncoded(Map(
      "slug" -> Seq(data.slug.value),
      "first-name" -> Seq(data.firstName),
      "last-name" -> Seq(data.lastName),
      "email" -> Seq(data.email.value),
      "password" -> Seq(data.password.decode),
      "rememberMe" -> Seq(data.rememberMe.toString)
    ))))
  }
}