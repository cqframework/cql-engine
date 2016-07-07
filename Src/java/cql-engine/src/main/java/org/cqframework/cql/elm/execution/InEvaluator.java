package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;
import org.cqframework.cql.runtime.DateTime;

/*
*** NOTES FOR INTERVAL ***
in(point T, argument Interval<T>) Boolean
The in operator for intervals returns true if the given point is greater than or equal to the starting point of the interval,
  and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.
*/

/*
*** NOTES FOR LIST ***
in(element T, argument List<T>) Boolean
The in operator for lists returns true if the given element is in the given list.
This operator uses the notion of equivalence to determine whether or not the element being searched for is equivalent to any element in the list.
  In particular this means that if the list contains a null, and the element being searched for is null, the result will be true.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class InEvaluator extends In {

  public static Boolean in(Object testElement, Iterable<? extends Object> list) {
    if (list == null) {
        return null;
    }

    boolean nullSwitch = false;
    for (Object element : list) {
      Boolean equiv = Value.equivalent(testElement, element);
      if (equiv == null) { nullSwitch = true; }
      else if (equiv) { return true; }
    }

    if (nullSwitch) { return null; }
    return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (right == null) { return null; }

    if (right instanceof Interval) {
      if (left == null) { return null; }
      Object rightStart = ((Interval)right).getStart();
      Object rightEnd = ((Interval)right).getEnd();

      if (rightStart == null && ((Interval)right).getLowClosed()) { return true; }
      else if (rightEnd == null && ((Interval)right).getHighClosed()) { return true; }
      else if (rightStart == null || rightEnd == null) { return null; }

      return (Value.compareTo(left, rightStart, ">=") && Value.compareTo(left, rightEnd, "<="));
    }

    else if (right instanceof Iterable) {
      return in(left, (Iterable<Object>)right);
    }
    throw new IllegalArgumentException(String.format("Cannot In arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }
}
