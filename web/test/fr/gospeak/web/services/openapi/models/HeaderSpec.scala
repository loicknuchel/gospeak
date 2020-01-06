package fr.gospeak.web.services.openapi.models

import fr.gospeak.web.services.openapi.OpenApiFactory.Formats._
import fr.gospeak.web.services.openapi.models.utils.Markdown
import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json.{JsSuccess, Json}

class HeaderSpec extends FunSpec with Matchers {
  describe("Header") {
    it("should parse and serialize") {
      val json = Json.parse(HeaderSpec.jsonStr)
      json.validate[Header] shouldBe JsSuccess(HeaderSpec.value)
      Json.toJson(HeaderSpec.value) shouldBe json
    }
  }
}

object HeaderSpec {
  val jsonStr: String =
    s"""{
       |  "description": "Header desc",
       |  "schema": ${SchemaSpec.jsonStr}
       |}""".stripMargin
  val value = Header(
    description = Some(Markdown("Header desc")),
    schema = Some(SchemaSpec.value))
}
