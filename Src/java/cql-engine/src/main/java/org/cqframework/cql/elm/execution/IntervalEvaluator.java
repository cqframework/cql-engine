package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class IntervalEvaluator extends Interval {

    @Override
    public Object evaluate(Context context) {
        Object low = getLow() != null ? getLow().evaluate(context) : null;
        Boolean lowClosed = getLowClosedExpression() != null ? (Boolean)getLowClosedExpression().evaluate(context) : this.lowClosed;
        Object high = getHigh() != null ? getHigh().evaluate(context) : null;
        Boolean highClosed = getHighClosedExpression() != null ? (Boolean)getHighClosedExpression().evaluate(context) : this.highClosed;

        // An interval with no boundaries is not an interval
        if (low == null && high == null) {
            return null;
        }

        return new org.cqframework.cql.runtime.Interval(low, lowClosed == null ? true : lowClosed, high, highClosed == null ? true : highClosed);
    }
}
