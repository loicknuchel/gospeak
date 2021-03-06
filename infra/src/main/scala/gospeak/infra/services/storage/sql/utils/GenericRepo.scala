package gospeak.infra.services.storage.sql.utils

import cats.data.NonEmptyList
import cats.effect.IO
import fr.loicknuchel.safeql.Query
import gospeak.libs.scala.Extensions._

trait GenericRepo {
  protected[sql] val xa: doobie.Transactor[IO]

  protected def runNel[Id, A](run: NonEmptyList[Id] => Query.Select.All[A], ids: List[Id]): IO[List[A]] =
    ids.distinct.toNel.map(run(_).run(xa)).getOrElse(IO.pure(List()))
}
