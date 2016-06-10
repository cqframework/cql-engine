package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/7/2016
*/

public class BeforeEvaluator extends Before {

  @Override
  public Object evaluate(Context context) {
    Object testLeft = getOperand().get(0).evaluate(context);
    Object testRight = getOperand().get(1).evaluate(context);

    if (testLeft == null || testRight == null) { return null; }

    if (testLeft instanceof Interval && testRight instanceof Interval) {
      Interval leftInterval = (Interval)getOperand().get(0).evaluate(context);
      Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

      if (leftInterval != null && rightInterval != null) {
        Object left = leftInterval.getStart();
        Object right = rightInterval.getEnd();

        if (left instanceof Integer) {
            return (Integer)left < (Integer)right;
        }

        else if (left instanceof BigDecimal) {
            return (((BigDecimal)left).compareTo((BigDecimal)right) < 0);
        }

        else if (left instanceof Quantity) {
          return (((Quantity)left).getValue()).compareTo(((Quantity)right).getValue()) < 0;
        }

        else {
          throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
        }
      }
    }

    else if (testLeft instanceof Interval && !(testRight instanceof Interval)) {
      Interval leftInterval = (Interval)testLeft;
      Object right = testRight;

      if (right instanceof Integer) {
        return (Integer)leftInterval.getEnd() < (Integer)right;
      }
      else if (right instanceof BigDecimal) {
        return ((BigDecimal)leftInterval.getEnd()).compareTo((BigDecimal)right) < 0;
      }
      else if (right instanceof Quantity) {
        return (((Quantity)leftInterval.getEnd()).getValue()).compareTo(((Quantity)right).getValue()) < 0;
      }
      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s'.", this.getClass().getSimpleName(), right.getClass().getName()));
      }
    }

    else if (!(testLeft instanceof Interval) && testRight instanceof Interval) {
      Object left = testLeft;
      Interval rightInterval = (Interval)testRight;

      if (left instanceof Integer) {
        return (Integer)left < (Integer)rightInterval.getStart();
      }
      else if (left instanceof BigDecimal) {
        return ((BigDecimal)left).compareTo((BigDecimal)rightInterval.getStart()) < 0;
      }
      else if (left instanceof Quantity) {
        return (((Quantity)left).getValue()).compareTo(((Quantity)rightInterval.getStart()).getValue()) < 0;
      }
      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s'.", this.getClass().getSimpleName(), left.getClass().getName()));
      }
    }

    return null;
  }
}
