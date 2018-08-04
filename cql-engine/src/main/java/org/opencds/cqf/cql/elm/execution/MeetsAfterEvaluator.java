package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*
meets after _precision_ (left Interval<T>, right Interval<T>) Boolean

The meets after operator returns true if the first interval starts immediately after the second interval ends.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class MeetsAfterEvaluator extends org.cqframework.cql.elm.execution.MeetsAfter {

    public static Boolean meetsAfter(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            Boolean isRightStartGreater = GreaterEvaluator.greater(((Interval) right).getStart(), ((Interval) left).getEnd());
            if (isRightStartGreater != null && isRightStartGreater) {
                return false;
            }

            Object leftStart = ((Interval) left).getStart();
            Object rightEnd = ((Interval) right).getEnd();

            Boolean isIn = InEvaluator.in(((Interval) left).getEnd(), right, precision);
            if (isIn != null && isIn) {
                return false;
            }
            isIn = InEvaluator.in(leftStart, right, precision);
            if (isIn != null && isIn) {
                return false;
            }
            isIn = InEvaluator.in(rightEnd, left, precision);
            if (isIn != null && isIn) {
                return false;
            }

            return MeetsEvaluator.meetsOperation(rightEnd, leftStart, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot MeetsAfter arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return meetsAfter(left, right, precision);
    }
}
