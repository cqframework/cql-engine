package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import java.util.*;
import java.lang.Math.*;
import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class ExceptEvaluator extends Except {

  @Override
  public Object evaluate(Context context) {
    Object test = getOperand().get(0).evaluate(context);
    if (test instanceof Interval) {
      Interval leftInterval = (Interval)test;
      Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

      if (leftInterval != null && rightInterval != null) {
        Object leftStart = leftInterval.getStart();
        Object leftEnd = leftInterval.getEnd();
        Object rightStart = rightInterval.getStart();
        Object rightEnd = rightInterval.getEnd();

        if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

        if (Value.compareTo(rightStart, leftEnd, ">")) { return leftInterval; }
        else if (Value.compareTo(leftStart, rightStart, "<") && Value.compareTo(leftEnd, rightEnd, ">")) { return null; }

        Boolean leftFirst = (Value.compareTo(leftStart, rightStart, "<") && Value.compareTo(leftEnd, rightEnd, "<="));
        Boolean rightFirst = (Value.compareTo(leftStart, rightStart, ">=") && Value.compareTo(leftEnd, rightEnd, ">"));

        if (leftStart instanceof Integer && leftFirst) {
            return new Interval((Integer)leftStart, true, (Object)Math.min((Integer)rightStart - 1, (Integer)leftEnd), true);
        }
        else if (leftStart instanceof Integer && rightFirst) {
          return new Interval((Object)Math.max((Integer)rightEnd + 1, (Integer)leftStart), true, (Integer)leftEnd, true);
        }

        else if (leftStart instanceof BigDecimal && leftFirst) {
          return new Interval((BigDecimal)leftStart, true, ((BigDecimal)leftEnd).min(((BigDecimal)rightStart).subtract(new BigDecimal("1.0"))), true);
        }
        else if (leftStart instanceof BigDecimal && rightFirst) {
          return new Interval(((BigDecimal)leftStart).max(((BigDecimal)leftEnd).add(new BigDecimal("1.0"))), true, (BigDecimal)leftEnd, true);
        }

        else if (leftStart instanceof Quantity && leftFirst) {
          String unit = ((Quantity)leftStart).getUnit();
          return new Interval(new Quantity().withValue(((Quantity)leftStart).getValue()).withUnit(unit), true, new Quantity().withValue((((Quantity)leftEnd).getValue()).min((((Quantity)rightStart).getValue()).subtract(new BigDecimal("1.0")))).withUnit(unit), true);
        }
        else if (leftStart instanceof Quantity && rightFirst) {
          String unit = ((Quantity)leftStart).getUnit();
          return new Interval(new Quantity().withValue((((Quantity)leftStart).getValue()).max((((Quantity)rightEnd).getValue()).add(new BigDecimal("1.0")))).withUnit(unit), true, new Quantity().withValue(((Quantity)leftEnd).getValue()).withUnit(unit), true);
        }
      }
      return null;
    }

    else {
      Iterable<Object> left = (Iterable<Object>)getOperand().get(0).evaluate(context);
      Iterable<Object> right = (Iterable<Object>)getOperand().get(1).evaluate(context);

      if (left == null || right == null) {
          return null;
      }

      java.util.List<Object> result = new ArrayList<Object>();
      for (Object leftItem : left) {
          if (!InEvaluator.in(leftItem, right)) {
              result.add(leftItem);
          }
      }

      return result;
    }
  }
}
