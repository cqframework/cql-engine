package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*
meets before _precision_ (left Interval<T>, right Interval<T>) Boolean

The meets before operator returns true if the first interval ends immediately before the second interval starts.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class MeetsBeforeEvaluator extends org.cqframework.cql.elm.execution.MeetsBefore {

    public static Boolean meetsBefore(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            Boolean isLeftStartGreater = GreaterEvaluator.greater(((Interval) left).getStart(), ((Interval) right).getEnd());
            if (isLeftStartGreater != null && isLeftStartGreater) {
                return false;
            }

            Object leftEnd = ((Interval) left).getEnd();
            Object rightStart = ((Interval) right).getStart();

            Boolean isIn = InEvaluator.in(leftEnd, right, precision);
            if (isIn != null && isIn) {
                return false;
            }
            isIn = InEvaluator.in(((Interval) left).getStart(), right, precision);
            if (isIn != null && isIn) {
                return false;
            }
            isIn = InEvaluator.in(leftEnd, right, precision);
            if (isIn != null && isIn) {
                return false;
            }

            return MeetsEvaluator.meetsOperation(leftEnd, rightStart, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot MeetsBefore arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return meetsBefore(left, right, precision);
    }
}
