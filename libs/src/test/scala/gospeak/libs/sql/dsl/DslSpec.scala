package gospeak.libs.sql.dsl

import doobie.implicits._
import gospeak.libs.sql.testingutils.Entities.{Post, User}
import gospeak.libs.sql.testingutils.SqlSpec
import gospeak.libs.sql.testingutils.database.Tables._

class DslSpec extends SqlSpec {
  describe("Table") {
    it("should build CRUD operations") {
      val u = User(User.Id(4), "Lou", "lou@mail.com")
      val u2 = u.copy(name = "Test", email = "test@mail.com")
      val select = USERS.select.where(_.ID eq u.id).build[User]
      val insert = USERS.insert.values(u.id, u.name, u.email)
      val update = USERS.update.set(_.NAME, u2.name).set(_.EMAIL, u2.email).where(_.ID eq u.id)
      val delete = USERS.delete.where(_.ID eq u.id)

      select.fr.query.sql shouldBe "SELECT u.id, u.name, u.email FROM users u WHERE u.id=?"
      insert.fr.query.sql shouldBe "INSERT INTO users (id, name, email) VALUES (?, ?, ?)"
      update.fr.query.sql shouldBe "UPDATE users u SET name=?, email=? WHERE u.id=?"
      delete.fr.query.sql shouldBe "DELETE FROM users u WHERE u.id=?"

      select.runOption(xa).unsafeRunSync() shouldBe None
      insert.run(xa).unsafeRunSync()
      select.runOption(xa).unsafeRunSync() shouldBe Some(u)
      update.run(xa).unsafeRunSync()
      select.runOption(xa).unsafeRunSync() shouldBe Some(u2)
      delete.run(xa).unsafeRunSync()
      select.runOption(xa).unsafeRunSync() shouldBe None
    }
    it("should build a joined select query") {
      val joined = POSTS.join(USERS).on(_.AUTHOR eq _.ID).joinOpt(CATEGORIES).on(POSTS.CATEGORY eq _.ID)
      val res = joined.select.fields(POSTS.getFields).where(USERS.ID eq User.loic.id).build[Post]
      val exp = fr0"SELECT p.id, p.title, p.text, p.date, p.author, p.category FROM posts p " ++
        fr0"INNER JOIN users u ON p.author=u.id " ++
        fr0"LEFT OUTER JOIN categories c ON p.category=c.id " ++
        fr0"WHERE u.id=${User.loic.id}"
      res.fr.query.sql shouldBe exp.query.sql
      res.runList(xa).unsafeRunSync() shouldBe List(Post.newYear, Post.first2020)
    }
    it("should have auto joins") {
      val join = POSTS.join(USERS).on(_.AUTHOR eq _.ID)
      val autoJoin = POSTS.AUTHOR.join
      autoJoin shouldBe join
    }
  }
}