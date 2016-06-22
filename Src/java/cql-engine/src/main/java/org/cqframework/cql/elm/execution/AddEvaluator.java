package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;

import java.math.BigDecimal;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/13/2016
 */
public class AddEvaluator extends Add {

  private static final int YEAR_RANGE_MAX = 9999;
  // private static Partial ret;

  public static Object add(Object left, Object right) {

    if (left instanceof Integer) {
      return (Integer)left + (Integer)right;
    }
    else if (left instanceof BigDecimal) {
      return ((BigDecimal)left).add((BigDecimal)right);
    }
    else if (left instanceof Quantity) {
      return (((Quantity)left).getValue()).add(((Quantity)right).getValue());
    }

    // +(DateTime, Quantity)
    else if (left instanceof DateTime && right instanceof Quantity) {
      DateTime dt = (DateTime)left;
      String unit = ((Quantity)right).getUnit();
      int value = ((Quantity)right).getValue().intValue();

      Partial ret = new Partial();

      if (unit.equals("years") || unit.equals("year")) {
        ret = dt.getPartial().property(DateTimeFieldType.year()).addToCopy(value);
      }

      else if (unit.equals("months") || unit.equals("month")) {
        ret = dt.getPartial().property(DateTimeFieldType.monthOfYear()).addToCopy(value);
      }

      else if (unit.equals("days") || unit.equals("day")) {
        ret = dt.getPartial().property(DateTimeFieldType.dayOfMonth()).addToCopy(value);
      }

      else if (unit.equals("hours") || unit.equals("hour")) {
        ret = dt.getPartial().property(DateTimeFieldType.hourOfDay()).addToCopy(value);
      }

      else if (unit.equals("minutes") || unit.equals("minute")) {
        ret = dt.getPartial().property(DateTimeFieldType.minuteOfHour()).addToCopy(value);
      }

      else if (unit.equals("seconds") || unit.equals("second")) {
        ret = dt.getPartial().property(DateTimeFieldType.secondOfMinute()).addToCopy(value);
      }

      else if (unit.equals("milliseconds") || unit.equals("millisecond")) {
        ret = dt.getPartial().property(DateTimeFieldType.millisOfSecond()).addToCopy(value);
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
      }

      if (ret.getValue(0) > YEAR_RANGE_MAX) {
        throw new ArithmeticException("The date time addition results in a year greater than the accepted range.");
      }

      return ret;
    }

    // TODO: Finish implementation
    // +(Time, Quantity)
    else if (left instanceof Time && right instanceof Quantity) {

    }

    throw new IllegalArgumentException(String.format("Cannot AddEvaluator arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        return add(left, right);
    }
}
