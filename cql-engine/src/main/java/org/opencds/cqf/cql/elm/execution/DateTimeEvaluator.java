package org.opencds.cqf.cql.elm.execution;

import org.joda.time.DateTimeZone;
import org.joda.time.Partial;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

/*
simple type DateTime

The DateTime type represents date and time values with potential uncertainty within CQL.
CQL supports date and time values in the range @0001-01-01T00:00:00.0 to @9999-12-31T23:59:59.999 with a 1 millisecond step size.
*/

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class DateTimeEvaluator extends org.cqframework.cql.elm.execution.DateTime {

    @Override
    public Object evaluate(Context context) {
        Integer year = this.getYear() == null ? null : (Integer)this.getYear().evaluate(context);

        if (year == null) {
            return null;
        }

        if (year < 1) {
            throw new IllegalArgumentException(String.format("The year: %d falls below the accepted bounds of 0001-9999.", year));
        }

        else if (year > 9999) {
            throw new IllegalArgumentException(String.format("The year: %d falls above the accepted bounds of 0001-9999.", year));
        }

        Integer month = (this.getMonth() == null) ? null : (Integer)this.getMonth().evaluate(context);
        Integer day = (this.getDay() == null) ? null : (Integer)this.getDay().evaluate(context);
        Integer hour = (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context);
        Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
        Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
        Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);

        DateTimeZone timeZone =
                BaseTemporal.resolveDateTimeZone(
                        this.getTimezoneOffset() != null
                                ? (BigDecimal) this.getTimezoneOffset().evaluate(context)
                                : null);

        if (BaseTemporal.formatCheck(new ArrayList<>(Arrays.asList(year, month, day, hour, minute, second, millis)))) {
            int [] values = DateTime.getValues(year, month, day, hour, minute, second, millis);
            return new DateTime(new Partial(DateTime.getFields(values.length), values), timeZone);
        }

        throw new IllegalArgumentException("DateTime format is invalid");
    }
}
