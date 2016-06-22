package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/14/2016
 */
public class SubtractEvaluator extends Subtract {

  private static final int YEAR_RANGE_MIN = 0001;

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        // -(Integer, Integer)
        if (left instanceof Integer) {
            return (Integer)left - (Integer)right;
        }

        // -(Decimal, Decimal)
        else if (left instanceof BigDecimal) {
            return ((BigDecimal)left).subtract((BigDecimal)right);
        }

        // -(Quantity, Quantity)
        else if (left instanceof Quantity) {
          return (((Quantity)left).getValue()).subtract(((Quantity)right).getValue());
        }

        // -(DateTime, Quantity)
        else if (left instanceof DateTime && right instanceof Quantity) {
          DateTime dt = (DateTime)left;
          String unit = ((Quantity)right).getUnit();
          int value = ((Quantity)right).getValue().intValue();

          Partial ret = new Partial();

          if (unit.equals("years") || unit.equals("year")) {
            ret = dt.getPartial().property(DateTimeFieldType.year()).addToCopy(-value);
          }

          else if (unit.equals("months") || unit.equals("month")) {
            ret = dt.getPartial().property(DateTimeFieldType.monthOfYear()).addToCopy(-value);
          }

          else if (unit.equals("days") || unit.equals("day")) {
            ret = dt.getPartial().property(DateTimeFieldType.dayOfMonth()).addToCopy(-value);
          }

          else if (unit.equals("hours") || unit.equals("hour")) {
            ret = dt.getPartial().property(DateTimeFieldType.hourOfDay()).addToCopy(-value);
          }

          else if (unit.equals("minutes") || unit.equals("minute")) {
            ret = dt.getPartial().property(DateTimeFieldType.minuteOfHour()).addToCopy(-value);
          }

          else if (unit.equals("seconds") || unit.equals("second")) {
            ret = dt.getPartial().property(DateTimeFieldType.secondOfMinute()).addToCopy(-value);
          }

          else if (unit.equals("milliseconds") || unit.equals("millisecond")) {
            ret = dt.getPartial().property(DateTimeFieldType.millisOfSecond()).addToCopy(-value);
          }

          else {
            throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
          }
          if (ret.getValue(0) < YEAR_RANGE_MIN) {
            throw new ArithmeticException("The date time addition results in a year less than the accepted range.");
          }

          return ret;
        }

        // TODO: Finish implementation of Subtract
        // -(Time, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
