package models

import org.joda.time.DateTime
import scala.slick.lifted.MappedTypeMapper
import java.util.Locale

object DatabaseTypeMappers {
  implicit val jodaTimeTypeMapper = MappedTypeMapper.base[DateTime, java.sql.Date](
    x => new java.sql.Date(x.toDate().getTime()),
    x => new DateTime(x.getTime))

  implicit val authModeTypeMapper = MappedTypeMapper.base[AuthenticationMode.AuthenticationMode, String](
    x => x.toString,
    x => AuthenticationMode.withName(x))

  implicit val localeTypeMapper = MappedTypeMapper.base[Locale, String](
    x => x.toLanguageTag(),
    x => Locale.forLanguageTag(x))
}