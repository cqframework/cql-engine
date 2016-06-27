package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;
import org.cqframework.cql.runtime.DateTime;

/*
*** NOTES FOR INTERVAL ***
The before operator for intervals returns true if the first interval ends before the second one starts.
  In other words, if the ending point of the first interval is less than the starting point of the second interval.
For the point-interval overload, the operator returns true if the given point is less than the start of the interval.
For the interval-point overload, the operator returns true if the given interval ends before the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
The before-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the before the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
* Created by Chris Schuler on 6/7/2016 (v1), 6/26/2016 (v2)
*/
public class BeforeEvaluator extends Before {

  @Override
  public Object evaluate(Context context) {
    Object testLeft = getOperand().get(0).evaluate(context);
    Object testRight = getOperand().get(1).evaluate(context);

    if (testLeft == null || testRight == null) { return null; }

    if (testLeft instanceof Interval && testRight instanceof Interval) {
      Interval leftInterval = (Interval)getOperand().get(0).evaluate(context);
      Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

      if (leftInterval != null && rightInterval != null) {
        Object left = leftInterval.getStart();
        Object right = rightInterval.getEnd();

        return Value.compareTo(left, right, "<");
      }
    }

    else if (testLeft instanceof Interval && !(testRight instanceof Interval)) {
      Interval leftInterval = (Interval)testLeft;
      Object right = testRight;

      return Value.compareTo(leftInterval.getEnd(), right, "<");
    }

    else if (!(testLeft instanceof Interval) && testRight instanceof Interval) {
      Object left = testLeft;
      Interval rightInterval = (Interval)testRight;

      return Value.compareTo(left, rightInterval.getStart(), "<");
    }

    // (DateTime, DateTime)
    else if (testLeft instanceof DateTime && testRight instanceof DateTime) {
      DateTime leftDT = (DateTime)testLeft;
      DateTime rightDT = (DateTime)testRight;
      String precision = getPrecision() == null ? null : getPrecision().value();

      if (precision == null) {
        throw new IllegalArgumentException("Precision must be specified.");
      }

      int idx = DateTime.getFieldIndex(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > leftDT.getPartial().size() || idx + 1 > rightDT.getPartial().size()) {

          // TODO: implement uncertainty

          return null;
        }

        return leftDT.getPartial().getValue(idx) < rightDT.getPartial().getValue(idx);

        // for (int i = 0; i < idx + 1; ++i) {
        //   if (leftDT.getPartial().getValue(i) > rightDT.getPartial().getValue(i) && i != idx) {
        //     return true;
        //   }
        // }
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }

    // Implement for Time

    throw new IllegalArgumentException(String.format("Cannot Before arguments of type '%s' and '%s'.", testLeft.getClass().getName(), testRight.getClass().getName()));
  }
}
