package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/13/2016
 */
public class TruncatedDivideEvaluator extends TruncatedDivide {

  public static Object div(Object left, Object right) {
    if (left instanceof Integer) {
      return (Integer)left / (Integer)right;
    }
    else if (left instanceof BigDecimal) {
        return ((BigDecimal)left).divideAndRemainder((BigDecimal)right)[0];
    }

    throw new IllegalArgumentException(String.format("Cannot Div arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }
        return div(left, right);
    }
}
