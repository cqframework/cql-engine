package org.opencds.cqf.cql.engine.runtime;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;



import org.opencds.cqf.cql.engine.elm.execution.AndEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.IntersectEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.MaxValueEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.MinValueEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.PredecessorEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.SubtractEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.SuccessorEvaluator;
import org.opencds.cqf.cql.engine.exception.InvalidInterval;
import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;

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
            throw new InvalidInterval("Low or high boundary of an interval must be present.");
        }

        if (this.high != null && this.high.getClass() != pointType) {
            throw new InvalidInterval("Low and high boundary values of an interval must be of the same type.");
        }

        // Special case for measure processing - MeasurementPeriod is a java date
        if (low instanceof Date && high instanceof Date) {
            if (((Date) low).after((Date) high)) {
                throw new InvalidInterval("Invalid Interval - the ending boundary must be greater than or equal to the starting boundary.");
            }
        }

        else if (low != null && high != null ) {
            Boolean isStartGreater = GreaterEvaluator.greater(getStart(), getEnd());
            if( isStartGreater == null || isStartGreater.equals(Boolean.TRUE) ) {
                throw new InvalidInterval("Invalid Interval - the ending boundary must be greater than or equal to the starting boundary.");
            }
        }
    }

    public static Object getSize(Object start, Object end) {
        if (start == null || end == null) {
            return null;
        }

        if (start instanceof Integer || start instanceof BigDecimal || start instanceof Quantity) {
            return SubtractEvaluator.subtract(end, start);
        }

        throw new InvalidOperatorArgument(String.format("Cannot perform width operator with argument of type '%s'.", start.getClass().getName()));
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
    public Type getPointType() {
        return pointType;
    }

    private boolean uncertain = false;
    public boolean isUncertain() {
        return uncertain;
    }
    public Interval setUncertain(boolean uncertain) {
        this.uncertain = uncertain;
        return this;
    }

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
            return SuccessorEvaluator.successor(low);
        }
        else {
            return low == null ? MinValueEvaluator.minValue(pointType.getTypeName()) : low;
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
            return PredecessorEvaluator.predecessor(high);
        }
        else {
            return high == null ? MaxValueEvaluator.maxValue(pointType.getTypeName()) : high;
        }
    }

    @Override
    public int compareTo(Interval other) {
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
        if (other instanceof Interval) {
            if (isUncertain()) {
                if (IntersectEvaluator.intersect(this, other) != null) {
                    return null;
                }
            }

            Interval otherInterval = (Interval) other;
            return AndEvaluator.and(
                    EqualEvaluator.equal(this.getStart(), otherInterval.getStart()),
                    EqualEvaluator.equal(this.getEnd(), otherInterval.getEnd())
            );

        }

        if (other instanceof Integer) {
            return equal(new Interval(other, true, other, true));
        }

        throw new InvalidOperatorArgument(String.format("Cannot perform equal operation on types: '%s' and '%s'", this.getClass().getName(), other.getClass().getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Interval ? equivalent(other) : false;
    }

    @Override
    public int hashCode() {
        return (31 * (lowClosed ? 1 : 0))
            + (47 * (highClosed ? 1 : 0))
            + (13 * (low != null ? low.hashCode() : 0))
            + (89 * (high != null ? high.hashCode() : 0));
    }

    @Override
    public String toString() {
        return String.format("Interval%s%s, %s%s",
                getLowClosed() ? "[" : "(",
                getLow() == null ? "null" : getLow().toString(),
                getHigh() == null ? "null" : getHigh().toString(),
                getHighClosed() ? "]" : ")"
        );
    }
}
