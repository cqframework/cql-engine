package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/13/2016
 */
public class DivideEvaluator extends Divide {

  public static Object divide(Object left, Object right) {
    if (left instanceof BigDecimal) {
        return ((BigDecimal)left).divide((BigDecimal)right);
    }
    else if (left instanceof Quantity && right instanceof Quantity) {
      return new Quantity().withValue((((Quantity)left).getValue()).divide(((Quantity)right).getValue())).withUnit(((Quantity)left).getUnit());
    }
    else if (left instanceof Quantity && right instanceof BigDecimal) {
      return new Quantity().withValue((((Quantity)left).getValue()).divide((BigDecimal)right)).withUnit(((Quantity)left).getUnit());
    }

    throw new IllegalArgumentException(String.format("Cannot Divide arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));

  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null || (Integer)right == 0) {
            return null;
        }
        return divide(left, right);
    }
}
