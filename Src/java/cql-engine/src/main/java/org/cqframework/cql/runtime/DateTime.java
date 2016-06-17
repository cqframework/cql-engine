package org.cqframework.cql.runtime;

import java.math.BigDecimal;
import java.text.*;
import java.util.Date;


/**
* Created by Chris Schuler on 6/16/2016
*/
public class DateTime extends Time {

  private Integer year;
  private Integer month;
  private Integer day;
  // private Integer hour;
  // private Integer minute;
  // private Integer second;
  // private Integer millisecond;
  // private BigDecimal timezoneOffset;

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

  @Override
  public boolean equals(Object other) {
    if (other instanceof DateTime) {
      DateTime otherDT = (DateTime)other;
      boolean timeEqual = true;
      if (this.getHour() != null && otherDT.getHour() != null) { timeEqual = super.equals(other); }
      return year.equals(otherDT.getYear()) && (month.equals(otherDT.getMonth()) || (month == null && otherDT.getMonth() == null))
             && (day.equals(otherDT.getDay()) || (day == null && otherDT.getDay() == null)) && timeEqual;
    }
    return false;
  }
}
