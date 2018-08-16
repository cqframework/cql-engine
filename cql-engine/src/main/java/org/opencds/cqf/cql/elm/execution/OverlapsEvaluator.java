package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps _precision_ (left Interval<T>, right Interval<T>) Boolean

The overlaps operator returns true if the first interval overlaps the second.
    More precisely, if the ending point of the first interval is greater than or equal to the
    starting point of the second interval, and the starting point of the first interval is
    less than or equal to the ending point of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class OverlapsEvaluator extends org.cqframework.cql.elm.execution.Overlaps {

    public static Boolean overlaps(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object leftEnd = ((Interval) left).getEnd();
            Object rightStart = ((Interval) right).getStart();
            Object rightEnd = ((Interval) right).getEnd();

            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                return AndEvaluator.and(
                        SameOrBeforeEvaluator.sameOrBefore(leftStart, rightEnd, precision),
                        SameOrBeforeEvaluator.sameOrBefore(rightStart, leftEnd, precision)
                );
            }

            else {
                return AndEvaluator.and(
                        LessOrEqualEvaluator.lessOrEqual(leftStart, rightEnd),
                        LessOrEqualEvaluator.lessOrEqual(rightStart, leftEnd)
                );
            }
        }

        throw new IllegalArgumentException(String.format("Cannot Overlaps arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return overlaps(left, right, precision);
    }
}
