package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
ends(left Interval<T>, right Interval<T>) Boolean

The ends operator returns true if the first interval ends the second.
  More precisely, if the starting point of the first interval is greater than or equal to the starting point of the second,
    and the ending point of the first interval is equal to the ending point of the second.
This operator uses the semantics described in the start and end operators to determine interval boundaries.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/7/2016
 */
public class EndsEvaluator extends org.cqframework.cql.elm.execution.Ends {

    public static Object ends(Object left, Object right) {
        Interval leftInterval = (Interval) left;
        Interval rightInterval = (Interval) right;

        if (leftInterval != null && rightInterval != null) {
            Object leftStart = leftInterval.getStart();
            Object leftEnd = leftInterval.getEnd();
            Object rightStart = rightInterval.getStart();
            Object rightEnd = rightInterval.getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null) {
                return null;
            }

            return (GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart)
                    && EqualEvaluator.equal(leftEnd, rightEnd));
        }

        return null;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), ends(left, right), left, right);
    }
}
