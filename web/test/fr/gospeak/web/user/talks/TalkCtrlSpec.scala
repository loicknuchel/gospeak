package fr.gospeak.web.pages.speaker

import fr.gospeak.libs.scalautils.domain.Page
import fr.gospeak.web.testingutils.CtrlSpec
import org.scalatest.BeforeAndAfterEach
import play.api.http.Status
import play.api.test.Helpers._

class TalkCtrlSpec extends CtrlSpec with BeforeAndAfterEach {
  private val params = Page.Params()
  private val ctrl = new TalkCtrl(cc, silhouette, db.user, db.talk, db.event, db.proposal)

  override def beforeEach(): Unit = db.createTables().unsafeRunSync()

  override def afterEach(): Unit = db.dropTables().unsafeRunSync()

  describe("TalkCtrl") {
    describe("list") {
      it("should return 200") {
        val res = ctrl.list(params).apply(securedReq)
        status(res) shouldBe Status.OK
        contentAsString(res) should include("""<div class="jumbotron">""")
      }
    }
  }
}
