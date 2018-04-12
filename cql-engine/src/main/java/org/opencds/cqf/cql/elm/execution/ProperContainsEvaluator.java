package org.opencds.cqf.cql.elm.execution;

/*
    There are two overloads of this operator:
        List, T: The type of T must be the same as the element type of the list.
        Interval, T : The type of T must be the same as the point type of the interval.

    For the List, T overload, this operator returns true if the given element is in the list,
        and it is not the only element in the list, using equivalence semantics.
    For the Interval, T overload, this operator returns true if the given point is greater than
        the starting point of the interval, and less than the ending point of the interval, as
        determined by the Start and End operators.
        If precision is specified and the point type is a date/time type, comparisons used in the
            operation are performed at the specified precision.
*/

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.List;

public class ProperContainsEvaluator extends org.cqframework.cql.elm.execution.ProperContains {

    public static Boolean properContains(Object left, Object right) {
        if (left instanceof Interval) {
            Boolean startProperContains = GreaterEvaluator.greater(right, ((Interval) left).getStart());
            Boolean endProperContains = LessEvaluator.less(right, ((Interval) left).getEnd());

            return startProperContains == null ? null : endProperContains == null ? null : startProperContains && endProperContains;
        }

        else if (left instanceof Iterable) {
            List leftList = (List) left;

            for (Object element : leftList) {
                Boolean isElementInList = EquivalentEvaluator.equivalent(element, right);
                if (isElementInList == null) {
                    return null;
                }

                if (isElementInList && leftList.size() > 1) {
                    return true;
                }
            }

            return false;
        }

        throw new IllegalArgumentException(String.format("Cannot perform ProperContains operation with arguments of type: %s and %s", left.getClass().getName(), right.getClass().getName()));
    }

    public static Boolean properContains(Object left, Object right, String precision) {
        if (left instanceof Interval && right instanceof BaseTemporal) {
            Boolean startProperContains = AfterEvaluator.after(right, ((Interval) left).getStart(), precision);
            Boolean endProperContains = BeforeEvaluator.before(right, ((Interval) left).getEnd(), precision);

            return startProperContains == null ? null : endProperContains == null ? null : startProperContains && endProperContains;
        }

        return properContains(left, right);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() != null ? getPrecision().value() : null;

        return context.logTrace(this.getClass(), properContains(left, right, precision), left, right, precision);
    }
}
