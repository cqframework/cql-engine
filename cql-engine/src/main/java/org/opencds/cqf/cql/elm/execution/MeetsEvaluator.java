package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
meets _precision_ (left Interval<T>, right Interval<T>) Boolean

The meets operator returns true if the first interval ends immediately before the second interval starts,
    or if the first interval starts immediately after the second interval ends.
    In other words, if the ending point of the first interval is equal to the predecessor of the starting point of the second,
    or if the starting point of the first interval is equal to the successor of the ending point of the second.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class MeetsEvaluator extends org.cqframework.cql.elm.execution.Meets {

    public static Boolean meets(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            return OrEvaluator.or(
                        MeetsBeforeEvaluator.meetsBefore(left, right, precision),
                        MeetsAfterEvaluator.meetsAfter(left, right, precision)
            );
        }

        throw new IllegalArgumentException(String.format("Cannot Meets arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return meets(left, right, precision);
    }
}
