package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler 6/8/2016
*/

public class WidthEvaluator extends Width {

  @Override
  public Object evaluate(Context context) {

    Interval argument = (Interval)getOperand().evaluate(context);

    if (argument != null) {
      Object start = argument.getStart();
      Object end = argument.getEnd();
      if (start instanceof Integer) { return (Integer)end - (Integer)start; }
      else if (start instanceof BigDecimal) { return ((BigDecimal)end).subtract((BigDecimal)start); }
      else if (start instanceof Quantity) {
        String unit = ((Quantity)start).getUnit();
        return new Quantity().withValue((((Quantity)end).getValue()).subtract(((Quantity)start).getValue())).withUnit(unit);
      }
      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), start.getClass().getName(), end.getClass().getName()));
      }
    }
    return null;
  }
}
