package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/*
*** NOTES FOR ARITHMETIC OPERATOR ***
-(left Integer, right Integer) Integer
-(left Decimal, right Decimal) Decimal
-(left Quantity, right Quantity) Quantity

The subtract (-) operator performs numeric subtraction of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
When subtracting quantities, the dimensions of each quantity must be the same, but not necessarily the unit.
  For example, units of 'cm' and 'm' can be subtracted, but units of 'cm2' and  'cm' cannot.
    The unit of the result will be the most granular unit of either input.
If either argument is null, the result is null.

*** NOTES FOR DATETIME ***
-(left DateTime, right Quantity) DateTime
-(left Time, right Quantity) Time

The subtract (-) operator returns the value of the given date/time, decremented by the time-valued quantity,
  respecting variable length periods for calendar years and months.
For DateTime values, the quantity unit must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, the quantity unit must be one of: hours, minutes, seconds, or milliseconds.
The operation is performed by attempting to derive the highest granularity precision first, working down successive
  granularities to the granularity of the time-valued quantity. For example, the following subtraction:
    DateTime(2014) - 24 months
    This example results in the value DateTime(2012) even though the date/time value is not specified to the level of precision of the time-valued quantity.
If either argument is null, the result is null.
NOTE: see note in AddEvaluator
*/

/**
 * Created by Bryn on 5/25/2016
 */
public class SubtractEvaluator extends org.cqframework.cql.elm.execution.Subtract {

    private static final int YEAR_RANGE_MIN = 0001;

    public static Object subtract(Object left, Object right) {
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
            return new Quantity().withValue((((Quantity)left).getValue()).subtract(((Quantity)right).getValue())).withUnit(((Quantity)left).getUnit());
        }

        // -(DateTime, Quantity)
        else if (left instanceof DateTime && right instanceof Quantity) {
            DateTime dt = (DateTime)left;
            DateTime ret = new DateTime(dt.getPartial(), dt.getTimezone());
            String unit = ((Quantity)right).getUnit();
            int value = ((Quantity)right).getValue().intValue();

            int idx = DateTime.getFieldIndex(unit);

            if (idx != -1) {
                int startSize = ret.getPartial().size();
                // check that the Partial has the precision specified
                if (startSize < idx + 1) {
                    // expand the Partial to the proper precision
                    for (int i = startSize; i < idx + 1; ++i) {
                        ret.setPartial(ret.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMinimumValue()));
                    }
                }

                // do the subtraction
                ret.setPartial(ret.getPartial().property(DateTime.getField(idx)).addToCopy(-value));
                // truncate to original precision
                for (int i = idx; i >= startSize; --i) {
                    ret.setPartial(ret.getPartial().without(DateTime.getField(i)));
                }
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
            }
            if (ret.getPartial().getValue(0) < YEAR_RANGE_MIN) {
                throw new ArithmeticException("The date time addition results in a year less than the accepted range.");
            }

            return ret;
        }

        else if (left instanceof Uncertainty && right instanceof Uncertainty) {
            Interval leftInterval = ((Uncertainty)left).getUncertaintyInterval();
            Interval rightInterval = ((Uncertainty)right).getUncertaintyInterval();
            return new Uncertainty().withUncertaintyInterval(new Interval(subtract(leftInterval.getStart(), rightInterval.getStart()), true, subtract(leftInterval.getEnd(), rightInterval.getEnd()), true));
        }

        // -(Time, Quantity)
        else if (left instanceof Time && right instanceof Quantity) {
            Time t = (Time)left;
            Time ret = new Time(t.getPartial(), t.getTimezone());
            String unit = ((Quantity)right).getUnit();
            int value = ((Quantity)right).getValue().intValue();

            int idx = Time.getFieldIndex(unit);

            if (idx != -1) {
                int startSize = ret.getPartial().size();
                // check that the Partial has the precision specified
                if (startSize < idx + 1) {
                    // expand the Partial to the proper precision
                    for (int i = startSize; i < idx + 1; ++i) {
                        ret.setPartial(ret.getPartial().with(Time.getField(i), Time.getField(i).getField(null).getMinimumValue()));
                    }
                }

                // do the subtraction
                ret.setPartial(ret.getPartial().property(Time.getField(idx)).addToCopy(-value));
                // truncate to original precision
                for (int i = idx; i >= startSize; --i) {
                    ret.setPartial(ret.getPartial().without(Time.getField(i)));
                }
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration unit: %s", unit));
            }

            return ret;
        }

        throw new IllegalArgumentException(String.format("Cannot Subtract arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), subtract(left, right), left, right);
    }
}
