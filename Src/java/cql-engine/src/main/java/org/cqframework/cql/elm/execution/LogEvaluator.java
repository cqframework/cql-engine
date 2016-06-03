package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class LogEvaluator extends Log {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof BigDecimal) {
            Double base = Math.log(((BigDecimal)right).doubleValue());
            Double value = Math.log(((BigDecimal)left).doubleValue());

            if (base == 0) {
                return new BigDecimal(value);
            }

            return new BigDecimal(value / base);
        }

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
