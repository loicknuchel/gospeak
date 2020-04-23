package gospeak.libs.scala

import gospeak.libs.scala.StringUtils._
import gospeak.libs.testingutils.BaseSpec

class StringUtilsSpec extends BaseSpec {
  describe("StringUtils") {
    describe("leftPad") {
      it("should pad Strings") {
        leftPad("test") shouldBe "      test"
        leftPad("test", 3) shouldBe "test"
      }
    }
    describe("rightPad") {
      it("should pad Strings") {
        rightPad("test") shouldBe "test      "
        rightPad("test", 3) shouldBe "test"
      }
    }
    describe("removeDiacritics") {
      it("should remove special chars") {
        removeDiacritics("téléphone") shouldBe "telephone"
      }
    }
    describe("slugify") {
      it("should remove special chars and replace the") {
        slugify("HumanTalks + Paris") shouldBe "humantalks-paris"
      }
    }
  }
}
