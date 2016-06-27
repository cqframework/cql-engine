package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
*** NOTES FOR INTERVAL ***
The equal (=) operator for intervals returns true if and only if the intervals are over the same point type,
  and they have the same value for the starting and ending points of the intervals as determined by the Start and End operators.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EqualEvaluator extends Equal {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return org.cqframework.cql.runtime.Value.equals(left, right);
    }
}
