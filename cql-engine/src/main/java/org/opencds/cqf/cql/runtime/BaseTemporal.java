package org.opencds.cqf.cql.runtime;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.opencds.cqf.cql.elm.execution.SameAsEvaluator;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by Christopher Schuler on 6/11/2017.
 */
public abstract class BaseTemporal implements CqlType, Comparable<BaseTemporal> {

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

    public String getUtcOffsetString() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ZZ");
        return dtf.withZone(timezone).print(0);
    }

    public BigDecimal getTimezoneOffset() {
        String[] parts = getUtcOffsetString().split(":");
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

    public static String getHighestPrecision(BaseTemporal ... values) {
        int max = -1;
        for (BaseTemporal baseTemporal : values) {
            if (baseTemporal.partial.size() - 1 > max) {
                max = baseTemporal.partial.size() - 1;
            }
        }

        if (max == -1) {
            max = 6;
        }

        return values[0] instanceof DateTime ? DateTime.getUnit(max) : Time.getUnit(max);
    }

    public static String getLowestPrecision(BaseTemporal ... values) {
        int min = 999;
        for (BaseTemporal baseTemporal : values) {
            if (baseTemporal.partial.size() - 1 < min) {
                min = baseTemporal.partial.size() - 1;
            }
        }

        if (min == 999) {
            min = 0;
        }

        return values[0] instanceof DateTime ? DateTime.getUnit(min) : Time.getUnit(min);
    }

    public Integer compare(BaseTemporal other, Boolean forSort) {
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
            return forSort ? (this.getPartial().size() > other.getPartial().size() ? 1 : -1) : null;
        }

        return 0;
    }

    // for list sorting
    @Override
    public int compareTo(@Nonnull BaseTemporal other) {
        return this.compare(other, true);
    }

    @Override
    public Boolean equivalent(Object other) {
        if (this.getPartial().size() != ((BaseTemporal) other).getPartial().size()) {
            return false;
        }
        for (int i = 0; i < this.getPartial().size(); ++i) {
            if (this.getPartial().getValue(i) != ((BaseTemporal) other).getPartial().getValue(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean equal(Object other) {
        Boolean isSame = SameAsEvaluator.sameAs(this, other, getLowestPrecision(this, (BaseTemporal) other));
        if (isSame != null && isSame) {
            if (this.getPartial().size() != ((BaseTemporal) other).getPartial().size()) { // Uncertainty
                return null;
            }
        }
        return isSame;
    }

    @Override
    public String toString() {
        return this.getPartial().toString();
    }
}
