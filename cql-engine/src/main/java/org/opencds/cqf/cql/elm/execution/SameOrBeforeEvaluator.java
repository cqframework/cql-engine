package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;

/*
same precision or before(left DateTime, right DateTime) Boolean
same precision or before(left DateTime, right DateTime) Boolean

The same-precision-or before operator compares two date/time values to the specified precision to determine
  whether the first argument is the same or before the second argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.

on or before precision (left Interval<T>, right Interval<T>) Boolean
on or before precision (left T, right Interval<T>) Boolean
on or before precision (left interval<T>, right T) Boolean

The on or before operator for intervals returns true if the first interval ends on or before the second one starts.
    In other words, if the ending point of the first interval is less than or equal to the starting point of the second interval.
For the point-interval overload, the operator returns true if the given point is less than or equal to the start of the interval.
For the interval-point overload, the operator returns true if the given interval ends on or before the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type,
    comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
Note that this operator can be invoked using either the on or before or the before or on syntax.
*/

/**
 * Created by Chris Schuler on 6/23/2016
 */
public class SameOrBeforeEvaluator extends org.cqframework.cql.elm.execution.SameOrBefore {

    public static Object onOrBefore(Object left, Object right, String precision) {
        // Interval, Interval
        if (left instanceof Interval && right instanceof Interval) {
            if (((Interval) left).getStart() instanceof DateTime
                    || ((Interval) left).getStart() instanceof Time)
            {
                return sameOrBefore(((Interval) left).getEnd(), ((Interval) right).getStart(), precision);
            }
            return LessOrEqualEvaluator.lessOrEqual(((Interval) left).getEnd(), ((Interval) right).getStart());
        }

        // Interval, Point
        else if (left instanceof Interval) {
            if (right instanceof DateTime || right instanceof Time) {
                return sameOrBefore(((Interval) left).getEnd(), right, precision);
            }
            return LessOrEqualEvaluator.lessOrEqual(((Interval) left).getEnd(), right);
        }

        // Point, Interval
        else if (right instanceof Interval) {
            if (left instanceof DateTime || left instanceof Time) {
                return sameOrBefore(left, ((Interval) right).getStart(), precision);
            }
            return LessOrEqualEvaluator.lessOrEqual(left, ((Interval) right).getStart());
        }

        throw new IllegalArgumentException(String.format("Cannot perform OnOrBefore operator with arguments %s and %s", left.getClass().getName(), right.getClass().getName()));
    }

    public static Object sameOrBefore(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // Interval OnOrBefore overload
        if (left instanceof Interval || right instanceof Interval) {
            return onOrBefore(left, right, precision);
        }

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        if (left instanceof DateTime && right instanceof DateTime) {
            DateTime leftDT = (DateTime)left;
            DateTime rightDT = (DateTime)right;

            int idx = DateTime.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (idx + 1 > leftDT.getPartial().size() || idx + 1 > rightDT.getPartial().size()) {
                    return null;
                }

                for (int i = 0; i < idx + 1; ++i) {
                    if (leftDT.getJodaDateTime().toInstant().get(DateTime.getField(i))
                            > rightDT.getJodaDateTime().toInstant().get(DateTime.getField(i)))
                    {
                        return false;
                    }
                }

                return leftDT.getJodaDateTime().toInstant().get(DateTime.getField(idx))
                        <= rightDT.getJodaDateTime().toInstant().get(DateTime.getField(idx));
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
            }
        }

        if (left instanceof Time && right instanceof Time) {
            Time leftT = (Time)left;
            Time rightT = (Time)right;

            int idx = Time.getFieldIndex(precision);

            if (idx != -1) {
                // check level of precision
                if (idx + 1 > leftT.getPartial().size() || idx + 1 > rightT.getPartial().size()) {
                    return null;
                }

                for (int i = 0; i < idx + 1; ++i) {
                    if (leftT.getPartial().getValue(i) > rightT.getPartial().getValue(i)) {
                        return false;
                    }
                    else if (leftT.getPartial().getValue(i) < rightT.getPartial().getValue(i)) {
                        return true;
                    }
                }

                return leftT.getPartial().getValue(idx) <= rightT.getPartial().getValue(idx);
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
            }
        }
        throw new IllegalArgumentException(String.format("Cannot SameOrBefore arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), sameOrBefore(left, right, precision), left, right, precision);
    }
}
