package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.IntervalTypeSpecifier;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.Arrays;

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

The in operator for lists returns true if the given element is in the given list.
This operator uses the notion of equivalence to determine whether or not the element being searched for
    is equivalent to any element in the list. In particular this means that if the list contains a null,
    and the element being searched for is null, the result will be true.
If the left argument is null, the result is null. If the right argument is null, the result is false.

*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class InEvaluator extends org.cqframework.cql.elm.execution.In {

    public static Boolean in(Object left, Object right, String precision) {
        if (right instanceof Iterable) {
            return listIn(left, (Iterable) right);
        }

        else if (right instanceof Interval) {
            return intervalIn(left, (Interval) right, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot In arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    public static Boolean intervalIn(Object left, Interval right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();

        if (left instanceof BaseTemporal) {
            if (AnyTrueEvaluator.anyTrue(Arrays.asList(SameAsEvaluator.sameAs(left, right.getStart(), precision), SameAsEvaluator.sameAs(left, right.getEnd(), precision))))
            {
                return true;
            }
            else if (AnyTrueEvaluator.anyTrue(Arrays.asList(BeforeEvaluator.before(left, right.getStart(), precision), AfterEvaluator.after(left, right.getEnd(), precision))))
            {
                return false;
            }
            Boolean pointSameOrAfterStart;
            if (rightStart == null && right.getLowClosed()) {
                pointSameOrAfterStart = true;
            }
            else {
                pointSameOrAfterStart = SameOrAfterEvaluator.sameOrAfter(left, rightStart, precision);
            }

            Boolean pointSamedOrBeforeEnd;
            if (rightEnd == null && right.getHighClosed()) {
                pointSamedOrBeforeEnd = true;
            }
            else {
                pointSamedOrBeforeEnd = SameOrBeforeEvaluator.sameOrBefore(left, rightEnd, precision);
            }

            return AndEvaluator.and(pointSameOrAfterStart, pointSamedOrBeforeEnd);
        }

        if (AnyTrueEvaluator.anyTrue(Arrays.asList(EqualEvaluator.equal(left, right.getStart()), EqualEvaluator.equal(left, right.getEnd()))))
        {
            return true;
        }
        else if (AnyTrueEvaluator.anyTrue(Arrays.asList(LessEvaluator.less(left, right.getStart()), GreaterEvaluator.greater(left, right.getEnd()))))
        {
            return false;
        }
        Boolean greaterOrEqual;
        if (rightStart == null && right.getLowClosed()) {
            greaterOrEqual = true;
        }
        else {
            greaterOrEqual = GreaterOrEqualEvaluator.greaterOrEqual(left, rightStart);
        }

        Boolean lessOrEqual;
        if (rightEnd == null && right.getHighClosed()) {
            lessOrEqual = true;
        }
        else {
            lessOrEqual = LessOrEqualEvaluator.lessOrEqual(left, rightEnd);
        }
        return AndEvaluator.and(greaterOrEqual, lessOrEqual);
    }

    public static Boolean listIn(Object left, Iterable right) {
        if (right == null) {
            return false;
        }

        for (Object element : right) {
            if (EquivalentEvaluator.equivalent(left, element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        if (left == null && right == null) {
            return null;
        }

        // null right operand case
        if (getOperand().get(1) instanceof AsEvaluator) {
            if (((AsEvaluator) getOperand().get(1)).getAsTypeSpecifier() instanceof IntervalTypeSpecifier) {
                return intervalIn(left, (Interval) right, precision);
            }
            else {
                return listIn(left, (Iterable) right);
            }
        }

        return in(left, right, precision);
    }
}
