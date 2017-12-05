package org.opencds.cqf.cql.runtime;

import org.joda.time.*;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Created by Chris Schuler on 6/16/2016
 */
public class Time extends BaseTemporal {

    protected static final DateTimeFieldType[] fields = new DateTimeFieldType[] {
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

        if (dateTimeElement.startsWith("hour")) {
            return 0;
        }
        else if (dateTimeElement.startsWith("minute")) {
            return 1;
        }
        else if (dateTimeElement.startsWith("second")) {
            return 2;
        }
        else if (dateTimeElement.startsWith("millisecond")) {
            return 3;
        }

        return -1;
    }

    public static String getUnit(int idx) {
        switch (idx) {
            case 0: return "hours";
            case 1: return "minutes";
            case 2: return "seconds";
            case 3: return "milliseconds";
        }
        throw new IllegalArgumentException("Invalid index for Time unit request.");
    }

    public org.joda.time.DateTime getDateTimePrecision(Partial partial) {
        switch (partial.size()) {
            case 0: return new org.joda.time.DateTime();
            case 1: return new org.joda.time.DateTime(1, 1, 1, partial.getValue(0), 0, 0, 0);
            case 2: return new org.joda.time.DateTime(1,1,1, partial.getValue(0), partial.getValue(1), 0, 0);
            case 3: return new org.joda.time.DateTime(1,1,1, partial.getValue(0), partial.getValue(1), partial.getValue(2), 0);
            case 4: return new org.joda.time.DateTime(1,1,1,partial.getValue(0), partial.getValue(1), partial.getValue(2), partial.getValue(3));
            default: throw new RuntimeException("Error creating Joda DateTime from Partial");
        }
    }

    public void setPartial(Partial partial) {
        this.jodaDateTime = getDateTimePrecision(partial);
        this.partial = partial;
    }

    public Time() {
        partial = new Partial();
        timezoneOffset = new BigDecimal(0);
        jodaDateTime = new org.joda.time.DateTime();
    }

    public Time(int hour) {
        setPartial(new Partial().with(DateTimeFieldType.hourOfDay(), hour));
        jodaDateTime = new DateTime(1, 1, 1, hour, 0, 0, 0);
    }

    public Time(int hour, BigDecimal offset) {
        setPartial(new Partial().with(DateTimeFieldType.hourOfDay(), hour));
        setTimezoneOffset(offset);
        jodaDateTime = new DateTime(1, 1, 1, hour, 0, 0, 0, getChronology());
    }

    public Time(int hour, int minute) {
        setPartial(new Partial(getFields(2), getValues(hour, minute)));
        jodaDateTime = new DateTime(1, 1, 1, hour, minute, 0, 0);
    }

    public Time(int hour, int minute, BigDecimal offset) {
        setPartial(new Partial(getFields(2), getValues(hour, minute)));
        setTimezoneOffset(offset);
        jodaDateTime = new DateTime(1, 1, 1, hour, minute, 0, 0, getChronology());
    }

    public Time(int hour, int minute, int second) {
        setPartial(new Partial(getFields(3), getValues(hour, minute, second)));
        jodaDateTime = new DateTime(1, 1, 1, hour, minute, second, 0);
    }

    public Time(int hour, int minute, int second, BigDecimal offset) {
        setPartial(new Partial(getFields(3), getValues(hour, minute, second)));
        setTimezoneOffset(offset);
        jodaDateTime = new DateTime(1, 1, 1, hour, minute, second, 0, getChronology());
    }

    public Time(int hour, int minute, int second, int millis) {
        setPartial(new Partial(getFields(4), getValues(hour, minute, second, millis)));
        jodaDateTime = new DateTime(1,1,1, hour, minute, second, millis);
    }

    public Time(int hour, int minute, int second, int millis, BigDecimal offset) {
        setPartial(new Partial(getFields(4), getValues(hour, minute, second, millis)));
//        setPartial(new Partial(getFields(4), new int[]{hour, minute, second, millis}));
        setTimezoneOffset(offset);
        jodaDateTime = new DateTime(1,1,1, hour, minute, second, millis, getChronology());
    }

    public Time withPartial(Partial newTime) {
        setPartial(newTime);
        return this;
    }

    public Time withTimezoneOffset(BigDecimal newTimezoneOffset) {
        setTimezoneOffset(newTimezoneOffset);
        return this;
    }

    public static Time getTimeOfDay() {
        org.joda.time.DateTime dt = org.joda.time.DateTime.now();
        int [] values = { dt.hourOfDay().get(), dt.minuteOfHour().get(), dt.secondOfMinute().get(), dt.millisOfSecond().get() };
        return new Time().withPartial(new Partial(fields, values)).withTimezoneOffset(new BigDecimal(0));
    }

    public static Time expandPartialMin(Time dt, int size) {
        for (int i = dt.getPartial().size(); i < size; ++i) {
            dt.setPartial(dt.getPartial().with(getField(i), getField(i).getField(null).getMinimumValue()));
        }
        return dt;
    }

    public Boolean equal(Time other) {
        if (this.getPartial().size() != other.getPartial().size()) { // Uncertainty
            return null;
        }
        Time left = new Time().withPartial(this.getPartial()).withTimezoneOffset(this.getTimezoneOffset());
        Time right = new Time().withPartial(other.getPartial()).withTimezoneOffset(other.getTimezoneOffset());

        // for Time equals, all Time elements must be present -- any null values result in null return
        if (this.getPartial().size() < 4) left = expandPartialMin(left, 4);
        if (other.getPartial().size() < 4) right = expandPartialMin(right, 4);

        return Arrays.equals(left.partial.getValues(), right.partial.getValues())
                && left.getTimezoneOffset().compareTo(right.getTimezoneOffset()) == 0;
    }
}
