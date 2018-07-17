package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Interval implements CqlType, Comparable<Interval> {

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
        if (low instanceof Date && high instanceof Date) {
            if (GreaterEvaluator.greater(DateTime.fromJavaDate((Date) getStart()), DateTime.fromJavaDate((Date) getEnd())))
            {
                throw new RuntimeException("Invalid Interval - the ending boundary must be greater than or equal to the starting boundary.");
            }
        }

        else if (low != null && high != null && GreaterEvaluator.greater(getStart(), getEnd())) {
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
                .withValue(new BigDecimal(DurationBetweenEvaluator.between(((DateTime)start).getJodaDateTime(), ((DateTime)end).getJodaDateTime(), ((DateTime)start).getPartial().size() - 1)))
                .withUnit(DateTime.getUnit(((DateTime)start).getPartial().size() - 1));
        }

        else if (start instanceof Time) {
            return new Quantity()
                .withValue(new BigDecimal(DurationBetweenEvaluator.between(((Time)start).getJodaDateTime(), ((Time)end).getJodaDateTime(), ((Time)start).getPartial().size() + 2)))
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
    public int compareTo(@Nonnull Interval other) {
        CqlList cqlList = new CqlList();
        if (cqlList.compareTo(getStart(), other.getStart()) == 0) {
            return cqlList.compareTo(getEnd(), other.getEnd());
        }
        return cqlList.compareTo(getStart(), other.getStart());
    }

    @Override
    public Boolean equivalent(Object other) {
        return EquivalentEvaluator.equivalent(this.getStart(), ((Interval) other).getStart())
                && EquivalentEvaluator.equivalent(this.getEnd(), ((Interval) other).getEnd());
    }

    @Override
    public Boolean equal(Object other) {
        return this.getLow() != null && EqualEvaluator.equal(this.getStart(), ((Interval) other).getStart())
                && this.getLowClosed() == ((Interval) other).getLowClosed()
                && this.getHigh() != null && EqualEvaluator.equal(this.getEnd(), ((Interval) other).getEnd())
                && this.getHighClosed() == ((Interval) other).getHighClosed();
    }

    @Override
    public String toString() {
        if (getStart() == null) {

        }
        return String.format("Interval%s%s, %s%s",
                getLowClosed() ? "[" : "(",
                getStart() == null ? "null" : getStart().toString(),
                getEnd() == null ? "null" : getEnd().toString(),
                getHighClosed() ? "]" : ")"
        );
    }
}
