package fr.gospeak.infra.services.storage.sql

import cats.data.NonEmptyList
import fr.gospeak.core.domain.Talk
import fr.gospeak.infra.services.storage.sql.TalkRepoSql._
import fr.gospeak.infra.services.storage.sql.testingutils.RepoSpec

class TalkRepoSqlSpec extends RepoSpec {
  describe("TalkRepoSql") {
    it("should create and retrieve") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      talkRepo.list(user.id, page).unsafeRunSync().items shouldBe Seq()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync() shouldBe None
      val talk = talkRepo.create(user.id, talkData1, now).unsafeRunSync()
      talkRepo.list(user.id, page).unsafeRunSync().items shouldBe Seq(talk)
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync() shouldBe Some(talk)
    }
    it("should not retrieve not owned talks") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      val user2 = userRepo.create(userData2, now).unsafeRunSync()
      val talk = talkRepo.create(user2.id, talkData1, now).unsafeRunSync()
      talkRepo.list(user.id, page).unsafeRunSync().items shouldBe Seq()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync() shouldBe None
    }
    it("should fail on duplicate slug on same user") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      val user2 = userRepo.create(userData2, now).unsafeRunSync()
      talkRepo.create(user.id, talkData1, now).unsafeRunSync()
      talkRepo.create(user2.id, talkData1, now).unsafeRunSync()
      an[Exception] should be thrownBy talkRepo.create(user.id, talkData1, now).unsafeRunSync()
    }
    it("should update talk data") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      talkRepo.create(user.id, talkData1, now).unsafeRunSync()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync().map(_.data) shouldBe Some(talkData1)
      talkRepo.edit(user.id, talkData1.slug)(talkData2, now).unsafeRunSync()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync() shouldBe None
      talkRepo.find(user.id, talkData2.slug).unsafeRunSync().map(_.data) shouldBe Some(talkData2)
    }
    it("should fail to change slug for an existing one") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      talkRepo.create(user.id, talkData1, now).unsafeRunSync()
      talkRepo.create(user.id, talkData2, now).unsafeRunSync()
      an[Exception] should be thrownBy talkRepo.edit(user.id, talkData1.slug)(talkData1.copy(slug = talkData2.slug), now).unsafeRunSync()
    }
    it("should update the status") {
      val user = userRepo.create(userData1, now).unsafeRunSync()
      talkRepo.create(user.id, talkData1, now).unsafeRunSync()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync().map(_.status) shouldBe Some(Talk.Status.Draft)
      talkRepo.editStatus(user.id, talkData1.slug)(Talk.Status.Public).unsafeRunSync()
      talkRepo.find(user.id, talkData1.slug).unsafeRunSync().map(_.status) shouldBe Some(Talk.Status.Public)
    }
    describe("Queries") {
      it("should build insert") {
        val q = insert(talk)
        q.sql shouldBe "INSERT INTO talks (id, slug, title, duration, status, description, speakers, slides, video, created, created_by, updated, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        check(q)
      }
      it("should build update") {
        val q = update(user.id, talk.slug)(talkData1, now)
        q.sql shouldBe "UPDATE talks SET slug=?, title=?, duration=?, description=?, slides=?, video=?, updated=?, updated_by=? WHERE speakers LIKE ? AND slug=?"
        check(q)
      }
      it("should build updateStatus") {
        val q = updateStatus(user.id, talk.slug)(Talk.Status.Public)
        q.sql shouldBe "UPDATE talks SET status=? WHERE speakers LIKE ? AND slug=?"
        check(q)
      }
      it("should build updateSlides") {
        val q = updateSlides(user.id, talk.slug)(slides, now)
        q.sql shouldBe "UPDATE talks SET slides=?, updated=?, updated_by=? WHERE speakers LIKE ? AND slug=?"
        check(q)
      }
      it("should build updateVideo") {
        val q = updateVideo(user.id, talk.slug)(video, now)
        q.sql shouldBe "UPDATE talks SET video=?, updated=?, updated_by=? WHERE speakers LIKE ? AND slug=?"
        check(q)
      }
      it("should build selectOne") {
        val q = selectOne(user.id, talk.slug)
        q.sql shouldBe "SELECT id, slug, title, duration, status, description, speakers, slides, video, created, created_by, updated, updated_by FROM talks WHERE speakers LIKE ? AND slug=?"
        check(q)
      }
      it("should build selectPage") {
        val (s, c) = selectPage(user.id, params)
        s.sql shouldBe "SELECT id, slug, title, duration, status, description, speakers, slides, video, created, created_by, updated, updated_by FROM talks WHERE speakers LIKE ? ORDER BY title OFFSET 0 LIMIT 20"
        c.sql shouldBe "SELECT count(*) FROM talks WHERE speakers LIKE ? "
        check(s)
        check(c)
      }
      it("should build selectAll") {
        val q = selectAll(NonEmptyList.of(talk.id))
        q.sql shouldBe "SELECT id, slug, title, duration, status, description, speakers, slides, video, created, created_by, updated, updated_by FROM talks WHERE id IN (?) "
        check(q)
      }
    }
  }
}