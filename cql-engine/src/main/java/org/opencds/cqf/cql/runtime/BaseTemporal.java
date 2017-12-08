package org.opencds.cqf.cql.runtime;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.elm.execution.LessEvaluator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * Created by Christopher Schuler on 6/11/2017.
 */
public abstract class BaseTemporal {

    protected Partial partial;
    DateTimeZone timezone;
    org.joda.time.DateTime jodaDateTime;
    boolean isDateTime;

    public Partial getPartial() {
        return partial;
    }

    public void setPartial(Partial partial) {
        this.partial = partial;
        jodaDateTime = dateTimeFromPartial(partial, timezone);
    }

    public boolean getIsDateTime() {
        return this.isDateTime;
    }

    public org.joda.time.DateTime dateTimeFromPartial(Partial partial, DateTimeZone timezone) {
        org.joda.time.DateTime dt = new org.joda.time.DateTime(timezone);

        if (!isDateTime) {
            dt.withDate(1, 1, 1);
            switch (partial.size()) {
                case 1: return dt.withField(DateTimeFieldType.hourOfDay(), partial.getValue(0))
                        .withField(DateTimeFieldType.minuteOfHour(), 0)
                        .withField(DateTimeFieldType.secondOfMinute(), 0)
                        .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 2: return dt.withField(DateTimeFieldType.hourOfDay(), partial.getValue(0))
                        .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(1))
                        .withField(DateTimeFieldType.secondOfMinute(), 0)
                        .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 3: return dt.withField(DateTimeFieldType.hourOfDay(), partial.getValue(0))
                        .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(1))
                        .withField(DateTimeFieldType.secondOfMinute(), partial.getValue(2))
                        .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 4: return dt.withField(DateTimeFieldType.hourOfDay(), partial.getValue(0))
                        .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(1))
                        .withField(DateTimeFieldType.secondOfMinute(), partial.getValue(2))
                        .withField(DateTimeFieldType.millisOfSecond(), partial.getValue(3));
            }
        }

        else {
            switch (partial.size()) {
                case 1: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), 1)
                            .withField(DateTimeFieldType.dayOfMonth(), 1)
                            .withField(DateTimeFieldType.hourOfDay(), 0)
                            .withField(DateTimeFieldType.minuteOfHour(), 0)
                            .withField(DateTimeFieldType.secondOfMinute(), 0)
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 2: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), 1)
                            .withField(DateTimeFieldType.hourOfDay(), 0)
                            .withField(DateTimeFieldType.minuteOfHour(), 0)
                            .withField(DateTimeFieldType.secondOfMinute(), 0)
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 3: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), partial.getValue(2))
                            .withField(DateTimeFieldType.hourOfDay(), 0)
                            .withField(DateTimeFieldType.minuteOfHour(), 0)
                            .withField(DateTimeFieldType.secondOfMinute(), 0)
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 4: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), partial.getValue(2))
                            .withField(DateTimeFieldType.hourOfDay(), partial.getValue(3))
                            .withField(DateTimeFieldType.minuteOfHour(), 0)
                            .withField(DateTimeFieldType.secondOfMinute(), 0)
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 5: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), partial.getValue(2))
                            .withField(DateTimeFieldType.hourOfDay(), partial.getValue(3))
                            .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(4))
                            .withField(DateTimeFieldType.secondOfMinute(), 0)
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 6: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), partial.getValue(2))
                            .withField(DateTimeFieldType.hourOfDay(), partial.getValue(3))
                            .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(4))
                            .withField(DateTimeFieldType.secondOfMinute(), partial.getValue(5))
                            .withField(DateTimeFieldType.millisOfSecond(), 0);
                case 7: return dt.withField(DateTimeFieldType.year(), partial.getValue(0))
                            .withField(DateTimeFieldType.monthOfYear(), partial.getValue(1))
                            .withField(DateTimeFieldType.dayOfMonth(), partial.getValue(2))
                            .withField(DateTimeFieldType.hourOfDay(), partial.getValue(3))
                            .withField(DateTimeFieldType.minuteOfHour(), partial.getValue(4))
                            .withField(DateTimeFieldType.secondOfMinute(), partial.getValue(5))
                            .withField(DateTimeFieldType.millisOfSecond(), partial.getValue(6));
            }
        }
        return dt;
    }

    public BigDecimal getTimezoneOffset() {
        if (timezone.getID().equals("UTC")) {
            return new BigDecimal("0.0");
        }
        String[] parts = timezone.getID().split(":");
        if (Integer.parseInt(parts[1]) == 0) {
            return new BigDecimal(parts[0] + "." + parts[1]);
        }
        String minuteOffset = Integer.toString(60/Integer.parseInt(parts[1]));
        return new BigDecimal(parts[0] + "." + minuteOffset);
    }

    public DateTimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(DateTimeZone timezone) {
        this.timezone = timezone;
    }

    public org.joda.time.DateTime getJodaDateTime() {
        return jodaDateTime;
    }

    public Integer compareTo(BaseTemporal other) {
        boolean differentPrecisions = this.getPartial().size() != other.getPartial().size();

        int size;
        if (differentPrecisions) {
            size = this.getPartial().size() > other.getPartial().size() ? other.getPartial().size() : this.getPartial().size();
        }
        else {
            size = this.getPartial().size();
        }

        if (!isDateTime) {
            size += 3;
        }

        Instant left = this.jodaDateTime.toInstant();
        Instant right = other.jodaDateTime.toInstant();

        for (int i = 0; i < size; ++i) {
            if (left.get(DateTime.getField(i)) > right.get(DateTime.getField(i))) {
                return 1;
            }
            else if (left.get(DateTime.getField(i)) < right.get(DateTime.getField(i))) {
                return -1;
            }
        }

        if (differentPrecisions) {
            return null;
        }

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

    public static DateTimeZone resolveDateTimeZone(BigDecimal offset) {
        if (offset == null) {
            return DateTimeZone.forOffsetMillis(TimeZone.getDefault().getRawOffset());
        }
        else {
            int minuteOffset = new BigDecimal("60").multiply(offset.remainder(BigDecimal.ONE)).intValue();
            return DateTimeZone.forOffsetHoursMinutes(offset.intValue(), minuteOffset);
        }
    }

    @Override
    public String toString() {
        return this.getPartial().toString();
    }
}
