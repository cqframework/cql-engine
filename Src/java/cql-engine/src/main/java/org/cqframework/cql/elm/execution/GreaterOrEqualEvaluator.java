package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class GreaterOrEqualEvaluator extends GreaterOrEqual {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            return Integer.compare((Integer)left, (Integer)right) >= 0;
        }

        if (left instanceof BigDecimal) {
            return ((BigDecimal)left).compareTo((BigDecimal)right) >= 0;
        }

        if (left instanceof String) {
            return ((String)left).compareTo((String)right) >= 0;
        }

        // TODO: Finish implementation
        // >(Quantity, Quantity)
        // >(DateTime, DateTime)
        // >(Time, Time)

        return false;
    }
}
