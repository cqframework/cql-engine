package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/13/2016
 */
public class DivideEvaluator extends Divide {

  public static Object divide(Object left, Object right) {

    if (left == null || right == null) {
        return null;
    }

    if (left instanceof BigDecimal) {
      if (Value.compareTo(right, new BigDecimal("0.0"), "==")) { return null; }
      return ((BigDecimal)left).divide((BigDecimal)right);
    }

    else if (left instanceof Quantity && right instanceof Quantity) {
      if (Value.compareTo(((Quantity)right).getValue(), new BigDecimal(0), "==")) { return null; }
      return new Quantity().withValue((((Quantity)left).getValue()).divide(((Quantity)right).getValue())).withUnit(((Quantity)left).getUnit());
    }

    else if (left instanceof Quantity && right instanceof BigDecimal) {
      if (Value.compareTo(right, new BigDecimal("0.0"), "==")) { return null; }
      return new Quantity().withValue((((Quantity)left).getValue()).divide((BigDecimal)right)).withUnit(((Quantity)left).getUnit());
    }

    throw new IllegalArgumentException(String.format("Cannot Divide arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));

  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return divide(left, right);
    }
}
