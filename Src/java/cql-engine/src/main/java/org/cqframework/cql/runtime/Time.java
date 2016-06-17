package org.cqframework.cql.runtime;

import java.math.BigDecimal;

/**
* Created by Chris Schuler on 6/16/2016
*/
public class Time {
  private Integer hour;
  private Integer minute;
  private Integer second;
  private Integer millisecond;
  private BigDecimal timezoneOffset;

  public Integer getHour() {
    return hour;
  }

  public void setHour(Integer hour) {
    this.hour = hour;
  }

  public Time withHour(Integer hour) {
    setHour(hour);
    return this;
  }

  public Integer getMinute() {
    return minute;
  }

  public void setMinute(Integer minute) {
    this.minute = minute;
  }

  public Time withMinute(Integer minute) {
    setMinute(minute);
    return this;
  }

  public Integer getSecond() {
    return second;
  }

  public void setSecond(Integer second) {
    this.second = second;
  }

  public Time withSecond(Integer second) {
    setSecond(second);
    return this;
  }

  public Integer getMillisecond() {
    return second;
  }

  public void setMillisecond(Integer millisecond) {
    this.millisecond = millisecond;
  }

  public Time withMillisecond(Integer millisecond) {
    setMillisecond(millisecond);
    return this;
  }

  public BigDecimal getTimezoneOffset() {
    return timezoneOffset;
  }

  public void setTimezoneOffset(BigDecimal timezoneOffset) {
    this.timezoneOffset = timezoneOffset;
  }

  public Time withTimezoneOffset(BigDecimal timezoneOffset) {
    setTimezoneOffset(timezoneOffset);
    return this;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Time) {
      Time otherTime = (Time)other;
      // only hour is required - others may be empty
      // However, will use in child DateTime where hour is not required... May need to revisit
      return (hour.equals(otherTime.getHour()) || hour == null && otherTime.getHour() == null)
              && (minute.equals(otherTime.getMinute()) || minute == null && otherTime.getMinute() == null)
              && (second.equals(otherTime.getSecond()) || second == null && otherTime.getSecond() == null)
              && (millisecond.equals(otherTime.getMillisecond()) || millisecond == null && otherTime.getMillisecond() == null)
              && (timezoneOffset.compareTo(otherTime.getTimezoneOffset()) == 0 || timezoneOffset == null && otherTime.getTimezoneOffset() == null);
    }
    return false;
  }
}
