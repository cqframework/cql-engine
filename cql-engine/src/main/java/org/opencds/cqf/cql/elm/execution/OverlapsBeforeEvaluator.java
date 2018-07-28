package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps before _precision_ (left Interval<T>, right Interval<T>) Boolean

The operator overlaps before returns true if the first interval overlaps the second and starts before it.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class OverlapsBeforeEvaluator extends org.cqframework.cql.elm.execution.OverlapsBefore {

    public static Object overlapsBefore(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object rightStart = ((Interval) right).getStart();

            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                return AndEvaluator.and(
                        BeforeEvaluator.before(leftStart, rightStart, precision),
                        OverlapsEvaluator.overlaps(left, right, precision)
                );
            }

            else {
                return AndEvaluator.and(
                        LessEvaluator.less(leftStart, rightStart),
                        OverlapsEvaluator.overlaps(left, right, precision)
                );
            }
        }

        throw new IllegalArgumentException(String.format("Cannot OverlapsBefore arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return overlapsBefore(left, right, precision);
    }
}
