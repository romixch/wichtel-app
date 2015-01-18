package ch.romix.wichtel.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
  @Override
  public Date convertToDatabaseColumn(LocalDate entityDate) {
    Date dbDate = null;
    if (entityDate != null) {
      Instant instant = entityDate.atStartOfDay().atZone(ZoneId.of("Europe/Zurich")).toInstant();
      dbDate = Date.from(instant);
    }
    return dbDate;
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbDate) {
    LocalDate entityDate = null;
    if (dbDate != null) {
      Instant instant = dbDate.toInstant();
      LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Zurich"));
      entityDate = localDateTime.toLocalDate();
    }
    return entityDate;
  }
}
