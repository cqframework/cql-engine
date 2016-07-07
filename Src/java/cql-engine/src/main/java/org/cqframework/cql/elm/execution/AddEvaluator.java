package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.cqframework.cql.runtime.Uncertainty;
import org.cqframework.cql.runtime.Interval;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;

/*
*** NOTES FOR DATETIME ***
The add (+) operator returns the value of the given date/time, incremented by the time-valued quantity,
  respecting variable length periods for calendar years and months.
For DateTime values, the quantity unit must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, the quantity unit must be one of: hours, minutes, seconds, or milliseconds.
The operation is performed by attempting to derive the highest granularity precision first, working down successive
  granularities to the granularity of the time-valued quantity. For example, the following addition:
    DateTime(2014) + 24 months
    This example results in the value DateTime(2016) even though the date/time value is not specified to the level
      of precision of the time-valued quantity.
    NOTE: this implementation (v3) returns a DateTime which truncates minimum element values until a non-minimum element is found
    or until the original DateTime's precision is reached.
    For Example:
      DateTime(2014) + 735 days
        returns DateTime(2016, 1, 6)
        TODO: Should the above example return an uncertainty?
        Something like: [2016, 2017]
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016 (v1)
 * Edited by Chris Schuler on 6/13/2016 (v2), 6/25/2016 (v3)
 */
public class AddEvaluator extends Add {

  private static final int YEAR_RANGE_MAX = 9999;

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

      int idx = DateTime.getFieldIndex2(unit);

      if (idx != -1) {
        int startSize = dt.getPartial().size();
        // check that the Partial has the precision specified
        if (startSize < idx + 1) {
          // expand the Partial to the proper precision
          for (int i = startSize; i < idx + 1; ++i) {
            dt.setPartial(dt.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMinimumValue()));
          }
        }

        // do the addition
        dt.setPartial(dt.getPartial().property(DateTime.getField(idx)).addToCopy(value));
        // truncate until non-minimum value is found
        for (int i = idx; i >= startSize; --i) {
          if (dt.getPartial().getValue(i) > DateTime.getField(i).getField(null).getMinimumValue()) {
            break;
          }
          dt.setPartial(dt.getPartial().without(DateTime.getField(i)));
        }
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
      }

      if (dt.getPartial().getValue(0) > YEAR_RANGE_MAX) {
        throw new ArithmeticException("The date time addition results in a year greater than the accepted range.");
      }

      return dt;
    }

    else if (left instanceof Uncertainty && right instanceof Uncertainty) {
      Interval leftInterval = ((Uncertainty)left).getUncertaintyInterval();
      Interval rightInterval = ((Uncertainty)right).getUncertaintyInterval();
      return new Uncertainty().withUncertaintyInterval(new Interval(add(leftInterval.getStart(), rightInterval.getStart()), true, add(leftInterval.getEnd(), rightInterval.getEnd()), true));
    }

    // +(Time, Quantity)
    else if (left instanceof Time && right instanceof Quantity) {
      Time time = (Time)left;
      String unit = ((Quantity)right).getUnit();
      int value = ((Quantity)right).getValue().intValue();

      int idx = Time.getFieldIndex2(unit);

      if (idx != -1) {
        int startSize = time.getPartial().size();
        // check that the Partial has the precision specified
        if (startSize < idx + 1) {
          // expand the Partial to the proper precision
          for (int i = startSize; i < idx + 1; ++i) {
            time.setPartial(time.getPartial().with(Time.getField(i), Time.getField(i).getField(null).getMinimumValue()));
          }
        }

        // do the addition
        time.setPartial(time.getPartial().property(Time.getField(idx)).addToCopy(value));
        // truncate until non-minimum value is found
        for (int i = idx; i >= startSize; --i) {
          if (time.getPartial().getValue(i) > Time.getField(i).getField(null).getMinimumValue()) {
            break;
          }
          time.setPartial(time.getPartial().without(Time.getField(i)));
        }
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
      }
      return time;
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
