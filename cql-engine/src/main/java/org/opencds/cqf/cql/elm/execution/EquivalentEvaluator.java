package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Tuple;

import java.util.HashMap;
import java.util.Iterator;

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

/**
 * Created by Bryn on 5/25/2016.
 */
public class EquivalentEvaluator extends org.cqframework.cql.elm.execution.Equivalent {

    public static Boolean equivalent(Object left, Object right) {
        if ((left == null) && (right == null)) {
            return true;
        }

        if ((left == null) || (right == null)) {
            return false;
        }

        if (left instanceof Iterable) {
            Iterator<Object> leftIterator = ((Iterable<Object>)left).iterator();
            Iterator<Object> rightIterator = ((Iterable<Object>)right).iterator();

            while (leftIterator.hasNext()) {
                Object leftObject = leftIterator.next();
                if (rightIterator.hasNext()) {
                    Object rightObject = rightIterator.next();
                    Boolean elementEquivalent = equivalent(leftObject, rightObject);
                    if (elementEquivalent == null || !elementEquivalent) {
                        return elementEquivalent;
                    }
                }
                else { return false; }
            }

            if (rightIterator.hasNext()) {
                return rightIterator.next() == null ? null : false;
            }

            return true;
        }

        else if (left instanceof Tuple) {
            HashMap<String, Object> leftMap = ((Tuple)left).getElements();
            HashMap<String, Object> rightMap = ((Tuple)right).getElements();
            for (String key : rightMap.keySet()) {
                if (leftMap.containsKey(key)) {
                    if (equivalent(rightMap.get(key), leftMap.get(key)) == null) { return null; }
                    else if (!equivalent(rightMap.get(key), leftMap.get(key))) { return false; }
                }
                else { return false; }
            }
            return true;
        }

        // Do not want to call the equals method for DateTime or Time - returns null if missing elements...
        else if (left instanceof DateTime && right instanceof DateTime) {
            DateTime leftDT = (DateTime)left;
            DateTime rightDT = (DateTime)right;
            if (leftDT.getPartial().size() != rightDT.getPartial().size()) { return false; }

            for (int i = 0; i < leftDT.getPartial().size(); ++i) {
                if (leftDT.getPartial().getValue(i) != rightDT.getPartial().getValue(i)) {
                    return false;
                }
            }
            return true;
        }

        else if (left instanceof Time && right instanceof Time) {
            Time leftT = (Time)left;
            Time rightT = (Time)right;
            if (leftT.getPartial().size() != rightT.getPartial().size()) { return false; }

            for (int i = 0; i < leftT.getPartial().size(); ++i) {
                if (leftT.getPartial().getValue(i) != rightT.getPartial().getValue(i)) {
                    return false;
                }
            }
            return true;
        }

        return EqualEvaluator.equal(left, right);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return equivalent(left, right);
    }
}
