package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/8/2016
*/

public class StartsEvaluator extends Starts {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();
      Object rightEnd = right.getEnd();

      if (leftStart instanceof Integer) {
        return ((Integer)leftStart == (Integer)rightStart && (Integer)leftEnd <= (Integer)rightEnd);
      }

      else if (leftStart instanceof BigDecimal) {
        return (((BigDecimal)leftStart).compareTo((BigDecimal)rightStart) == 0 && ((BigDecimal)leftEnd).compareTo((BigDecimal)rightEnd) <= 0);
      }

      else if (leftStart instanceof Quantity) {
        return ((((Quantity)leftStart).getValue()).compareTo(((Quantity)rightStart).getValue()) == 0 && (((Quantity)leftEnd).getValue()).compareTo(((Quantity)rightEnd).getValue()) <= 0);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
      }
    }
    return null;
  }
}
