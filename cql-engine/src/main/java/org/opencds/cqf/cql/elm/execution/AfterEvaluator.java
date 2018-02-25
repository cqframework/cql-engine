package org.opencds.cqf.cql.elm.execution;

import org.joda.time.Instant;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.util.List;

/*
*** NOTES FOR INTERVAL ***
after(left Interval<T>, right Interval<T>) Boolean
after(left T, right Interval<T>) Boolean
after(left Interval<T>, right T) Boolean

The after operator for intervals returns true if the first interval starts after the second one ends.
  In other words, if the starting point of the first interval is greater than the ending point of the second interval.
For the point-interval overload, the operator returns true if the given point is greater than the end of the interval.
For the interval-point overload, the operator returns true if the given interval starts after the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
after precision of(left DateTime, right DateTime) Boolean
after precision of(left Time, right Time) Boolean

The after-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the after the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/7/2016
 */
public class AfterEvaluator extends org.cqframework.cql.elm.execution.After {

    public static Boolean after(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // (Interval, Interval)
        if (left instanceof Interval && right instanceof Interval) {
            return GreaterEvaluator.greater(((Interval)left).getStart(), ((Interval)right).getEnd());
        }

        // (Interval, Point)
        else if (left instanceof Interval) {
            return GreaterEvaluator.greater(((Interval)left).getStart(), right);
        }

        // (Point, Interval)
        else if (right instanceof Interval) {
            return GreaterEvaluator.greater(left, ((Interval)right).getEnd());
        }

        else if (left instanceof BaseTemporal && right instanceof BaseTemporal) {
            BaseTemporal leftTemporal = (BaseTemporal) left;
            BaseTemporal rightTemporal = (BaseTemporal) right;

            if (precision == null) {
                precision = "millisecond";
            }

            int idx = leftTemporal.getIsDateTime() ? DateTime.getFieldIndex(precision) : Time.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (Uncertainty.isUncertain(leftTemporal, precision) || Uncertainty.isUncertain(rightTemporal, precision)) {

                    // get the precision of the uncertain DateTime/Time value
                    if (Uncertainty.isUncertain(leftTemporal, precision)) {
                        idx = leftTemporal.getPartial().size() - 1;
                    } else if (Uncertainty.isUncertain(rightTemporal, precision)) {
                        idx = rightTemporal.getPartial().size() - 1;
                    }

                    // if not equal do After to that precision
                    if (leftTemporal instanceof DateTime && !SameAsEvaluator.sameAs(leftTemporal, rightTemporal, DateTime.getUnit(idx))) {
                         return after(leftTemporal, rightTemporal, DateTime.getUnit(idx));
                    }

                    else if (leftTemporal instanceof Time && !SameAsEvaluator.sameAs(leftTemporal, rightTemporal, Time.getUnit(idx))) {
                        return after(leftTemporal, rightTemporal, Time.getUnit(idx));
                    }

                    // else null
                    return null;
                }

                if (leftTemporal.getTimezone().getID().equals(rightTemporal.getTimezone().getID())) {
                    for (int i = 0; i < idx; i++) {
                        if (leftTemporal.getPartial().getValue(i) < rightTemporal.getPartial().getValue(i)) {
                            return false;
                        }
                        else if (leftTemporal.getPartial().getValue(i) > rightTemporal.getPartial().getValue(i)) {
                            return true;
                        }
                    }
                    return leftTemporal.getPartial().getValue(idx) > rightTemporal.getPartial().getValue(idx);
                }

                else {
                    Instant leftInstant = leftTemporal.getJodaDateTime().toInstant();
                    Instant rightInstant = rightTemporal.getJodaDateTime().toInstant();
                    for (int i = 0; i < idx; i++) {
                        if (leftInstant.get(DateTime.getField(i)) < rightInstant.get(DateTime.getField(i))) {
                            return false;
                        }
                        else if (leftInstant.get(DateTime.getField(i)) > rightInstant.get(DateTime.getField(i))) {
                            return true;
                        }
                    }
                    return leftInstant.get(DateTime.getField(idx)) > rightInstant.get(DateTime.getField(idx));
                }
            }
        }

        throw new IllegalArgumentException(String.format("Cannot After arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), after(left, right, precision), left, right);
    }
}
