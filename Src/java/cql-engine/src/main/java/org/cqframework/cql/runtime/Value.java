package org.cqframework.cql.runtime;

import java.math.BigDecimal;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Tuple;
import org.cqframework.cql.runtime.Time;
import org.cqframework.cql.runtime.DateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.joda.time.DateTimeFieldType;
/**
 * Created by Bryn on 5/2/2016.
 * Edited by Chris Schuler
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

      // TODO: implement for Time and DateTime
      // else if (left instanceof DateTime) {
      //
      // }
      //
      // else if (left instanceof Time) {
      //
      // }

      else {
        throw new IllegalArgumentException(String.format("Cannot Compare arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
      }
    }

    public static Boolean equals(Object left, Object right) {
        if ((left == null) || (right == null) || !left.getClass().equals(right.getClass())) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
          Object leftStart = ((Interval)left).getStart();
          Object leftEnd = ((Interval)left).getEnd();
          Object rightStart = ((Interval)right).getStart();
          Object rightEnd = ((Interval)right).getEnd();

          if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

          return (compareTo(leftStart, rightStart, "==") && compareTo(leftEnd, rightEnd, "=="));
        }

        // list equal
        else if (left instanceof Iterable) {
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
        else if (left instanceof BigDecimal) {
            return ((BigDecimal)left).compareTo((BigDecimal)right) == 0;
        }

        else if (left instanceof Tuple) {
          return ((Tuple)left).equals((Tuple)right);
        }

        else if (left instanceof Time) {
          return ((Time)left).equals((Time)right);
        }

        else if (left instanceof DateTime && right instanceof DateTime) {
          if (((DateTime)left).dateTime.getValues().length < 7 || ((DateTime)right).dateTime.getValues().length < 7) {
            return null;
          }
          return Arrays.equals(((DateTime)left).dateTime.getValues(), ((DateTime)right).dateTime.getValues())
                 && ((DateTime)left).getTimezoneOffset().compareTo(((DateTime)right).getTimezoneOffset()) == 0;
        }

        // Uncertainty
        else if (left instanceof Interval && right instanceof DateTime) {
          
        }

        else if (left instanceof DateTime && right instanceof Interval) {

        }

        return left.equals(right);
    }

    public static Boolean equivalent(Object left, Object right) {
        if ((left == null) && (right == null)) {
            return true;
        }

        if ((left == null) || (right == null)) {
            return null;
        }

        // list equal
        else if (left instanceof Iterable) {
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

        else if (left instanceof Tuple) {
          return ((Tuple)left).equals((Tuple)right);
        }

        else if (left instanceof Time) {
          return ((Time)left).equals((Time)right);
        }

        else if (left instanceof DateTime) {
          return ((DateTime)left).equals((DateTime)right);
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
