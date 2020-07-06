package org.opencds.cqf.cql.engine.elm.execution;

import java.math.BigDecimal;

import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.CqlList;
import org.opencds.cqf.cql.engine.runtime.CqlType;
import org.opencds.cqf.cql.engine.runtime.Interval;

/*
*** NOTES FOR CLINICAL OPERATORS ***
~(left Code, right Code) Boolean

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
~(left Interval<T>, right Interval<T>) Boolean

The ~ operator for intervals returns true if and only if the intervals are over the same point type,
  and the starting and ending points of the intervals as determined by the Start and End operators are equivalent.

*** NOTES FOR LIST ***
~(left List<T>, right List<T>) Boolean

The ~ operator for lists returns true if and only if the lists contain elements of the same type, have the same number of elements,
  and for each element in the lists, in order, the elements are equivalent.
*/

public class EquivalentEvaluator extends org.cqframework.cql.elm.execution.Equivalent {

    public static Boolean equivalent(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        if (left instanceof Interval && right instanceof Integer) {
            return ((Interval) left).equivalent(right);
        }

        if (right instanceof Interval && left instanceof Integer) {
            return ((Interval) right).equivalent(left);
        }

        if (!left.getClass().equals(right.getClass())) {
            return false;
        }

        else if (left instanceof Boolean || left instanceof Integer) {
            return left.equals(right);
        }

        else if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
        }

        if (left instanceof Iterable) {
            return CqlList.equivalent((Iterable<?>) left, (Iterable<?>) right);
        }

        else if (left instanceof CqlType) {
            return ((CqlType) left).equivalent(right);
        }

        else if (left instanceof String && right instanceof String) {
            return ((String) left).equalsIgnoreCase((String) right);
        }

        return Context.getContext().objectEquivalent(left, right);
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return equivalent(left, right);
    }
}
