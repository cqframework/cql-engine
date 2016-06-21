package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;

public class TimeEvaluator extends Time {

  @Override
  public Object evaluate(Context context) {
    Integer hour = (Integer)this.getHour().evaluate(context);
    Integer minute = (Integer)this.getMinute().evaluate(context);
    Integer second = (Integer)this.getSecond().evaluate(context);
    Integer millisecond = (Integer)this.getMillisecond().evaluate(context);
    BigDecimal timezoneOffset = (BigDecimal)this.getTimezoneOffset().evaluate(context);

    if (DateTime.formatCheck(new ArrayList<Object>(Arrays.asList(hour, minute, second, millisecond, timezoneOffset)))) {
      int [] values = org.cqframework.cql.runtime.DateTime.getValues(hour, minute, second, millisecond);
      return new org.cqframework.cql.runtime.Time(values, org.cqframework.cql.runtime.Time.getFields(values.length), timezoneOffset);
    }
    throw new IllegalArgumentException("Time format is invalid");
  }
}
