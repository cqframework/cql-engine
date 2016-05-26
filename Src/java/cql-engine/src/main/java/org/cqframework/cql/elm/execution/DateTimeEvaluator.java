package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Bryn on 5/25/2016.
 */
public class DateTimeEvaluator extends DateTime {
    // TODO: Fix this, it should be using a Partial, not a DateTime
//    @Override
//    public Object evaluate(Context context) {
//        Expression field = null;
//        String year = (field = (Expression) this.getYear()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        if (year == null) return null;
//        if (year.length() < 4) {
//            throw new IllegalArgumentException("Must use 4 digits for year.");
//        }
//
//        String month = (field = (Expression) this.getMonth()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String day = (field = (Expression) this.getDay()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String hour = (field = (Expression) this.getHour()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String minute = (field = (Expression) this.getMinute()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String second = (field = (Expression) this.getSecond()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String milliSecond = (field = (Expression) this.getMillisecond()) == null ? null : ((Integer) field.evaluate(context)).toString();
//        String tzOffset = (field = (Expression) this.getTimezoneOffset()) == null ? null : ((Integer) field.evaluate(context)).toString();
//
//        StringBuffer timeBuffer = new StringBuffer(year);
//        if (month != null) {
//            timeBuffer.append("-").append(String.format("%0"+ (2 - month.length() )+"d%s",0 ,month));
//        }
//
//        if (day != null) {
//            timeBuffer.append("-").append(String.format("%0"+ (2 - day.length() )+"d%s",0 ,day));
//        }
//
//        if (hour != null) {
//            timeBuffer.append("T").append(hour);
//        }
//
//        if (minute != null) {
//            timeBuffer.append(":").append(minute);
//        }
//
//        if (second != null) {
//            timeBuffer.append(":").append(second);
//        }
//
//        if (milliSecond != null) {
//            timeBuffer.append(".").append(milliSecond);
//        }
//
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//        org.joda.time.DateTime newDate = formatter.parseDateTime(timeBuffer.toString());
//
//        return newDate.toDate();
//    }
}
