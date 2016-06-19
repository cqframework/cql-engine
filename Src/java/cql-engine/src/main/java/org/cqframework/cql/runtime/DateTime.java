package org.cqframework.cql.runtime;

import java.math.BigDecimal;
import java.text.*;
import java.util.Date;
import java.util.ArrayList;


/**
* Created by Chris Schuler on 6/16/2016
*/
public class DateTime extends Time {

  private Integer year;
  private Integer month;
  private Integer day;

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

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public DateTime withYear(Integer year) {
    setYear(year);
    return this;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public DateTime withMonth(Integer month) {
    setMonth(month);
    return this;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public DateTime withDay(Integer day) {
    setDay(day);
    return this;
  }

  public DateTime getToday() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
    Date date = new Date();
    String todayString = dateFormat.format(date);
    String [] todayArray = todayString.split(" ");
    return new DateTime().withYear(Integer.parseInt(todayArray[0])).withMonth(Integer.parseInt(todayArray[1])).withDay(Integer.parseInt(todayArray[2]));
  }

  public static Boolean nullCheck(Boolean result, DateTime otherDT) {
    if (result != null) { return result; }
    else if (result == null && otherDT.getHour() == null) { return true; }

    return false;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof DateTime) {
      DateTime otherDT = (DateTime)other;
      boolean timeEqual = true;
      if (this.getHour() != null && otherDT.getHour() != null) { timeEqual = super.equals(other); }

      Boolean yearEq = nullCheck(Value.equals(year, otherDT.getYear()), otherDT);
      Boolean monthEq = nullCheck(Value.equals(month, otherDT.getMonth()), otherDT);
      Boolean dayEq = nullCheck(Value.equals(day, otherDT.getDay()), otherDT);

      return yearEq && (monthEq || (month == null && otherDT.getMonth() == null))
             && (dayEq || (day == null && otherDT.getDay() == null)) && timeEqual;
    }
    return false;
  }
}
