package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
start of(argument Interval<T>) T

The Start operator returns the starting point of an interval.
If the low boundary of the interval is open, this operator returns the successor of the low value of the interval.
  Note that if the low value of the interval is null, the result is null.
If the low boundary of the interval is closed and the low value of the interval is not null, this operator returns the
  low value of the interval. Otherwise, the result is the minimum value of the point type of the interval.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class StartEvaluator extends Start {

    @Override
    public Object evaluate(Context context) {
        org.cqframework.cql.runtime.Interval argument = (org.cqframework.cql.runtime.Interval)this.getOperand().evaluate(context);
        if (argument != null) {
            return argument.getStart();
        }

        return null;
    }
}
