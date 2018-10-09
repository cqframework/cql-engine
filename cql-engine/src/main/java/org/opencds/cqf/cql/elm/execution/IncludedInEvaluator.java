package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.Arrays;

/*
*** NOTES FOR INTERVAL ***
included in _precision_ (left Interval<T>, right Interval<T>) Boolean

The included in operator for intervals returns true if the first interval is completely included in the second.
    More precisely, if the starting point of the first interval is greater than or equal to the starting point
    of the second interval, and the ending point of the first interval is less than or equal to the ending point
    of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
Note that during is a synonym for included in and may be used to invoke the same operation whever included in may appear.

*** NOTES FOR LIST ***
included in(left List<T>, right list<T>) Boolean

he included in operator for lists returns true if every element of the first list is in the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If the left argument is null, the result is true, else if the right argument is null, the result is false.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IncludedInEvaluator extends org.cqframework.cql.elm.execution.IncludedIn {

    public static Boolean includedIn(Object left, Object right, String precision) {
        if (left instanceof Interval && right instanceof Interval) {
            return intervalIncludedIn((Interval) left, (Interval) right, precision);
        }
        if (left instanceof Iterable && right instanceof Iterable) {
            return listIncludedIn((Iterable) left, (Iterable) right);
        }

        throw new IllegalArgumentException(String.format("Cannot IncludedIn arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    public static Boolean intervalIncludedIn(Interval left, Interval right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        Object leftStart = left.getStart();
        Object leftEnd = left.getEnd();
        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();

        Boolean boundaryCheck =
                AndEvaluator.and(
                        InEvaluator.intervalIn(leftStart, right, precision),
                        InEvaluator.intervalIn(leftEnd, right, precision)
                );

        if (boundaryCheck != null && boundaryCheck) {
            return true;
        }

        if (leftStart instanceof BaseTemporal || leftEnd instanceof BaseTemporal
                || rightStart instanceof BaseTemporal || rightEnd instanceof BaseTemporal)
        {
            if (AnyTrueEvaluator.anyTrue(Arrays.asList(BeforeEvaluator.before(leftStart, rightStart, precision), AfterEvaluator.after(leftEnd, rightEnd, precision))))
            {
                return false;
            }
            return AndEvaluator.and(
                    SameOrAfterEvaluator.sameOrAfter(leftStart, rightStart, precision),
                    SameOrBeforeEvaluator.sameOrBefore(leftEnd, rightEnd, precision)
            );
        }

        if (AnyTrueEvaluator.anyTrue(Arrays.asList(LessEvaluator.less(leftStart, rightStart), GreaterEvaluator.greater(leftEnd, rightEnd))))
        {
            return false;
        }
        return AndEvaluator.and(
                GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart),
                LessOrEqualEvaluator.lessOrEqual(leftEnd, rightEnd)
        );
    }

    public static Boolean listIncludedIn(Iterable left, Iterable right) {
        if (left == null) {
            return true;
        }
        if (right == null) {
            return false;
        }

        for (Object element : left) {
            Object in = InEvaluator.listIn(element, right);

            if (in == null) continue;

            if (!(Boolean) in) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() != null ? getPrecision().value() : null;

        if (left == null && right == null) {
            return null;
        }

        if (left == null) {
            return right instanceof Interval
                    ? intervalIncludedIn(null, (Interval) right, precision)
                    : listIncludedIn(null, (Iterable) right);
        }

        if (right == null) {
            return left instanceof Interval
                    ? intervalIncludedIn((Interval) left, null, precision)
                    : listIncludedIn((Iterable) left, null);
        }

        return includedIn(left, right, precision);
    }
}
