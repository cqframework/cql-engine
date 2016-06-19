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

  public static Boolean nullCheck(Boolean result, Time otherTime) {
    if (result != null) { return result; }
    else if (result == null && otherTime.getHour() == null) { return true; }
    return false;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Time) {
      Time otherTime = (Time)other;

      Boolean hourEq = nullCheck(Value.equals(hour, otherTime.getHour()), otherTime);
      Boolean minEq = nullCheck(Value.equals(minute, otherTime.getMinute()), otherTime);
      Boolean secEq = nullCheck(Value.equals(second, otherTime.getSecond()), otherTime);
      Boolean msEq = nullCheck(Value.equals(millisecond, otherTime.getMillisecond()), otherTime);
      Boolean tzEq = nullCheck(Value.equals(timezoneOffset, otherTime.getTimezoneOffset()), otherTime);

      return (hourEq || hour == null && otherTime.getHour() == null) && (minEq || minute == null && otherTime.getMinute() == null)
              && (secEq || second == null && otherTime.getSecond() == null)
              && (msEq || millisecond == null && otherTime.getMillisecond() == null)
              && (tzEq || timezoneOffset == null && otherTime.getTimezoneOffset() == null);
    }
    return false;
  }
}
