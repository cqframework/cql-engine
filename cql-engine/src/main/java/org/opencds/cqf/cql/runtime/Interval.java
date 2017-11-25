package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.DurationBetweenEvaluator;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.elm.execution.SubtractEvaluator;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Interval {

    public Interval(Object low, boolean lowClosed, Object high, boolean highClosed) {
        this.low = low;
        this.lowClosed = lowClosed;
        this.high = high;
        this.highClosed = highClosed;

        if (this.low != null) {
            pointType = this.low.getClass();
        }
        else if (this.high != null) {
            pointType = this.high.getClass();
        }

        if (pointType == null) {
            throw new IllegalArgumentException("Low or high boundary of an interval must be present.");
        }

        if ((this.low != null && this.low.getClass() != pointType)
                || (this.high != null && this.high.getClass() != pointType)) {
            throw new IllegalArgumentException("Low and high boundary values of an interval must be of the same type.");
        }

        // Special case for measure processing - MeasurementPeriod is a java date
        if (low instanceof Date && high instanceof Date
                && GreaterEvaluator.greater(DateTime.fromJavaDate((Date) getStart()), DateTime.fromJavaDate((Date) getEnd())))
        {
            throw new RuntimeException("Invalid Interval - the ending boundary must be greater than or equal to the starting boundary.");
        }

        if (low != null && high != null && GreaterEvaluator.greater(getStart(), getEnd())) {
            throw new RuntimeException("Invalid Interval - the ending boundary must be greater than or equal to the starting boundary.");
        }
    }

    public static Object getSize(Object start, Object end) {
        if (start == null || end == null) {
            return null;
        }

        if (start instanceof Integer || start instanceof BigDecimal || start instanceof Quantity) {
            return SubtractEvaluator.subtract(end, start);
        }

        else if (start instanceof DateTime) {
            return new Quantity()
                .withValue(new BigDecimal(DurationBetweenEvaluator.between((DateTime)start, (DateTime)end, ((DateTime)start).getPartial().size() - 1)))
                .withUnit(DateTime.getUnit(((DateTime)start).getPartial().size() - 1));
        }

        else if (start instanceof Time) {
            return new Quantity()
                .withValue(new BigDecimal(DurationBetweenEvaluator.between((Time)start, (Time)end, ((Time)start).getPartial().size() - 1)))
                .withUnit(Time.getUnit(((Time)start).getPartial().size() - 1));
        }

        throw new IllegalArgumentException(String.format("Cannot getIntervalSize argument of type '%s'.", start.getClass().getName()));
    }

    private Object low;
    public Object getLow() {
        return low;
    }

    private boolean lowClosed;
    public boolean getLowClosed() {
        return lowClosed;
    }

    private Object high;
    public Object getHigh() {
        return high;
    }

    private boolean highClosed;
    public boolean getHighClosed() {
        return highClosed;
    }

    private Type pointType;

    /*
    Returns the starting point of the interval.

    If the low boundary of the interval is open, returns the Successor of the low value of the interval.
    Note that if the low value of the interval is null, the result is null.

    If the low boundary of the interval is closed and the low value of the interval is not null,
    returns the low value of the interval. Otherwise, the result is the minimum value of
    the point type of the interval.
     */
    public Object getStart() {
        if (!lowClosed) {
            return Value.successor(low);
        }
        else {
            return low == null ? Value.minValue(pointType) : low;
        }
    }

    public Boolean equal(Interval other) {
        return this.getLow() != null && EqualEvaluator.equal(this.getLow(), other.getLow())
                && this.getLowClosed() == other.getLowClosed()
                && this.getHigh() != null && EqualEvaluator.equal(this.getHigh(), other.getHigh())
                && this.getHighClosed() == other.getHighClosed();
    }

    /*
    Returns the ending point of an interval.

    If the high boundary of the interval is open, returns the Predecessor of the high value of the interval.
    Note that if the high value of the interval is null, the result is null.

    If the high boundary of the interval is closed and the high value of the interval is not null,
    returns the high value of the interval. Otherwise, the result is the maximum value of
    the point type of the interval.
     */
    public Object getEnd() {
        if (!highClosed) {
            return Value.predecessor(high);
        }
        else {
            return high == null ? Value.maxValue(pointType) : high;
        }
    }

    @Override
    public String toString() {
        return String.format("Interval [ %s, %s ]", getStart().toString(), getEnd().toString());
    }
}
