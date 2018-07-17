package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

/*
*** NOTES FOR INTERVAL ***
intersect(left Interval<T>, right Interval<T>) Interval<T>

The intersect operator for intervals returns the intersection of two intervals.
  More precisely, the operator returns the interval that defines the overlapping portion of both arguments.
If the arguments do not overlap, this operator returns null.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
intersect(left List<T>, right List<T>) List<T>

The intersect operator for lists returns the intersection of two lists.
  More precisely, the operator returns a list containing only the elements that appear in both lists.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IntersectEvaluator extends org.cqframework.cql.elm.execution.Intersect {

    public static Object intersect(Object left, Object right) {

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval) {
            Interval leftInterval = (Interval)left;
            Interval rightInterval = (Interval)right;

            Object leftStart = leftInterval.getStart();
            Object leftEnd = leftInterval.getEnd();
            Object rightStart = rightInterval.getStart();
            Object rightEnd = rightInterval.getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null)
            {
                return null;
            }

            String precision = null;
            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                precision = BaseTemporal.getHighestPrecision((BaseTemporal) leftStart, (BaseTemporal) leftEnd, (BaseTemporal) rightStart, (BaseTemporal) rightEnd);
            }

            Boolean overlaps = OverlapsEvaluator.overlaps(leftInterval, rightInterval, precision);
            if (overlaps == null || !overlaps) {
                return null;
            }

            Object max = GreaterEvaluator.greater(leftStart, rightStart) ? leftStart : rightStart;
            Object min = LessEvaluator.less(leftEnd, rightEnd) ? leftEnd : rightEnd;

            return new Interval(max, true, min, true);
        }

        else if (left instanceof Iterable) {
            Iterable leftArr = (Iterable)left;
            Iterable rightArr = (Iterable)right;

            List<Object> result = new ArrayList<>();
            for (Object leftItem : leftArr) {
                if (InEvaluator.in(leftItem, rightArr, null)) {
                    result.add(leftItem);
                }
            }
            return result;
        }

        throw new IllegalArgumentException(String.format("Cannot Intersect arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), intersect(left, right), left, right);
    }
}
