package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;
import org.cqframework.cql.runtime.DateTime;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import java.util.Arrays;
import java.util.ArrayList;

/**
* Created by Chris Schuler on 6/7/2016
*/
public class AfterEvaluator extends After {

  @Override
  public Object evaluate(Context context) {
    Object testLeft = getOperand().get(0).evaluate(context);
    Object testRight = getOperand().get(1).evaluate(context);

    if (testLeft == null || testRight == null) { return null; }

    // (Interval, Interval)
    if (testLeft instanceof Interval && testRight instanceof Interval) {
      Interval leftInterval = (Interval)testLeft;
      Interval rightInterval = (Interval)testRight;
      Object left = leftInterval.getStart();
      Object right = rightInterval.getEnd();

      return Value.compareTo(left, right, ">");
    }

    // (Interval, Point)
    else if (testLeft instanceof Interval && !(testRight instanceof Interval)) {
      Interval leftInterval = (Interval)testLeft;
      Object right = testRight;

      return Value.compareTo(leftInterval.getStart(), right, ">");
    }

    // (Point, Interval)
    else if (!(testLeft instanceof Interval) && testRight instanceof Interval) {
      Object left = testLeft;
      Interval rightInterval = (Interval)testRight;

      return Value.compareTo(left, rightInterval.getEnd(), ">");
    }

    // (DateTime, DateTime)
    else if (testLeft instanceof DateTime && testRight instanceof DateTime) {
      DateTime leftDT = (DateTime)testLeft;
      DateTime rightDT = (DateTime)testRight;
      String precision = getPrecision() == null ? null : getPrecision().value();

      if (precision == null) {
        throw new IllegalArgumentException("Precision must be specified.");
      }

      // DateTimePrecision Enum represents precision as Titlecase Strings
      ArrayList<String> indexes = new ArrayList<>(Arrays.asList("Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond"));
      int idx = indexes.indexOf(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > leftDT.getPartial().size() || idx + 1 > rightDT.getPartial().size()) {
          return null;
        }

        return leftDT.getPartial().getValue(idx) > rightDT.getPartial().getValue(idx);

        // for (int i = 0; i < idx + 1; ++i) {
        //   if (leftDT.getPartial().getValue(i) < rightDT.getPartial().getValue(i) && i != idx) {
        //     return false;
        //   }
        //   else if (i == idx) {
        //     return leftDT.getPartial().getValue(i) > rightDT.getPartial().getValue(i);
        //   }
        // }
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }

    throw new IllegalArgumentException(String.format("Cannot After arguments of type '%s' and '%s'.", testLeft.getClass().getName(), testRight.getClass().getName()));
  }
}
