package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

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

    public static Object after(Object left, Object right, String precision) {
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

        // (DateTime, DateTime)
        else if (left instanceof DateTime && right instanceof DateTime) {
            DateTime leftDT = (DateTime)left;
            DateTime rightDT = (DateTime)right;

            if (precision == null) {
                throw new IllegalArgumentException("Precision must be specified.");
            }

            int idx = DateTime.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (idx + 1 > leftDT.getPartial().size() || idx + 1 > rightDT.getPartial().size()) {

                    // Uncertainty
                    if (Uncertainty.isUncertain(leftDT, precision)) {
                        return GreaterEvaluator.greater(Uncertainty.getHighLowList(leftDT, precision).get(0), rightDT);
                    }

                    else if (Uncertainty.isUncertain(rightDT, precision)) {
                        return GreaterEvaluator.greater(leftDT, Uncertainty.getHighLowList(rightDT, precision).get(1));
                    }
                    return null;
                }
                return leftDT.getPartial().getValue(idx) > rightDT.getPartial().getValue(idx);
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
            }
        }

        // (Time, Time)
        else if (left instanceof Time && right instanceof Time) {
            Time leftT = (Time)left;
            Time rightT = (Time)right;

            if (precision == null) {
                throw new IllegalArgumentException("Precision must be specified.");
            }

            int idx = Time.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (idx + 1 > leftT.getPartial().size() || idx + 1 > rightT.getPartial().size()) {

                    // Uncertainty
                    if (Uncertainty.isUncertain(leftT, precision)) {
                        return GreaterEvaluator.greater(Uncertainty.getHighLowList(leftT, precision).get(0), rightT);
                    }

                    else if (Uncertainty.isUncertain(rightT, precision)) {
                        return GreaterEvaluator.greater(leftT, Uncertainty.getHighLowList(rightT, precision).get(1));
                    }

                    return null;
                }

                return leftT.getPartial().getValue(idx) > rightT.getPartial().getValue(idx);
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
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
