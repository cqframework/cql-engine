package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.List;

/*
*** NOTES FOR INTERVAL ***
properly includes(left Interval<T>, right Interval<T>) Boolean

The properly includes operator for intervals returns true if the first interval completely includes the second and the
  first interval is strictly larger than the second.
  More precisely, if the starting point of the first interval is less than or equal to the starting point of the second interval,
    and the ending point of the first interval is greater than or equal to the ending point of the second interval,
      and they are not the same interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
properly includes(left List<T>, right List<T>) Boolean

The properly includes operator for lists returns true if the first list contains every element of the second list,
  and the first list is strictly larger than the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class ProperlyIncludesEvaluator extends org.cqframework.cql.elm.execution.ProperIncludes {

    public static Object properlyIncludes(Object left, Object right) {
        if (left == null) {
            return false;
        }

        if (right == null) {
            return true;
        }

        if (left instanceof Interval) {
            Interval leftInterval = (Interval)left;
            Interval rightInterval = (Interval)right;

            Object leftStart = leftInterval.getStart();
            Object leftEnd = leftInterval.getEnd();
            Object rightStart = rightInterval.getStart();
            Object rightEnd = rightInterval.getEnd();

            return (GreaterEvaluator.greater(Interval.getSize(leftStart, leftEnd), Interval.getSize(rightStart, rightEnd))
                    && LessOrEqualEvaluator.lessOrEqual(leftStart, rightStart)
                    && GreaterOrEqualEvaluator.greaterOrEqual(leftEnd, rightEnd)
            );
        }

        else if (left instanceof Iterable) {
            List leftArr = (List) left;
            List rightArr = (List) right;

            Object includes = IncludesEvaluator.includes(leftArr, rightArr);

            if (includes == null) {
                return null;
            }

            return (Boolean)includes && leftArr.size() > rightArr.size();
        }

        throw new IllegalArgumentException(String.format("Cannot perform ProperlyIncludes operation with arguments of type: %s and %s", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), properlyIncludes(left, right), left, right);
    }
}
