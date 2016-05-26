package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class PowerEvaluator extends Power {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            return new BigDecimal((Integer)left).pow((Integer)right).intValue();
        }

        if (left instanceof BigDecimal) {
            return new BigDecimal(Math.pow((((BigDecimal)left).doubleValue()), ((BigDecimal)right).doubleValue()));
        }

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
