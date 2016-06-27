package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

// for Uncertainty
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Uncertainty;

import java.util.Arrays;
import java.util.ArrayList;

import org.joda.time.*;

/*
The duration-between operator returns the number of whole calendar periods for the specified precision between
  the first and second arguments.
If the first argument is after the second argument, the result is negative.
The result of this operation is always an integer; any fractional periods are dropped.
For DateTime values, duration must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, duration must be one of: hours, minutes, seconds, or milliseconds.
If either argument is null, the result is null.

Additional Complexity: precison elements above the specified precision must also be accounted.
For example:
days between DateTime(2012, 5, 5) and DateTime(2011, 5, 0) = 365 + 5 = 370 days
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DurationBetweenEvaluator extends DurationBetween {

  public static Integer between(DateTime leftDT, DateTime rightDT, int idx) {
    Integer ret = 0;
    switch(idx) {
      case 0: ret = Years.yearsBetween(leftDT.getPartial(), rightDT.getPartial()).getYears();
              break;
      case 1: ret = Months.monthsBetween(leftDT.getPartial(), rightDT.getPartial()).getMonths();
              break;
      case 2: ret = Days.daysBetween(leftDT.getPartial(), rightDT.getPartial()).getDays();
              break;
      case 3: ret = Hours.hoursBetween(leftDT.getPartial(), rightDT.getPartial()).getHours();
              break;
      case 4: ret = Minutes.minutesBetween(leftDT.getPartial(), rightDT.getPartial()).getMinutes();
              break;
      case 5: ret = Seconds.secondsBetween(leftDT.getPartial(), rightDT.getPartial()).getSeconds();
              break;
      case 6: ret = Seconds.secondsBetween(leftDT.getPartial(), rightDT.getPartial()).getSeconds() * 1000;
              // now do the actual millisecond DurationBetween - add to ret
              ret += rightDT.getPartial().getValue(idx) - leftDT.getPartial().getValue(idx);
              break;
    }
    return ret;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);
    String precision = getPrecision().value();

    if (precision == null) {
      throw new IllegalArgumentException("Precision must be specified.");
    }

    if (left == null || right == null) { return null; }

    if (left instanceof DateTime && right instanceof DateTime) {
      DateTime leftDT = (DateTime)left;
      DateTime rightDT = (DateTime)right;

      int idx = DateTime.getFieldIndex(precision);

      if (idx != -1) {

        // Uncertainty
        if (Uncertainty.isUncertain(leftDT, precision)) {
          ArrayList<DateTime> highLow = Uncertainty.getHighLowList(leftDT, precision);
          return new Uncertainty().withUncertaintyInterval(new Interval(between(highLow.get(1), rightDT, idx), true, between(highLow.get(0), rightDT, idx), true));
        }

        else if (Uncertainty.isUncertain(rightDT, precision)) {
          ArrayList<DateTime> highLow = Uncertainty.getHighLowList(rightDT, precision);
          return new Uncertainty().withUncertaintyInterval(new Interval(between(leftDT, highLow.get(0), idx), true, between(leftDT, highLow.get(1), idx), true));
        }

        return between(leftDT, rightDT, idx);
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }

      // TODO: Implement for Time

    throw new IllegalArgumentException(String.format("Cannot DifferenceBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }
}
