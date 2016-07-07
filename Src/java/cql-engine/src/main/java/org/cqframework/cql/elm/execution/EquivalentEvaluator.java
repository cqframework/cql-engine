package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
*** NOTES FOR INTERVAL ***
~(left Interval<T>, right Interval<T>) Boolean
The ~ operator for intervals returns true if and only if the intervals are over the same point type,
  and the starting and ending points of the intervals as determined by the Start and End operators are equivalent.

*** NOTES FOR LIST ***
~(left List<T>, right List<T>) Boolean
The ~ operator for lists returns true if and only if the lists contain elements of the same type, have the same number of elements,
  and for each element in the lists, in order, the elements are equivalent.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EquivalentEvaluator extends Equivalent {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return org.cqframework.cql.runtime.Value.equivalent(left, right);
    }
}
