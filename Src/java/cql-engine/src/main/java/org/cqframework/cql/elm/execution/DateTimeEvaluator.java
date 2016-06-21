package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class DateTimeEvaluator extends DateTime {

  @Override
  public Object evaluate(Context context) {
    Integer year = (Integer)this.getYear().evaluate(context);
    if (year == null) { return null; }
    else if (((year - 1000) < 0)) {
      throw new IllegalArgumentException("Must use 4 digits for year.");
    }

    Integer month = (this.getMonth() == null) ? null : (Integer)this.getMonth().evaluate(context);
    Integer day = (this.getDay() == null) ? null : (Integer)this.getDay().evaluate(context);
    Integer hour = (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context);
    Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
    Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
    Integer millisecond = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);
    BigDecimal timezoneOffset = (this.getTimezoneOffset() == null) ? null :  (BigDecimal)this.getTimezoneOffset().evaluate(context);

    if (org.cqframework.cql.runtime.DateTime.formatCheck(new ArrayList<Object>(Arrays.asList(year, month, day, hour, minute, second, millisecond, timezoneOffset)))) {
      int [] values = org.cqframework.cql.runtime.DateTime.getValues(year, month, day, hour, minute, second, millisecond);
      return new org.cqframework.cql.runtime.DateTime(values, org.cqframework.cql.runtime.DateTime.getFields(values.length), timezoneOffset);
    }
    else {
      throw new IllegalArgumentException("DateTime format is invalid");
    }
  }
}
