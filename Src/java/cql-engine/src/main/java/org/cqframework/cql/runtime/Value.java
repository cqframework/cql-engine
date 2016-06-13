package org.cqframework.cql.runtime;

import java.math.BigDecimal;
import org.cqframework.cql.runtime.Quantity;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Bryn on 5/2/2016.
 * Edited by Chris Schuler on 6/10/2016 - Added compareTo(), compare(), and Interval logic in equals()
 */
public class Value {

    public static Boolean compare(double left, double right, String op) {
      if (op.equals("==")) { return left == right; }
      else if (op.equals("!=")) { return left != right; }
      else if (op.equals("<")) { return left < right; }
      else if (op.equals(">")) { return left > right; }
      else if (op.equals("<=")) {return left <= right; }
      else if (op.equals(">=")) { return left >= right; }
      else { return null; }
    }

    public static Boolean compareTo(Object left, Object right, String op) {
      if (left == null || right == null) { return null; }

      if (left instanceof Integer) {
        BigDecimal leftOp = new BigDecimal((Integer)left);
        BigDecimal rightOp = new BigDecimal((Integer)right);
        if (leftOp == null || rightOp == null) { return null; }
        return compare(leftOp.doubleValue(), rightOp.doubleValue(), op);
      }

      else if (left instanceof BigDecimal) {
        return compare(((BigDecimal)left).doubleValue(), ((BigDecimal)right).doubleValue(), op);
      }

      else if (left instanceof Quantity) {
        BigDecimal leftOp = ((Quantity)left).getValue();
        BigDecimal rightOp = ((Quantity)right).getValue();
        if (leftOp == null || rightOp == null) { return null; }
        return compare(leftOp.doubleValue(), rightOp.doubleValue(), op);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot Compare arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
      }
    }

    public static Boolean equals(Object left, Object right) {
        if ((left == null) || (right == null)) {
            return null;
        }

        if (left instanceof Interval) {
          Object leftStart = ((Interval)left).getStart();
          Object leftEnd = ((Interval)left).getEnd();
          Object rightStart = ((Interval)right).getStart();
          Object rightEnd = ((Interval)right).getEnd();

          if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

          return (compareTo(leftStart, rightStart, "==") && compareTo(leftEnd, rightEnd, "=="));
        }

        // list equal
        if (left instanceof Iterable) {
            Iterable<Object> leftList = (Iterable<Object>)left;
            Iterable<Object> rightList = (Iterable<Object>)right;
            Iterator<Object> leftIterator = leftList.iterator();
            Iterator<Object> rightIterator = rightList.iterator();

            while (leftIterator.hasNext()) {
                Object leftObject = leftIterator.next();
                if (rightIterator.hasNext()) {
                    Object rightObject = rightIterator.next();
                    Boolean elementEquals = equals(leftObject, rightObject);
                    if (elementEquals == null || elementEquals == false) {
                        return elementEquals;
                    }
                }
                else {
                    return false;
                }
            }

            return true;
        }

        // Decimal equal
        // Have to use this because 10.0 != 10.00
        if (left instanceof BigDecimal) {
            return ((BigDecimal)left).compareTo((BigDecimal)right) == 0;
        }

        return left.equals(right);
    }

    public static Boolean equivalent(Object left, Object right) {
        if ((left == null) && (right == null)) {
            return true;
        }

        if ((left == null) || (right == null)) {
            return false;
        }

        // list equal
        if (left instanceof Iterable) {
            Iterable<Object> leftList = (Iterable<Object>)left;
            Iterable<Object> rightList = (Iterable<Object>)right;
            Iterator<Object> leftIterator = leftList.iterator();
            Iterator<Object> rightIterator = rightList.iterator();

            while (leftIterator.hasNext()) {
                Object leftObject = leftIterator.next();
                if (rightIterator.hasNext()) {
                    Object rightObject = rightIterator.next();
                    Boolean elementEquivalent = equivalent(leftObject, rightObject);
                    if (elementEquivalent == null || elementEquivalent == false) {
                        return elementEquivalent;
                    }
                }
                else {
                    return false;
                }
            }

            return true;
        }

        return equals(left, right);
    }

    public static Iterable<Object> ensureIterable(Object source) {
        if (source instanceof Iterable) {
            return (Iterable<Object>)source;
        }
        else {
            ArrayList sourceList = new ArrayList();
            if (source != null)
                sourceList.add(source);
            return sourceList;
        }
    }
}
