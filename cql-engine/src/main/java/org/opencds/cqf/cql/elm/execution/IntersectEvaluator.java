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
This operator uses equality semantics to determine whether or not two elements are the same.
The operator is defined with set semantics, meaning that each element will appear in the result at most once,
    and that there is no expectation that the order of the inputs will be preserved in the results.
If either argument is null, the result is null.
*/

public class IntersectEvaluator extends org.cqframework.cql.elm.execution.Intersect
{
    public static Object intersect(Object left, Object right)
    {
        if (left == null || right == null)
        {
            return null;
        }

        if (left instanceof Interval)
        {
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
            if (leftStart instanceof BaseTemporal
                    && rightStart instanceof BaseTemporal)
            {
                precision = BaseTemporal.getHighestPrecision((BaseTemporal) leftStart, (BaseTemporal) leftEnd, (BaseTemporal) rightStart, (BaseTemporal) rightEnd);
            }

            Boolean overlaps = OverlapsEvaluator.overlaps(leftInterval, rightInterval, precision);
            if (overlaps == null || !overlaps)
            {
                return null;
            }

            Object max = GreaterEvaluator.greater(leftStart, rightStart) ? leftStart : rightStart;
            Object min = LessEvaluator.less(leftEnd, rightEnd) ? leftEnd : rightEnd;

            return new Interval(max, true, min, true);
        }

        else if (left instanceof Iterable)
        {
            Iterable leftArr = (Iterable)left;
            Iterable rightArr = (Iterable)right;

            List<Object> result = new ArrayList<>();
            Boolean in;
            for (Object leftItem : leftArr)
            {
                in = InEvaluator.in(leftItem, rightArr, null);
                if (in != null && in)
                {
                    result.add(leftItem);
                }
            }

            return DistinctEvaluator.distinct(result);
        }

        throw new IllegalArgumentException(String.format("Cannot Intersect arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context)
    {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return intersect(left, right);
    }
}
