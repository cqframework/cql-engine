package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/8/2016
*/

public class MeetsEvaluator extends Meets {

  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();
      Object rightEnd = right.getEnd();

      if (leftStart instanceof Integer) {
        return ((Integer)rightStart == (Integer)leftEnd + 1 || (Integer)leftStart == (Integer)rightEnd + 1);
      }

      else if (leftStart instanceof BigDecimal) {
        return (((BigDecimal)rightStart).compareTo(((BigDecimal)leftEnd).add(new BigDecimal(1))) == 0 || ((BigDecimal)leftStart).compareTo(((BigDecimal)rightEnd).add(new BigDecimal(1))) == 0);
      }

      else if (leftStart instanceof Quantity) {
        return ((((Quantity)rightStart).getValue()).compareTo((((Quantity)leftEnd).getValue()).add(new BigDecimal(1))) == 0 || (((Quantity)leftStart).getValue()).compareTo((((Quantity)rightEnd).getValue()).add(new BigDecimal(1))) == 0);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
      }
    }
    return null;
  }
}
