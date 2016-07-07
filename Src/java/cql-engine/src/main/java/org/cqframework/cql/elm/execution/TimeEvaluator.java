package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;

import org.joda.time.Partial;

public class TimeEvaluator extends Time {

  @Override
  public Object evaluate(Context context) {
    Integer hour = (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context);
    Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
    Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
    Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);
    BigDecimal offset = (this.getTimezoneOffset() == null) ? new BigDecimal(0) : (BigDecimal)this.getTimezoneOffset().evaluate(context);

    org.cqframework.cql.runtime.Time time = new org.cqframework.cql.runtime.Time();

    if (DateTime.formatCheck(new ArrayList<Object>(Arrays.asList(hour, minute, second, millis)))) {
      int [] values = DateTime.getValues(hour, minute, second, millis);
      return time.withPartial(new Partial(time.getFields(values.length), values)).withTimezoneOffset(offset);
    }
    throw new IllegalArgumentException("Time format is invalid");
  }
}
