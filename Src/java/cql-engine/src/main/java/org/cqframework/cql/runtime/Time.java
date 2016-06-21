package org.cqframework.cql.runtime;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import java.math.BigDecimal;

/**
* Created by Chris Schuler on 6/16/2016
*/
public class Time {
  protected static DateTimeFieldType[] fields = new DateTimeFieldType[] {
    DateTimeFieldType.hourOfDay(),
    DateTimeFieldType.minuteOfHour(),
    DateTimeFieldType.secondOfMinute(),
    DateTimeFieldType.millisOfSecond(),
  };

  protected Partial time;
  protected BigDecimal timezoneOffset;

  public Time(int[] values, DateTimeFieldType[] fields, BigDecimal timezoneOffset) {
    time = new Partial(fields, values);
    this.timezoneOffset = timezoneOffset == null ? new BigDecimal(0) : timezoneOffset;
  }

  public static DateTimeFieldType[] getFields(int numFields) {
    DateTimeFieldType[] ret = new DateTimeFieldType[numFields];
    for (int i = 0; i < numFields; ++i) {
      ret[i] = fields[i];
    }
    return ret;
  }

  public Partial getPartial() {
    return time;
  }

  public BigDecimal getTimezoneOffset() {
    return timezoneOffset;
  }
}
