package org.cqframework.cql.runtime;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.*;

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
    int[] ret = Arrays.copyOf(temp, count);
    return ret;
  }

  public static DateTimeFieldType[] getFields(int numFields) {
    DateTimeFieldType[] ret = new DateTimeFieldType[numFields];
    for (int i = 0; i < numFields; ++i) {
      ret[i] = fields[i];
    }
    return ret;
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
    int [] values = new int[todayArray.length];
    for (int i = 0; i < todayArray.length; ++i)  {
      values[i] = Integer.parseInt(todayArray[i]);
    }
    return new DateTime(values, Arrays.copyOf(fields, 3), null);
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
