package org.opencds.cqf.cql.runtime;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Partial;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class DateTime extends BaseTemporal implements CqlType {

    public DateTime(Partial partial) {
        this.timezone = DateTimeZone.forOffsetMillis(TimeZone.getDefault().getRawOffset());
        this.isDateTime = true;
        setPartial(partial);
    }

    public DateTime(Partial partial, DateTimeZone timezone) {
        this.timezone = timezone;
        this.isDateTime = true;
        setPartial(partial);
    }

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

    public static DateTime fromJavaDate(Date date) {
        if (date == null) {
            return null;
        }
        return fromJodaDateTime(new org.joda.time.DateTime(date));
    }

    public static DateTime fromJodaDateTime(org.joda.time.DateTime dt) {
        int [] values = { dt.year().get(), dt.monthOfYear().get(), dt.dayOfMonth().get(), dt.hourOfDay().get(),
                dt.minuteOfHour().get(), dt.secondOfMinute().get(), dt.millisOfSecond().get() };
        return new DateTime(new Partial(fields, values), dt.getZone());
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
}
