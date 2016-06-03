package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ToDecimalEvaluator extends ToDecimal {

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        if (operand == null) {
            return null;
        }

        if (operand instanceof Integer) {
            return new BigDecimal((Integer)operand);
        }

        if (operand instanceof String) {
            return new BigDecimal((String)operand);
        }

        throw new IllegalArgumentException(String.format("Cannot call %s argument of type '%s'.", this.getClass().getSimpleName(), operand.getClass().getName()));
    }
}
