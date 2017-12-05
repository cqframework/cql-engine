package org.opencds.cqf.cql.runtime;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.joda.time.chrono.ISOChronology;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.elm.execution.LessEvaluator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Christopher Schuler on 6/11/2017.
 */
public abstract class BaseTemporal {

    protected Partial partial;
    BigDecimal timezoneOffset;
    ISOChronology chronology;
    org.joda.time.DateTime jodaDateTime;

    public Partial getPartial() {
        return partial;
    }

    public BigDecimal getTimezoneOffset() {
        if (timezoneOffset == null) {
            timezoneOffset = new BigDecimal("0.0");
        }
        return timezoneOffset;
    }

    public Integer getOffsetMillis() {
        return timezoneOffset.multiply(new BigDecimal("3600000.0")).intValue();
    }

    public void setTimezoneOffset(BigDecimal newTimezoneOffset) {
        timezoneOffset = newTimezoneOffset;
        chronology = ISOChronology.getInstance(DateTimeZone.forOffsetMillis(getOffsetMillis()));
    }
    public ISOChronology getChronology() {
        return this.chronology;
    }

    public org.joda.time.DateTime toJodaDateTime() {
        if (chronology == null) {
            return getPartial().toDateTime(new org.joda.time.DateTime());
        }
        return getPartial().toDateTime(new org.joda.time.DateTime().withChronology(getChronology()));
    }

    public static Boolean formatCheck(ArrayList<Object> timeElements) {
        boolean prevNull = false;
        for (Object element : timeElements) {
            if (element == null) { prevNull = true; }
            else if (prevNull) {
                return false;
            }
        }
        return true;
    }

    public static int[] getValues(Integer... values) {
        int count = 0;
        int[] temp = new int[7];
        for (Integer value : values) {
            if (value != null) {
                temp[count] = value;
                ++count;
            }
        }
        return Arrays.copyOf(temp, count);
    }

    public org.joda.time.DateTime getJodaDateTime() {
        return jodaDateTime;
    }

    public Integer compareTo(BaseTemporal other) {
        int size;

        // Uncertainty detection
        if (this.getPartial().size() != other.getPartial().size()) {
            size = this.getPartial().size() > other.getPartial().size() ? other.getPartial().size() : this.getPartial().size();
        }
        else { size = this.getPartial().size(); }

        for (int i = 0; i < size; ++i) {
            Object left = this.getPartial().getValue(i);
            Object right = other.getPartial().getValue(i);
            if (GreaterEvaluator.greater(left, right)) { return 1; }
            else if (LessEvaluator.less(left, right)) { return -1; }
        }
        // Uncertainty wrinkle
        if (this.getPartial().size() != other.getPartial().size()) { return null; }
        return 0;
    }

    public static Partial truncatePartial(BaseTemporal temporal, int index) {
        boolean isDateTime = temporal instanceof DateTime;
        int [] a = new int[index + 1];

        for (int i = 0; i < index + 1; ++i) {
            a[i] = temporal.getPartial().getValue(i);
        }

        return isDateTime ? new Partial(DateTime.getFields(index + 1), a)
                        : new Partial(Time.getFields(index + 1), a);
    }

    @Override
    public String toString() {
        return this.getPartial().toString();
    }
}
