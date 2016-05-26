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

        return new BigDecimal((String)operand);
    }
}
