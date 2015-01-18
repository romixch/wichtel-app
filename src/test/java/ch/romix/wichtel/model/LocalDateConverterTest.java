package ch.romix.wichtel.model;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class LocalDateConverterTest {

  @Test
  public void testConverter() throws Exception {
    LocalDate now = LocalDate.now();
    LocalDateConverter conv = new LocalDateConverter();
    Date date = conv.convertToDatabaseColumn(now);
    LocalDate testDate = conv.convertToEntityAttribute(date);
    assertThat(testDate, is(now));
  }
}
