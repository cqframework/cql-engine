package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
contains(argument List<T>, element T) Boolean

The contains operator for intervals returns true if the given point is greater than or equal to the starting point
  of the interval, and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016
 */
public class ContainsEvaluator extends org.cqframework.cql.elm.execution.Contains {

    public static Object contains(Object left, Object right) {
        if (left == null) {
            return null;
        }

        if (left instanceof Interval) {
            Interval leftInterval = (Interval)left;

            if (right != null) {
                Object leftStart = leftInterval.getStart();
                Object leftEnd = leftInterval.getEnd();

                Boolean greaterOrEqual = GreaterOrEqualEvaluator.greaterOrEqual(right, leftStart);
                Boolean lessOrEqual = LessOrEqualEvaluator.lessOrEqual(right, leftEnd);
                if (greaterOrEqual == null || lessOrEqual == null) {
                    return null;
                }

                return greaterOrEqual && lessOrEqual;
            }
            return null;
        }

        else if (left instanceof Iterable) {
            Iterable<Object> list = (Iterable<Object>)left;

            return InEvaluator.in(right, list, null);
        }

        throw new IllegalArgumentException(String.format("Cannot Contains arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), contains(left, right));
    }
}
