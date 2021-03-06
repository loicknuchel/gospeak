package gospeak.core.services.storage

import gospeak.core.testingutils.BaseSpec
import gospeak.libs.scala.domain.Secret

class DbConfSpec extends BaseSpec {
  describe("DatabaseConf") {
    describe("from") {
      it("should parse a H2 url") {
        val url = "jdbc:h2:mem:gospeak_db;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1"
        DbConf.from(url) shouldBe Right(DbConf.H2(url))
      }
      it("should parse a local PostgreSQL url") {
        val url = "postgres://user:pass@localhost:5432/gospeak"
        DbConf.from(url) shouldBe Right(DbConf.PostgreSQL(
          "jdbc:postgresql://localhost:5432/gospeak",
          "user",
          Secret("pass")))
      }
      it("should parse a local PostgreSQL url with jdbc prefix") {
        val url = "jdbc:postgresql://user:pass@localhost:5432/gospeak"
        DbConf.from(url) shouldBe Right(DbConf.PostgreSQL(
          "jdbc:postgresql://localhost:5432/gospeak",
          "user",
          Secret("pass")))
      }
      it("should parse a PostgreSQL url") {
        val url = "postgres://ugmagymioprjcz:4eece8951709fdd97f7de69aeb812692a937d11aa32fd482cda27a0fab0eebb1@ec2-42-149-162-364.eu-west-1.compute.amazonaws.com:5432/bid2dp7shda753"
        DbConf.from(url) shouldBe Right(DbConf.PostgreSQL(
          "jdbc:postgresql://ec2-42-149-162-364.eu-west-1.compute.amazonaws.com:5432/bid2dp7shda753",
          "ugmagymioprjcz",
          Secret("4eece8951709fdd97f7de69aeb812692a937d11aa32fd482cda27a0fab0eebb1")))
      }
    }
  }
}
