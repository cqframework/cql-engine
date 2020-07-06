package org.opencds.cqf.cql.engine.elm.execution;

import java.util.Arrays;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.BaseTemporal;
import org.opencds.cqf.cql.engine.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
in(point T, argument Interval<T>) Boolean

The in operator for intervals returns true if the given point is greater than or equal to the
    starting point of the interval, and less than or equal to the ending point of the interval.
    For open interval boundaries, exclusive comparison operators are used.
    For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If precision is specified and the point type is a date/time type, comparisons used in the
    operation are performed at the specified precision.
If either argument is null, the result is null.

*/

/*
*** NOTES FOR LIST ***
in(element T, argument List<T>) Boolean

The in operator for lists returns true if the given element is in the given list using equality semantics.

If either argument is null, the result is null.

*/

public class InEvaluator extends org.cqframework.cql.elm.execution.In
{
    public static Boolean in(Object left, Object right, String precision)
    {
        if (left == null )
        {
            return null;
        }

        if(left != null && right == null)
        {
            return false;
        }

        if (right instanceof Iterable)
        {
            return listIn(left, (Iterable<?>) right);
        }

        else if (right instanceof Interval)
        {
            return intervalIn(left, (Interval) right, precision);
        }

        throw new InvalidOperatorArgument(
                "In(T, Interval<T>) or In(T, List<T>)",
                String.format("In(%s, %s)", left.getClass().getName(), right.getClass().getName())
        );
    }

    private static Boolean intervalIn(Object left, Interval right, String precision)
    {
        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();

        if (left instanceof BaseTemporal)
        {
            if (AnyTrueEvaluator.anyTrue(Arrays.asList(SameAsEvaluator.sameAs(left, right.getStart(), precision), SameAsEvaluator.sameAs(left, right.getEnd(), precision))))
            {
                return true;
            }
            else if (AnyTrueEvaluator.anyTrue(Arrays.asList(BeforeEvaluator.before(left, right.getStart(), precision), AfterEvaluator.after(left, right.getEnd(), precision))))
            {
                return false;
            }

            Boolean pointSameOrAfterStart;
            if (rightStart == null && right.getLowClosed())
            {
                pointSameOrAfterStart = true;
            }
            else
            {
                pointSameOrAfterStart = SameOrAfterEvaluator.sameOrAfter(left, rightStart, precision);
            }

            Boolean pointSameOrBeforeEnd;
            if (rightEnd == null && right.getHighClosed())
            {
                pointSameOrBeforeEnd = true;
            }
            else
            {
                pointSameOrBeforeEnd = SameOrBeforeEvaluator.sameOrBefore(left, rightEnd, precision);
            }

            return AndEvaluator.and(pointSameOrAfterStart, pointSameOrBeforeEnd);
        }

        else if (AnyTrueEvaluator.anyTrue(Arrays.asList(EqualEvaluator.equal(left, right.getStart()), EqualEvaluator.equal(left, right.getEnd()))))
        {
            return true;
        }
        else if (AnyTrueEvaluator.anyTrue(Arrays.asList(LessEvaluator.less(left, right.getStart()), GreaterEvaluator.greater(left, right.getEnd()))))
        {
            return false;
        }

        Boolean greaterOrEqual;
        if (rightStart == null && right.getLowClosed())
        {
            greaterOrEqual = true;
        }
        else
        {
            greaterOrEqual = GreaterOrEqualEvaluator.greaterOrEqual(left, rightStart);
        }

        Boolean lessOrEqual;
        if (rightEnd == null && right.getHighClosed())
        {
            lessOrEqual = true;
        }
        else
        {
            lessOrEqual = LessOrEqualEvaluator.lessOrEqual(left, rightEnd);
        }

        return AndEvaluator.and(greaterOrEqual, lessOrEqual);
    }

    private static Boolean listIn(Object left, Iterable<?> right)
    {
        Boolean isEqual;
        for (Object element : right)
        {
            isEqual = EqualEvaluator.equal(left, element);
            if ((isEqual != null && isEqual))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    protected Object internalEvaluate(Context context)
    {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        // null right operand case
//        if (getOperand().get(1) instanceof AsEvaluator) {
//            if (((AsEvaluator) getOperand().get(1)).getAsTypeSpecifier() instanceof IntervalTypeSpecifier) {
//                return intervalIn(left, (Interval) right, precision);
//            }
//            else {
//                return listIn(left, (Iterable) right);
//            }
//        }

        return in(left, right, precision);
    }
}
