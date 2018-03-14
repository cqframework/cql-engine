package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import org.apache.commons.lang3.NotImplementedException;

import java.math.BigDecimal;

/*
*** NOTES FOR CLINICAL OPERATORS ***
=(left Code, right Code) Boolean
=(left Concept, right Concept) Boolean

The equal (=) operator for Codes and Concepts uses tuple equality semantics.
  This means that the operator will return true if and only if the values for each element by name are equal.
If either argument is null, or contains any null components, the result is null.

*** NOTES FOR INTERVAL ***
=(left Interval<T>, right Interval<T>) Boolean

The equal (=) operator for intervals returns true if and only if the intervals are over the same point type,
  and they have the same value for the starting and ending points of the intervals as determined by the Start and End operators.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
=(left List<T>, right List<T>) Boolean

The equal (=) operator for lists returns true if and only if the lists have the same element type,
  and have the same elements by value, in the same order.
If either argument is null, or contains null elements, the result is null.

*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EqualEvaluator extends org.cqframework.cql.elm.execution.Equal {

    public enum SimilarityMode { EQUAL, EQUIVALENT }

    public static Boolean equal(Object left, Object right) {
        return EqualEvaluator.similar(left, right, SimilarityMode.EQUAL);
    }

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

        else if (left instanceof Boolean) {
            return left.equals(right);
        }

        else if (left instanceof Integer) {
            return left.equals(right);
        }

        // Decimal
        else if (left instanceof BigDecimal) {
            return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
        }

        else if (left instanceof String) {
            return left.equals(right);
        }

        else if (left instanceof Quantity) {
            return ((Quantity) left).equal((Quantity) right);
        }

        else if (left instanceof Code) {
            return ((Code) left).equal((Code) right);
        }

        else if (left instanceof Concept) {
            return ((Concept) left).equal((Concept) right);
        }

        else if (left instanceof DateTime) {
            return ((DateTime) left).similar((DateTime) right, mode);
        }

        else if (left instanceof Time) {
            return ((Time) left).similar((Time) right, mode);
        }

        else if (left instanceof Interval) {
            return ((Interval) left).similar((Interval) right, mode);
        }

        else if (left instanceof Tuple) {
            return ((Tuple) left).similar((Tuple) right, mode);
        }

        else if (left instanceof Uncertainty) {
            return ((Uncertainty) left).equal(right);
        }

        // List
        else if (left instanceof Iterable) {
            return CqlList.similar((Iterable)left, (Iterable)right, mode);
        }

        // Note: Either (or both) input Java Object might be invalid, not representing any CQL value.
        throw new NotImplementedException(String.format("Equal and Equivalent are not implemented for type %s", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), equal(left, right), left, right);
    }
}
