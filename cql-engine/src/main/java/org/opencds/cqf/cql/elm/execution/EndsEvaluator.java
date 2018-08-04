package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
ends _precision_ (left Interval<T>, right Interval<T>) Boolean

The ends operator returns true if the first interval ends the second.
    More precisely, if the starting point of the first interval is greater than or equal to the starting point of the second,
    and the ending point of the first interval is equal to the ending point of the second.
This operator uses the semantics described in the start and end operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/7/2016
 */
public class EndsEvaluator extends org.cqframework.cql.elm.execution.Ends {

    public static Boolean ends(Object left, Object right, String precision) {
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
                        SameOrAfterEvaluator.sameOrAfter(leftStart, rightStart, precision),
                        SameAsEvaluator.sameAs(leftEnd, rightEnd, precision)
                );
            }

            else {
                return AndEvaluator.and(
                        GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart),
                        EqualEvaluator.equal(leftEnd, rightEnd)
                );
            }
        }

        throw new IllegalArgumentException(String.format("Cannot Ends arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), ends(left, right, precision), left, right, precision);
    }
}
