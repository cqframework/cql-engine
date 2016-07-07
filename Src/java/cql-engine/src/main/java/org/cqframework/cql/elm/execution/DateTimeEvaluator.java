package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;

import org.joda.time.Partial;

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class DateTimeEvaluator extends DateTime {

  @Override
  public Object evaluate(Context context) {
    Integer year = (Integer)this.getYear().evaluate(context);
    if (year == null) { return null; }

    else if (year.toString().length() < 4) {
      throw new IllegalArgumentException("Must use 4 digits for year.");
    }

    Integer month = (this.getMonth() == null) ? null : (Integer)this.getMonth().evaluate(context);
    Integer day = (this.getDay() == null) ? null : (Integer)this.getDay().evaluate(context);
    Integer hour = (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context);
    Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
    Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
    Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);
    BigDecimal offset = (this.getTimezoneOffset() == null) ? new BigDecimal(0) : (BigDecimal)this.getTimezoneOffset().evaluate(context);

    org.cqframework.cql.runtime.DateTime dt = new org.cqframework.cql.runtime.DateTime();

    if (dt.formatCheck(new ArrayList<Object>(Arrays.asList(year, month, day, hour, minute, second, millis)))) {
      int [] values = dt.getValues(year, month, day, hour, minute, second, millis);
      return dt.withPartial(new Partial(dt.getFields(values.length), values)).withTimezoneOffset(offset);
    }
    else {
      throw new IllegalArgumentException("DateTime format is invalid");
    }
  }
}
