package org.opencds.cqf.cql.runtime;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class DateTime extends BaseTemporal {

    protected static final DateTimeFieldType[] fields = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth(),
            DateTimeFieldType.hourOfDay(),
            DateTimeFieldType.minuteOfHour(),
            DateTimeFieldType.secondOfMinute(),
            DateTimeFieldType.millisOfSecond(),
    };

    public static DateTimeFieldType[] getFields(int numFields) {
        DateTimeFieldType[] ret = new DateTimeFieldType[numFields];
        System.arraycopy(fields, 0, ret, 0, numFields);
        return ret;
    }

    public static DateTimeFieldType getField(int idx) {
        return fields[idx];
    }

    public static int getFieldIndex(String dateTimeElement) {
        dateTimeElement = dateTimeElement.toLowerCase();

        if (dateTimeElement.startsWith("year")) {
            return 0;
        }
        else if (dateTimeElement.startsWith("month")) {
            return 1;
        }
        else if (dateTimeElement.startsWith("day")) {
            return 2;
        }
        else if (dateTimeElement.startsWith("hour")) {
            return 3;
        }
        else if (dateTimeElement.startsWith("minute")) {
            return 4;
        }
        else if (dateTimeElement.startsWith("second")) {
            return 5;
        }
        else if (dateTimeElement.startsWith("millisecond")) {
            return 6;
        }
        else if (dateTimeElement.startsWith("week")) {
            return 7;
        }

        return -1;
    }

    public static String getUnit(int idx) {
        switch (idx) {
            case 0: return "years";
            case 1: return "months";
            case 2: return "days";
            case 3: return "hours";
            case 4: return "minutes";
            case 5: return "seconds";
            case 6: return "milliseconds";
        }
        throw new IllegalArgumentException("Invalid index for DateTime unit request.");
    }

    public org.joda.time.DateTime getDateTimePrecision(Partial partial) {
        switch (partial.size()) {
            case 0: return new org.joda.time.DateTime(partial.getChronology());
            case 1: return new org.joda.time.DateTime(partial.getValue(0), 1, 1, 0, 0, 0, 0, getChronology());
            case 2: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1),1, 0, 0, 0, 0, getChronology());
            case 3: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1), partial.getValue(2), 0, 0, 0, 0, getChronology());
            case 4: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1), partial.getValue(2), partial.getValue(3), 0, 0, 0, getChronology());
            case 5: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1), partial.getValue(2), partial.getValue(3), partial.getValue(4), 0, 0, getChronology());
            case 6: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1), partial.getValue(2), partial.getValue(3), partial.getValue(4), partial.getValue(5), 0, getChronology());
            case 7: return new org.joda.time.DateTime(partial.getValue(0), partial.getValue(1), partial.getValue(2), partial.getValue(3), partial.getValue(4), partial.getValue(5), partial.getValue(6), getChronology());
            default: throw new RuntimeException("Error creating Joda DateTime from Partial");
        }
    }

    public void setPartial(Partial partial) {
        this.jodaDateTime = getDateTimePrecision(partial);
        this.partial = partial;
    }

    public DateTime() {
        partial = new Partial();
        timezoneOffset = new BigDecimal(0);
        jodaDateTime = new org.joda.time.DateTime();
    }

    public DateTime(int year) {
        setPartial(new Partial(DateTimeFieldType.year(), year));
        jodaDateTime = new org.joda.time.DateTime(year, 1, 1, 0, 0, 0, 0);
    }

    public DateTime(int year, BigDecimal timezoneOffset) {
        setPartial(new Partial(DateTimeFieldType.year(), year));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, 1, 1, 0, 0, 0, 0, getChronology());
    }

    public DateTime(int year, int month) {
        setPartial(new Partial(getFields(2), getValues(year, month)));
        jodaDateTime = new org.joda.time.DateTime(year, month, 1, 0, 0, 0, 0);
    }

    public DateTime(int year, int month, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(2), getValues(year, month)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, 1, 0, 0, 0, 0, getChronology());
    }

    public DateTime(int year, int month, int day) {
        setPartial(new Partial(getFields(3), getValues(year, month, day)));
        jodaDateTime = new org.joda.time.DateTime(year, month, day, 0, 0, 0, 0);
    }

    public DateTime(int year, int month, int day, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(3), getValues(year, month, day)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, day, 0, 0, 0, 0, getChronology());
    }

    public DateTime(int year, int month, int day, int hour) {
        setPartial(new Partial(getFields(4), getValues(year, month, day, hour)));
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, 0, 0, 0);
    }

    public DateTime(int year, int month, int day, int hour, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(4), getValues(year, month, day, hour)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, 0, 0, 0, getChronology());
    }

    public DateTime(int year, int month, int day, int hour, int minute) {
        setPartial(new Partial(getFields(5), getValues(year, month, day, hour, minute)));
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, 0, 0);
    }

    public DateTime(int year, int month, int day, int hour, int minute, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(5), getValues(year, month, day, hour, minute)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, 0, 0, getChronology());
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        setPartial(new Partial(getFields(6), getValues(year, month, day, hour, minute, second)));
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, second, 0);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(6), getValues(year, month, day, hour, minute, second)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, second, 0, getChronology());
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        setPartial(new Partial(getFields(7), getValues(year, month, day, hour, minute, second, millisecond)));
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, second, millisecond);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, BigDecimal timezoneOffset) {
        setPartial(new Partial(getFields(7), getValues(year, month, day, hour, minute, second, millisecond)));
        setTimezoneOffset(timezoneOffset);
        jodaDateTime = new org.joda.time.DateTime(year, month, day, hour, minute, second, millisecond, getChronology());
    }

    public DateTime withPartial(Partial newDateTime) {
        setPartial(newDateTime);
        return this;
    }

    public DateTime withTimezoneOffset(BigDecimal newTimezoneOffset) {
        setTimezoneOffset(newTimezoneOffset);
        return this;
    }

    public static DateTime fromJavaDate(Date date) {
        if (date == null) {
            return null;
        }
        return fromJodaDateTime(new org.joda.time.DateTime(date));
    }

    public static DateTime fromJodaDateTime(org.joda.time.DateTime dt) {
        int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), dt.hourOfDay().get(),
                dt.minuteOfHour().get(), dt.secondOfMinute().get(), dt.millisOfSecond().get() };
        return new DateTime().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
    }

    public static DateTime getToday() {
        org.joda.time.DateTime dt = org.joda.time.DateTime.now();
        int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), 0, 0, 0, 0 };
        return new DateTime().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
    }

    public static DateTime getNow() {
        org.joda.time.DateTime dt = org.joda.time.DateTime.now();
        int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), dt.hourOfDay().get(),
                dt.minuteOfHour().get(), dt.secondOfMinute().get(), dt.millisOfSecond().get() };
        return new DateTime().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
    }

    public static DateTime expandPartialMin(DateTime dt, int size) {
        for (int i = dt.getPartial().size(); i < size; ++i) {
            dt.setPartial(dt.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMinimumValue()));
        }
        return dt;
    }

    public static DateTime expandPartialMax(DateTime dt, int size, int maxPrecision) {
        for (int i = dt.getPartial().size(); i < size; ++i) {
            // only want to max values up to the missing precision
            if (i > maxPrecision) {
                dt.setPartial(dt.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMinimumValue()));
            }
            else if (i == 2) {
                dt.setPartial(dt.getPartial().with(getField(i), dt.getPartial().getChronology().dayOfMonth().getMaximumValue(dt.getPartial())));
            }
            else {
                dt.setPartial(dt.getPartial().with(getField(i), DateTime.getField(i).getField(null).getMaximumValue()));
            }
        }
        return dt;
    }

    public Boolean equal(DateTime other) {
        if (this.getPartial().size() != other.getPartial().size()) { // Uncertainty
            return null;
        }
        DateTime left = new DateTime().withPartial(this.getPartial()).withTimezoneOffset(this.getTimezoneOffset());
        DateTime right = new DateTime().withPartial(other.getPartial()).withTimezoneOffset(other.getTimezoneOffset());

        // for DateTime equals, all DateTime elements must be present -- any null values result in null return
        if (this.getPartial().size() < 7) left = expandPartialMin(left, 7);
        if (other.getPartial().size() < 7) right = expandPartialMin(right, 7);

        boolean tzEqaul = false;
        if (left.getTimezoneOffset() == null && right.getTimezoneOffset() == null) {
            tzEqaul = true;
        }
        else if (left.getTimezoneOffset() != null && right.getTimezoneOffset() != null) {
            tzEqaul = left.getTimezoneOffset().compareTo(right.getTimezoneOffset()) == 0;
        }

        return Arrays.equals(left.partial.getValues(), right.partial.getValues());
    }

    @Override
    public String toString() {
        return this.getPartial().toString();
    }
}
