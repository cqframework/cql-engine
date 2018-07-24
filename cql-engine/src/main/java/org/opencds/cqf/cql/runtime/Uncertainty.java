package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.elm.execution.LessEvaluator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.opencds.cqf.cql.elm.execution.DifferenceBetweenEvaluator.between;

/**
 * Created by Chris Schuler on 6/25/2016
 */
public class Uncertainty {

    private Interval uncertainty;

    public Interval getUncertaintyInterval() {
        return uncertainty;
    }

    public void setUncertaintyInterval(Interval uncertainty) {
        this.uncertainty = uncertainty;
    }

    public Uncertainty withUncertaintyInterval(Interval uncertainty) {
        setUncertaintyInterval(uncertainty);
        return this;
    }

    // Implicit conversion
    public static Interval toUncertainty(Object point) {
        return new Interval(point, true, point, true);
    }

    public static boolean isUncertain(DateTime dt, String precision) {
        try {
            if (precision.toLowerCase().equals("weeks") || precision.toLowerCase().equals("week")) {
                precision = "Day";
            }
            dt.getPartial().getValue(DateTime.getFieldIndex(precision));
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    public static boolean isUncertain(Time t, String precision) {
        try {
            t.getPartial().getValue(Time.getFieldIndex(precision));
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    public static boolean isUncertain(BaseTemporal bt, String precision) {
        return bt instanceof DateTime ? isUncertain((DateTime) bt, precision) : isUncertain((Time) bt, precision);
    }

    public static Interval resolveUncertaintyWithFunction(BaseTemporal leftTemporal, BaseTemporal rightTemporal,
                                                             String precision, Object theClass, Method method, int index)
            throws InvocationTargetException, IllegalAccessException
    {
        boolean isDateTime = leftTemporal instanceof DateTime;
        if (Uncertainty.isUncertain(leftTemporal, precision)) {
            if (isDateTime) {
                ArrayList<DateTime> highLow = Uncertainty.getHighLowList((DateTime) leftTemporal, precision);
                Object[] lowParams = {highLow.get(1), rightTemporal, index, true};
                Object[] highParams = {highLow.get(0), rightTemporal, index, true};
                return new Interval(
                        method.invoke(theClass, lowParams), true,
                        method.invoke(theClass, highParams), true
                ).setUncertain(true);
            }
            else {
                ArrayList<Time> highLow = Uncertainty.getHighLowList((Time) leftTemporal, precision);
                Object[] lowParams = {highLow.get(1), rightTemporal, index, false};
                Object[] highParams = {highLow.get(0), rightTemporal, index, false};
                return new Interval(
                        method.invoke(theClass, lowParams), true,
                        method.invoke(theClass, highParams), true
                ).setUncertain(true);
            }
        }

        else if (Uncertainty.isUncertain(rightTemporal, precision)) {
            if (isDateTime) {
                ArrayList<DateTime> highLow = Uncertainty.getHighLowList((DateTime) rightTemporal, precision);
                Object[] lowParams = {leftTemporal, highLow.get(0), index, true};
                Object[] highParams = {leftTemporal, highLow.get(1), index, true};
                return new Interval(
                        method.invoke(theClass, lowParams), true,
                        method.invoke(theClass, highParams), true
                ).setUncertain(true);
            }
            else {
                ArrayList<Time> highLow = Uncertainty.getHighLowList((Time) rightTemporal, precision);
                Object[] lowParams = {leftTemporal, highLow.get(0), index, false};
                Object[] highParams = {leftTemporal, highLow.get(1), index, false};
                return new Interval(
                        method.invoke(theClass, lowParams), true,
                        method.invoke(theClass, highParams), true
                ).setUncertain(true);
            }
        }
        return null;
    }

    /**
     This method's purpose is to return a list of DateTimes with max and min values
     For example:
     DateTime(2012) where precision is days
     Would result in the following DateTimes being returned:
     low = (2012, 1, 1)
     high = (2012, 12, 31)
     The uncertainty interval can then be constructed by running the high and low DateTimes
     through the operation that called this method.
     So, the following expression:
     days between DateTime(2012) and DateTime(2013, 10, 15)
     would result in evaluating
     days between DateTime(2012, 12, 31) and DateTime(2013, 10, 15) -- for the low point of the interval
     and
     days between DateTime(2012, 1, 1) and DateTime(2013, 10, 15) -- for the high point of the interval
     */
    public static Object getHighLowList(BaseTemporal uncertain, String precision) {
        if (uncertain instanceof DateTime) {
            return getHighLowList((DateTime) uncertain, precision);
        }
        return getHighLowList((Time) uncertain, precision);
    }

    public static ArrayList<DateTime> getHighLowList(DateTime uncertain, String precision) {
        if (isUncertain(uncertain, precision)) {
            DateTime low = new DateTime(uncertain.getPartial(), uncertain.getTimezone());
            DateTime high = new DateTime(uncertain.getPartial(), uncertain.getTimezone());

            int idx = DateTime.getFieldIndex(precision);
            if (idx == 7) {
                idx = 2;
            }
            if (idx == -1) { idx = DateTime.getFieldIndex(precision); }
            if (idx != -1) {
                // expand the high and low date times with respective max and min values
                return new ArrayList<>(Arrays.asList(DateTime.expandPartialMin(low, idx + 1), DateTime.expandPartialMax(high, idx + 1, high.getPartial().size())));
            }

            else {
                throw new IllegalArgumentException(String.format("Invalid duration unit: %s", precision));
            }
        }

        throw new IllegalArgumentException("Specified DateTime is not uncertain.");
    }

    public static ArrayList<Time> getHighLowList(Time uncertain, String precision) {
        if (isUncertain(uncertain, precision)) {
            Time low = new Time(uncertain.getPartial(), uncertain.getTimezone());
            Time high = new Time(uncertain.getPartial(), uncertain.getTimezone());

            int idx = Time.getFieldIndex(precision);
            if (idx == -1) { idx = Time.getFieldIndex(precision); }
            if (idx != -1) {
                // expand the high and low times with respective max and min values
                for (int i = uncertain.getPartial().size(); i < idx + 1; ++i) {
                    low.setPartial(low.getPartial().with(Time.getField(i), Time.getField(i).getField(null).getMinimumValue()));
                    high.setPartial(high.getPartial().with(Time.getField(i), Time.getField(i).getField(null).getMaximumValue()));
                }
                return new ArrayList<>(Arrays.asList(low, high));
            }
            else {
                throw new IllegalArgumentException(String.format("Invalid duration unit: %s", precision));
            }
        }

        throw new IllegalArgumentException("Specified Time is not uncertain.");
    }

    public static ArrayList<Interval> getLeftRightIntervals(Object left, Object right) {

        Interval leftU;
        Interval rightU;

        if (left instanceof Uncertainty && right instanceof Uncertainty) {
            leftU = ((Uncertainty)left).getUncertaintyInterval();
            rightU = ((Uncertainty)right).getUncertaintyInterval();
        }
        else if (left instanceof Uncertainty) {
            leftU = ((Uncertainty)left).getUncertaintyInterval();
            rightU = Uncertainty.toUncertainty(right);
        }
        else {
            leftU = Uncertainty.toUncertainty(left);
            rightU = ((Uncertainty)right).getUncertaintyInterval();
        }
        return new ArrayList<>(Arrays.asList(leftU, rightU));
    }

    public Boolean equal(Object other) {
        ArrayList<Interval> intervals = Uncertainty.getLeftRightIntervals(this, other);
        Interval leftU = intervals.get(0);
        Interval rightU = intervals.get(1);

        if (LessEvaluator.less(leftU.getEnd(), rightU.getStart())) {
            return false;
        }
        if (GreaterEvaluator.greater(leftU.getStart(), rightU.getEnd())) {
            return false;
        }

        return null;
    }

    @Override
    public String toString() {
        return getUncertaintyInterval().toString();
    }
}
