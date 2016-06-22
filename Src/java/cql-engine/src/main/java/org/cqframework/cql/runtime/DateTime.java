package org.cqframework.cql.runtime;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

  public DateTime (int[] values, DateTimeFieldType[] fields, BigDecimal timezoneOffset) {
    dateTime = new Partial(fields, values);
    this.timezoneOffset = timezoneOffset == null ? new BigDecimal(0) : timezoneOffset;
  }

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

  public Partial getPartial() {
    return dateTime;
  }

  public BigDecimal getTimezoneOffset() {
    return timezoneOffset;
  }

  public static DateTime getToday() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
    Date date = new Date();
    String todayString = dateFormat.format(date);
    String [] todayArray = todayString.split(" ");
    int [] values = new int[7];
    for (int i = 0; i < todayArray.length; ++i)  {
      values[i] = Integer.parseInt(todayArray[i]);
    }
    return new DateTime(values, fields, null);
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
