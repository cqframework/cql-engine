package org.opencds.cqf.cql.runtime;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.Partial;
import org.opencds.cqf.cql.elm.execution.GreaterOrEqualEvaluator;
import org.opencds.cqf.cql.elm.execution.LessOrEqualEvaluator;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

/*
*** NOTES FOR CLINICAL OPERATORS ***
=(left Code, right Code) Boolean
=(left Concept, right Concept) Boolean
~(left Code, right Code) Boolean

The equal (=) operator for Codes and Concepts uses tuple equality semantics.
  This means that the operator will return true if and only if the values for each element by name are equal.
If either argument is null, or contains any null components, the result is null.

The ~ operator for Code values returns true if the code, system, and version elements are equivalent.
  The display element is ignored for the purposes of determining Code equivalence.
For Concept values, equivalence is defined as a non-empty intersection of the codes in each Concept.
  The display element is ignored for the purposes of determining Concept equivalence.
Note that this operator will always return true or false, even if either or both of its arguments are null,
  or contain null components.
Note carefully that this notion of equivalence is not the same as the notion of equivalence used in terminology:
  "these codes represent the same concept." CQL specifically avoids defining terminological equivalence.
    The notion of equivalence defined here is used to provide consistent and intuitive semantics when dealing with
      missing information in membership contexts.

*** NOTES FOR INTERVAL ***
=(left Interval<T>, right Interval<T>) Boolean
~(left Interval<T>, right Interval<T>) Boolean

The equal (=) operator for intervals returns true if and only if the intervals are over the same point type,
  and they have the same value for the starting and ending points of the intervals as determined by the Start and End operators.
If either argument is null, the result is null.

The ~ operator for intervals returns true if and only if the intervals are over the same point type,
  and the starting and ending points of the intervals as determined by the Start and End operators are equivalent.

*** NOTES FOR LIST ***
=(left List<T>, right List<T>) Boolean
~(left List<T>, right List<T>) Boolean

The equal (=) operator for lists returns true if and only if the lists have the same element type,
  and have the same elements by value, in the same order.
If either argument is null, or contains null elements, the result is null.

The ~ operator for lists returns true if and only if the lists contain elements of the same type, have the same number of elements,
  and for each element in the lists, in order, the elements are equivalent.
*/

/**
 * Created by Bryn on 5/2/2016.
 */
public class Value {

    public enum SimilarityMode { EQUAL, EQUIVALENT }

    public static Boolean similar(Object left, Object right, SimilarityMode mode) {
        if (left == null || right == null) {
            return mode.equals(SimilarityMode.EQUAL) ? null : (left == null && right == null);
        }

        if (left instanceof Uncertainty && right instanceof Integer) {
            return ((Uncertainty) left).equal(right);
        }

        // mismatched types not allowed
        if (!left.getClass().equals(right.getClass())) {
            // Note: Either (or both) input Java Object might be invalid, not representing any CQL value.
            return null;
        }

        if (left instanceof Uncertainty) {
            return ((Uncertainty) left).equal(right);
        }

        if (left instanceof Boolean) {
            return left.equals(right);
        }

        if (left instanceof Integer) {
            return left.equals(right);
        }

        // Decimal
        if (left instanceof BigDecimal) {
            return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
        }

        if (left instanceof String) {
            return left.equals(right);
        }

        if (left instanceof DateTime) {
            return ((DateTime) left).similar((DateTime) right, mode);
        }

        if (left instanceof Time) {
            return ((Time) left).similar((Time) right, mode);
        }

        // List
        if (left instanceof Iterable) {
            Iterator leftIterator = ((Iterable)left).iterator();
            Iterator rightIterator = ((Iterable)right).iterator();

            while (leftIterator.hasNext()) {
                Object leftObject = leftIterator.next();
                if (rightIterator.hasNext()) {
                    Object rightObject = rightIterator.next();
                    Boolean elementSimilar = similar(leftObject, rightObject, mode);
                    if (elementSimilar == null || !elementSimilar) {
                        return elementSimilar;
                    }
                }
                else {
                    return false;
                }
            }

            if (rightIterator.hasNext()) {
                return rightIterator.next() == null ? null : false;
            }

            return true;
        }

        if (left instanceof Interval) {
            return ((Interval) left).similar((Interval) right, mode);
        }

        if (left instanceof Tuple) {
            return ((Tuple) left).similar((Tuple) right, mode);
        }

        if (left instanceof Quantity) {
            return ((Quantity) left).equal((Quantity) right);
        }

        if (left instanceof Code) {
            return ((Code) left).equal((Code) right);
        }

        if (left instanceof Concept) {
            return ((Concept) left).equal((Concept) right);
        }

        // Note: Either (or both) input Java Object might be invalid, not representing any CQL value.
        throw new NotImplementedException(String.format("Equal and Equivalent are not implemented for type %s", left.getClass().getName()));
    }

    public static BigDecimal verifyPrecision(BigDecimal value) {
        // at most 8 decimal places
        if (value.precision() > 8) {
            return value.setScale(8, RoundingMode.FLOOR);
        }

        else if (value.precision() < 2) {
            return value.setScale(1, RoundingMode.FLOOR);
        }

        return value;
    }

    public static Object successor(Object value) {
        if (value == null) {
            return null;
        }

        if (GreaterOrEqualEvaluator.greaterOrEqual(value, maxValue(value.getClass()))) {
            throw new RuntimeException("The result of the successor operation exceeds the maximum value allowed for the type");
        }

        else if (value instanceof Integer) {
            return ((Integer)value) + 1;
        }
        else if (value instanceof BigDecimal) {
            return ((BigDecimal)value).add(new BigDecimal("0.00000001"));
        }
        else if (value instanceof Quantity) {
            Quantity quantity = (Quantity)value;
            return new Quantity().withValue((BigDecimal)successor(quantity.getValue())).withUnit(quantity.getUnit());
        }
        else if (value instanceof DateTime) {
            DateTime dt = (DateTime)value;
            return new DateTime(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(1), dt.getTimezone());
        }
        else if (value instanceof Time) {
            Time t = (Time)value;
            return new Time(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(1), t.getTimezone());
        }

        throw new NotImplementedException(String.format("Successor is not implemented for type %s", value.getClass().getName()));
    }



    public static Object predecessor(Object value) {
        if (value == null) {
            return null;
        }
        if (LessOrEqualEvaluator.lessOrEqual(value, minValue(value.getClass()))) {
            throw new RuntimeException("The result of the predecessor operation precedes the minimum value allowed for the type");
        }
        else if (value instanceof Integer) {
            return ((Integer)value) - 1;
        }
        else if (value instanceof BigDecimal) {
            return ((BigDecimal)value).subtract(new BigDecimal("0.00000001"));
        }
        else if (value instanceof Quantity) {
            Quantity quantity = (Quantity)value;
            return new Quantity().withValue((BigDecimal)predecessor(quantity.getValue())).withUnit(quantity.getUnit());
        }
        else if (value instanceof DateTime) {
            DateTime dt = (DateTime)value;
            return new DateTime(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(-1), dt.getTimezone());
        }
        else if (value instanceof Time) {
            Time t = (Time)value;
            return new Time(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(-1), t.getTimezone());
        }

        throw new NotImplementedException(String.format("Predecessor is not implemented for type %s", value.getClass().getName()));
    }

    public static Object minValue(Type type) {
        if (type == Integer.class) {
            return Integer.MIN_VALUE;
        }
        else if (type == BigDecimal.class) {
            return new BigDecimal("-9999999999999999999999999999.99999999");
        }
        else if (type == Quantity.class) {
            return new Quantity().withValue((BigDecimal)minValue(BigDecimal.class));
        }
        else if (type == DateTime.class) {
            return new DateTime(new Partial(DateTime.fields, new int[] {0001, 1, 1, 0, 0, 0, 0}));
        }
        else if (type == Time.class) {
            return new Time(new Partial(Time.fields, new int[] {0, 0, 0, 0}));
        }

        throw new NotImplementedException(String.format("MinValue is not implemented for type %s.", type.getTypeName()));
    }

    public static Object maxValue(Type type) {
        if (type == Integer.class) {
            return Integer.MAX_VALUE;
        }
        else if (type == BigDecimal.class) {
            return new BigDecimal("9999999999999999999999999999.99999999");
        }
        else if (type == Quantity.class) {
            return new Quantity().withValue((BigDecimal)maxValue(BigDecimal.class));
        }
        else if (type == DateTime.class) {
            return new DateTime(new Partial(DateTime.fields, new int[] {9999, 12, 31, 23, 59, 59, 999}));
        }
        else if (type == Time.class) {
            return new Time(new Partial(Time.fields, new int[] {23, 59, 59, 999}));
        }

        throw new NotImplementedException(String.format("MaxValue is not implemented for type %s.", type.getTypeName()));
    }
}
