package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/7/2016
*/

public class EndsEvaluator extends Ends {

  @Override
  public Object evaluate(Context context) {
    Interval leftInterval = (Interval)getOperand().get(0).evaluate(context);
    Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

    if (leftInterval != null && rightInterval != null) {
      Object leftStart = leftInterval.getStart();
      Object leftEnd = leftInterval.getEnd();
      Object rightStart = rightInterval.getStart();
      Object rightEnd = rightInterval.getEnd();

      if (leftStart instanceof Integer) {
          return ((Integer)leftStart >= (Integer)rightStart && (Integer)leftEnd == (Integer)rightEnd);
      }

      else if (leftStart instanceof BigDecimal) {
          return (((BigDecimal)leftStart).compareTo((BigDecimal)rightStart) >= 0 && ((BigDecimal)leftEnd).compareTo((BigDecimal)rightEnd) == 0);
      }

      else if (leftStart instanceof Quantity) {
        return ((((Quantity)leftStart).getValue()).compareTo(((Quantity)rightStart).getValue()) >= 0 && (((Quantity)leftEnd).getValue()).compareTo(((Quantity)rightEnd).getValue()) == 0);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), leftStart.getClass().getName(), rightStart.getClass().getName()));
      }
    }
    return null;
  }
}
