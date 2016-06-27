package org.cqframework.cql.runtime;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.*;

/*
  Notes on uncertainty comparisons:
    Equality:
      A = B
      if Alow <= Bhigh and Ahigh >= Blow
        then if Alow = Ahigh and Blow = Bhigh
          then true
          else null
      else false

    Relative Comparison w/out Equality
      A < B
      if Ahigh < Blow
        then true
      else if Alow >= Bhigh
        then false
      else null

    Relative Comparison with Equality
      A <= B
      if Ahigh <= Blow
        then true
      else if Alow > Bhigh
        then false
      else null

    ** Note carefully that these semantics introduce some asymmetries into the comparison operators.
       In particular, A = B or A < B is not equivalent to A <= B because of the uncertainty.
*/

/**
* Created by Chris Schuler on 6/20/2016
*/
public class DateTime {

  protected Partial dateTime;
  protected BigDecimal timezoneOffset;

  protected static DateTimeFieldType[] fields = new DateTimeFieldType[] {
    DateTimeFieldType.year(),
    DateTimeFieldType.monthOfYear(),
    DateTimeFieldType.dayOfMonth(),
    DateTimeFieldType.hourOfDay(),
    DateTimeFieldType.minuteOfHour(),
    DateTimeFieldType.secondOfMinute(),
    DateTimeFieldType.millisOfSecond(),
  };

  public static int[] getValues(Integer... values) {
    int count = 0;
    int[] temp = new int[7];
    for (Integer value : values) {
      if (value != null) {
        temp[count] = value;
        ++count;
      }
    }
    return Arrays.copyOf(temp, count);
  }

  public static DateTimeFieldType[] getFields(int numFields) {
    DateTimeFieldType[] ret = new DateTimeFieldType[numFields];
    for (int i = 0; i < numFields; ++i) {
      ret[i] = fields[i];
    }
    return ret;
  }

  public static DateTimeFieldType getField(int idx) {
    return fields[idx];
  }

  public static int getFieldIndex(String dateTimeElement) {
    // DateTimePrecision Enum represents precision as Titlecase Strings
    ArrayList<String> indexes = new ArrayList<>(Arrays.asList("year", "month", "day", "hour", "minute", "second", "millisecond"));
    return indexes.indexOf(dateTimeElement.toLowerCase());
  }

  public static int getFieldIndex2(String dateTimeElement) {
    ArrayList<String> indexes = new ArrayList<>(Arrays.asList("years", "months", "days", "hours", "minutes", "seconds", "milliseconds"));
    return indexes.indexOf(dateTimeElement.toLowerCase());
  }

  public Partial getPartial() {
    return dateTime;
  }

  public void setPartial(Partial newDateTime) {
    dateTime = newDateTime;
  }

  public DateTime withPartial(Partial newDateTime) {
    setPartial(newDateTime);
    return this;
  }

  public BigDecimal getTimezoneOffset() {
    return timezoneOffset;
  }

  public void setTimezoneOffset(BigDecimal newTimezoneOffset) {
    timezoneOffset = newTimezoneOffset;
  }

  public DateTime withTimezoneOffset(BigDecimal newTimezoneOffset) {
    setTimezoneOffset(newTimezoneOffset);
    return this;
  }

  public static DateTime getToday() {
    org.joda.time.DateTime dt = org.joda.time.DateTime.now();
    int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), 0, 0, 0, 0 };
    return new DateTime().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
  }

  public static DateTime getNow() {
    org.joda.time.DateTime dt = org.joda.time.DateTime.now();
    int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), dt.hourOfDay().get(),
                      dt.minuteOfHour().get(), dt.secondOfMinute().get(), dt.millisOfSecond().get() };
    return new DateTime().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
  }

  public static Boolean formatCheck(ArrayList<Object> timeElements) {
    boolean prevNull = false;
    for (Object element : timeElements) {
      if (element == null) { prevNull = true; }
      else if (element != null && prevNull) {
        return false;
      }
    }
    return true;
  }
}
