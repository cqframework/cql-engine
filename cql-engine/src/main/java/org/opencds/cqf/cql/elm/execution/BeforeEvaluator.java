package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.util.List;

/*
*** NOTES FOR INTERVAL ***
before(left Interval<T>, right Interval<T>) Boolean
before(left T, right Interval<T>) Boolean
before(left interval<T>, right T) Boolean

The before operator for intervals returns true if the first interval ends before the second one starts.
  In other words, if the ending point of the first interval is less than the starting point of the second interval.
For the point-interval overload, the operator returns true if the given point is less than the start of the interval.
For the interval-point overload, the operator returns true if the given interval ends before the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
before precision of(left DateTime, right DateTime) Boolean
before precision of(left Time, right Time) Boolean

The before-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the before the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/7/2016
 */
public class BeforeEvaluator extends org.cqframework.cql.elm.execution.Before {

    public static Boolean before(Object left, Object right, String precision) {

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            return LessEvaluator.less(((Interval)left).getStart(), ((Interval)right).getEnd());
        }

        else if (left instanceof Interval) {
            return LessEvaluator.less(((Interval)left).getEnd(), right);
        }

        else if (right instanceof Interval) {
            return LessEvaluator.less(left, ((Interval)right).getStart());
        }

        else if (left instanceof BaseTemporal && right instanceof BaseTemporal) {
            BaseTemporal leftTemporal = (BaseTemporal) left;
            BaseTemporal rightTemporal = (BaseTemporal) right;

            if (precision == null) {
                precision = "millisecond";
            }

            int idx = DateTime.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (Uncertainty.isUncertain(leftTemporal, precision) || Uncertainty.isUncertain(rightTemporal, precision)) {

                    if (Uncertainty.isUncertain(leftTemporal, precision)) {
                        return LessEvaluator.less(((List) Uncertainty.getHighLowList(leftTemporal, precision)).get(0), rightTemporal);
                    }

                    else if (Uncertainty.isUncertain(rightTemporal, precision)) {
                        return LessEvaluator.less(leftTemporal, ((List)Uncertainty.getHighLowList(rightTemporal, precision)).get(1));
                    }

                    return null;
                }

                if (leftTemporal.getTimezone().getID().equals(rightTemporal.getTimezone().getID())) {
                    if (leftTemporal instanceof Time) {
                        idx -= 3;
                    }
                    return leftTemporal.getPartial().getValue(idx) < rightTemporal.getPartial().getValue(idx);
                }

                else {
                    return leftTemporal.getJodaDateTime().toInstant().get(DateTime.getField(idx))
                            < rightTemporal.getJodaDateTime().toInstant().get(DateTime.getField(idx));
                }
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
            }
        }

        throw new IllegalArgumentException(String.format("Cannot Before arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), before(left, right, precision), left, right);
    }
}
