package org.opencds.cqf.cql.elm.execution;

import org.joda.time.*;
import org.joda.time.field.MillisDurationField;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.opencds.cqf.cql.runtime.Uncertainty.resolveUncertaintyWithFunction;

// for Uncertainty

/*
duration between(low DateTime, high DateTime) Integer
duration between(low Time, high Time) Integer

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
public class DurationBetweenEvaluator extends org.cqframework.cql.elm.execution.DurationBetween {

    public static Integer between(org.joda.time.DateTime leftDateTime, org.joda.time.DateTime rightDateTime, int idx) {
        Integer ret = 0;
        switch(idx) {
            case 0: ret = Years.yearsBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getYears();
                break;
            case 1: ret = Months.monthsBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getMonths();
                break;
            case 2: ret = Days.daysBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getDays();
                break;
            case 3: ret = Hours.hoursBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getHours();
                break;
            case 4: ret = Minutes.minutesBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getMinutes();
                break;
            case 5: ret = Seconds.secondsBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getSeconds();
                break;
            case 6:
                if (rightDateTime.getYear() == leftDateTime.getYear()
                        && rightDateTime.getMonthOfYear() == leftDateTime.getMonthOfYear()
                        && rightDateTime.getDayOfWeek() == leftDateTime.getDayOfWeek()
                        && rightDateTime.getHourOfDay() == leftDateTime.getHourOfDay()
                        && rightDateTime.getMinuteOfHour() == leftDateTime.getMinuteOfHour()
                        && rightDateTime.getSecondOfMinute() == leftDateTime.getSecondOfMinute())
                {
                    ret = rightDateTime.getMillisOfSecond() - leftDateTime.getMillisOfSecond();;
                }
                else {
                    ret = Seconds.secondsBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getSeconds() * 1000;
                }
                break;
            case 7: ret = Days.daysBetween(leftDateTime.toInstant(), rightDateTime.toInstant()).getDays() / 7;
                break;
        }
        return ret;
    }

    public static Integer between(BaseTemporal left, BaseTemporal right, int idx, boolean dt) {
        return between(left.getJodaDateTime(), right.getJodaDateTime(), idx);
    }

    public static Object durationBetween(Object left, Object right, String precision) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        if (left == null || right == null) {
            return null;
        }

        boolean isDateTime = left instanceof DateTime;

        BaseTemporal leftTemporal = (BaseTemporal) left;
        BaseTemporal rightTemporal = (BaseTemporal) right;

        int index = isDateTime ? DateTime.getFieldIndex(precision) : Time.getFieldIndex(precision);

        if (index != -1) {
            boolean weeks = false;
            if (index == 7) {
                index = 2;
                weeks = true;
            }

            if (Uncertainty.isUncertain(leftTemporal, precision) || Uncertainty.isUncertain(rightTemporal, precision)) {
                DurationBetweenEvaluator evaluator = new DurationBetweenEvaluator();
                Method method = evaluator.getClass().getMethod("between", BaseTemporal.class, BaseTemporal.class, int.class, boolean.class);
                return resolveUncertaintyWithFunction(leftTemporal, rightTemporal, precision, evaluator, method, weeks ? 7 : index);
            }

            if (weeks) {
                index = 7;
            }

            if (!isDateTime) {
                index += 3;
            }

            return between(leftTemporal.getJodaDateTime(), rightTemporal.getJodaDateTime(), index);
        }

        else {
            throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
        }

//    if (left == null || right == null) {
//      return null;
//    }
//
//    if (precision == null) {
//      throw new IllegalArgumentException("Precision must be specified.");
//    }
//
//    if (left instanceof DateTime && right instanceof DateTime) {
//      DateTime leftDT = (DateTime)left;
//      DateTime rightDT = (DateTime)right;
//
//      int idx = DateTime.getFieldIndex(precision);
//
//      if (idx != -1) {
//
//        // Uncertainty
//        if (Uncertainty.isUncertain(leftDT, precision)) {
//          precision = DateTime.getUnit(rightDT.getPartial().size() - 1);
//          ArrayList<DateTime> highLow = Uncertainty.getHighLowList(leftDT, precision);
//          return new Uncertainty().withUncertaintyInterval(new Interval(between(highLow.get(1), rightDT, idx), true, between(highLow.get(0), rightDT, idx), true));
//        }
//
//        else if (Uncertainty.isUncertain(rightDT, precision)) {
//          precision = DateTime.getUnit(leftDT.getPartial().size() - 1);
//          ArrayList<DateTime> highLow = Uncertainty.getHighLowList(rightDT, precision);
//          return new Uncertainty().withUncertaintyInterval(new Interval(between(leftDT, highLow.get(0), idx), true, between(leftDT, highLow.get(1), idx), true));
//        }
//
//        else if (leftDT.getPartial().size() > rightDT.getPartial().size()) {
//          // each partial must have same number of fields - expand rightDT
//          rightDT = DateTime.expandPartialMin(rightDT, leftDT.getPartial().size());
//        }
//
//        else if (rightDT.getPartial().size() > leftDT.getPartial().size()) {
//          // each partial must have same number of fields - expand leftDT
//          leftDT = DateTime.expandPartialMin(leftDT, rightDT.getPartial().size());
//        }
//
//        return between(leftDT, rightDT, idx);
//      }
//
//      else {
//        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
//      }
//    }
//
//    if (left instanceof Time && right instanceof Time) {
//      Time leftT = (Time)left;
//      Time rightT = (Time)right;
//
//      int idx = Time.getFieldIndex(precision);
//
//      if (idx != -1) {
//
//        // Uncertainty
//        if (Uncertainty.isUncertain(leftT, precision)) {
//          precision = Time.getUnit(rightT.getPartial().size() - 1);
//          ArrayList<Time> highLow = Uncertainty.getHighLowList(leftT, precision);
//          return new Uncertainty().withUncertaintyInterval(new Interval(between(highLow.get(1), rightT, idx), true, between(highLow.get(0), rightT, idx), true));
//        }
//
//        else if (Uncertainty.isUncertain(rightT, precision)) {
//          precision = Time.getUnit(leftT.getPartial().size() - 1);
//          ArrayList<Time> highLow = Uncertainty.getHighLowList(rightT, precision);
//          return new Uncertainty().withUncertaintyInterval(new Interval(between(leftT, highLow.get(0), idx), true, between(leftT, highLow.get(1), idx), true));
//        }
//
//        else if (leftT.getPartial().size() > rightT.getPartial().size()) {
//          // each partial must have same number of fields - expand rightDT
//          rightT = Time.expandPartialMin(rightT, leftT.getPartial().size());
//        }
//
//        else if (rightT.getPartial().size() > leftT.getPartial().size()) {
//          // each partial must have same number of fields - expand leftDT
//          leftT = Time.expandPartialMin(leftT, rightT.getPartial().size());
//        }
//
//        return between(leftT, rightT, idx);
//      }
//
//      else {
//        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
//      }
//    }
//
//    throw new IllegalArgumentException(String.format("Cannot DurationBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision().value();

        try {
            return context.logTrace(this.getClass(), durationBetween(left, right, precision), left, right);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
