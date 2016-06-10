package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
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
        if (leftStart instanceof Integer) {
          if ((Integer)rightStart > (Integer)leftEnd) { return leftInterval; }
          else if ((Integer)leftStart < (Integer)rightStart && (Integer)leftEnd > (Integer)rightEnd) { return null; }
          else if ((Integer)leftStart < (Integer)rightStart && (Integer)leftEnd <= (Integer)rightEnd) {
            return new Interval((Integer)leftStart, true, (Object)Math.min((Integer)rightStart - 1, (Integer)leftEnd), true);
          }
          else if ((Integer)leftStart >= (Integer)rightStart && (Integer)leftEnd > (Integer)rightEnd) {
            return new Interval((Object)Math.max((Integer)rightEnd + 1, (Integer)leftStart), true, (Integer)leftEnd, true);
          }
          else { return null; }
        }

        else if (leftStart instanceof BigDecimal) {
          BigDecimal b1 = (BigDecimal)leftStart;
          BigDecimal e1 = (BigDecimal)leftEnd;
          BigDecimal b2 = (BigDecimal)rightStart;
          BigDecimal e2 = (BigDecimal)rightEnd;
          if (b2.compareTo(e1) > 0) { return leftInterval; }
          else if (b1.compareTo(b2) < 0 && e1.compareTo(e2) > 0) {
            return null;
          }
          else if ((b1.compareTo(b2) < 0) && (e1.compareTo(e2) <= 0)) {
            return new Interval(b1, true, e1.min(b2.subtract(new BigDecimal("1.0"))), true);
          }
          else if ((b1.compareTo(b2) >= 0) && (e1.compareTo(e2) > 0)) {
            return new Interval(b1.max(e2.add(new BigDecimal("1.0"))), true, e1, true);
          }
          else { return null; }
        }

        else if (leftStart instanceof Quantity) {
          BigDecimal b1 = ((Quantity)leftStart).getValue();
          BigDecimal e1 = ((Quantity)leftEnd).getValue();
          BigDecimal b2 = ((Quantity)rightStart).getValue();
          BigDecimal e2 = ((Quantity)rightEnd).getValue();
          String unit = ((Quantity)leftStart).getUnit();
          if (b2.compareTo(e1) > 0) { return leftInterval; }
          else if (b1.compareTo(b2) < 0 && e1.compareTo(e2) > 0) {
            return null;
          }
          else if ((b1.compareTo(b2) < 0) && (e1.compareTo(e2) <= 0)) {
            return new Interval(new Quantity().withValue(b1).withUnit(unit), true, new Quantity().withValue(e1.min(b2.subtract(new BigDecimal("1.0")))).withUnit(unit), true);
          }
          else if ((b1.compareTo(b2) >= 0) && (e1.compareTo(e2) > 0)) {
            return new Interval(new Quantity().withValue(b1.max(e2.add(new BigDecimal("1.0")))).withUnit(unit), true, new Quantity().withValue(e1).withUnit(unit), true);
          }
          else { return null; }
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
