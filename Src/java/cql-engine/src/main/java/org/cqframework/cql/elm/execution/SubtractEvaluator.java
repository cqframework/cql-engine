package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/14/2016
 */
public class SubtractEvaluator extends Subtract {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        // -(Integer, Integer)
        if (left instanceof Integer) {
            return (Integer)left - (Integer)right;
        }

        // -(Decimal, Decimal)
        else if (left instanceof BigDecimal) {
            return ((BigDecimal)left).subtract((BigDecimal)right);
        }

        // -(Quantity, Quantity)
        else if (left instanceof Quantity) {
          return (((Quantity)left).getValue()).subtract(((Quantity)right).getValue());
        }

        // TODO: Finish implementation of Subtract
        // -(DateTime, Quantity)
        // -(Time, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
