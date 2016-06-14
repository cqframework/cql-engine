package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/14/2016
 */
public class MultiplyEvaluator extends Multiply {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        // *(Integer, Integer)
        if (left instanceof Integer) {
            return (Integer)left * (Integer)right;
        }

        // *(Decimal, Decimal)
        else if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return ((BigDecimal)left).multiply((BigDecimal)right);
        }

        // *(Quantity, Quantity)
        else if (left instanceof Quantity && right instanceof Quantity) {
          return (((Quantity)left).getValue()).multiply(((Quantity)right).getValue());
        }

        // *(Decimal, Quantity)
        else if (left instanceof BigDecimal && right instanceof Quantity) {
          return ((BigDecimal)left).multiply(((Quantity)right).getValue());
        }

        // *(Quantity, Decimal)
        else if (left instanceof Quantity && right instanceof BigDecimal) {
          return (((Quantity)left).getValue()).multiply((BigDecimal)right);
        }

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
