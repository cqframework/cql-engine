package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
includes(left Interval<T>, right Interval<T>) Boolean

The includes operator for intervals returns true if the first interval completely includes the second.
  More precisely, if the starting point of the first interval is less than or equal to the starting point of the second interval,
	and the ending point of the first interval is greater than or equal to the ending point of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
includes(left List<T>, right List<T>) Boolean

The includes operator for lists returns true if the first list contains every element of the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IncludesEvaluator extends org.cqframework.cql.elm.execution.Includes {

  public static Object includes(Object left, Object right) {

    if (left == null) {
      return false;
    }

    if (right == null) {
      return true;
    }

    if (left instanceof Interval) {
      Object leftStart = ((Interval)left).getStart();
      Object leftEnd = ((Interval)left).getEnd();
      Object rightStart = ((Interval)right).getStart();
      Object rightEnd = ((Interval)right).getEnd();

      if (leftStart == null || leftEnd == null
              || rightStart == null || rightEnd == null) {
        return null;
      }

      return (LessOrEqualEvaluator.lessOrEqual(leftStart, rightStart)
              && GreaterOrEqualEvaluator.greaterOrEqual(leftEnd, rightEnd));
    }

    else if (left instanceof Iterable) {
      for (Object rightElement : (Iterable)right) {
        Object in = InEvaluator.in(rightElement, (Iterable)left);

        if (in == null) continue;

        if (!(Boolean) in) {
          return false;
        }
      }
      return true;
    }

    throw new IllegalArgumentException(String.format("Cannot Includes arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

  @Override
  public Object evaluate(Context context) {
	Object left = getOperand().get(0).evaluate(context);
	Object right = getOperand().get(1).evaluate(context);

	return includes(left, right);
  }
}
