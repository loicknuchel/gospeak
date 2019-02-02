package fr.gospeak.web.utils

import fr.gospeak.libs.scalautils.domain.Page
import play.api.mvc.QueryStringBindable

object QueryStringBindables {
  implicit def pageParamsQueryStringBindable(implicit stringBinder: QueryStringBindable[String], intBinder: QueryStringBindable[Int]): QueryStringBindable[Page.Params] =
    new QueryStringBindable[Page.Params] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Page.Params]] =
        Some(for {
          page <- intBinder.bind(Page.No.key, params).map(_.map(p => Page.No(p))).getOrElse(Right(Page.Params.defaults.page))
          pageSize <- intBinder.bind(Page.Size.key, params).map(_.map(p => Page.Size(p))).getOrElse(Right(Page.Params.defaults.pageSize))
          search <- stringBinder.bind(Page.Search.key, params).map(_.map(s => Some(Page.Search(s)))).getOrElse(Right(Page.Params.defaults.search))
          orderBy <- stringBinder.bind(Page.OrderBy.key, params).map(_.map(s => Some(Page.OrderBy(s)))).getOrElse(Right(Page.Params.defaults.orderBy))
        } yield Page.Params(page, pageSize, search, orderBy))

      override def unbind(key: String, params: Page.Params): String =
        Seq(
          Some(params.page).filter(_.nonEmpty).map(p => intBinder.unbind(p.key, p.value)),
          Some(params.pageSize).filter(_.nonEmpty).map(p => intBinder.unbind(p.key, p.value)),
          params.search.filter(_.nonEmpty).map(p => stringBinder.unbind(p.key, p.value)),
          params.orderBy.filter(_.nonEmpty).map(p => stringBinder.unbind(p.key, p.value))
        ).flatten.mkString("&")
    }
}
