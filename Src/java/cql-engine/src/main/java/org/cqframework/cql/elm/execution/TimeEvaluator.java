package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

public class TimeEvaluator extends Time {

  @Override
  public Object evaluate(Context context) {
    Integer hour = (Integer)this.getHour().evaluate(context);
    Integer minute = (Integer)this.getMinute().evaluate(context);
    Integer second = (Integer)this.getSecond().evaluate(context);
    Integer millisecond = (Integer)this.getMillisecond().evaluate(context);
    BigDecimal timezoneOffset = (BigDecimal)this.getTimezoneOffset().evaluate(context);
    return new org.cqframework.cql.runtime.Time().withHour(hour).withMinute(minute).withSecond(second).withMillisecond(millisecond).withTimezoneOffset(timezoneOffset);
  }
}
