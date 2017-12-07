package org.opencds.cqf.cql.elm.execution;

import org.joda.time.DateTimeZone;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.DateTime;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.Time;

/*
simple type Time

The Time type represents time-of-day values within CQL.
CQL supports time values in the range @T00:00:00.0 to @T23:59:59.999 with a step size of 1 millisecond.
*/

/**
 * Created by Chris Schuler on 6/20/2016
 */
public class TimeEvaluator extends org.cqframework.cql.elm.execution.Time {

    @Override
    public Object evaluate(Context context) {

        if (this.getHour() == null) {
            return null;
        }

        Integer hour = (Integer)this.getHour().evaluate(context);
        Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
        Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
        Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);

        DateTimeZone timeZone =
                BaseTemporal.resolveDateTimeZone(
                        this.getTimezoneOffset() != null
                                ? (BigDecimal) this.getTimezoneOffset().evaluate(context)
                                : null);

        if (BaseTemporal.formatCheck(new ArrayList<>(Arrays.asList(hour, minute, second, millis)))) {
            int [] values = DateTime.getValues(hour, minute, second, millis);
            return new Time(new Partial(Time.getFields(values.length), values), timeZone);
        }

        throw new IllegalArgumentException("Time format is invalid");
    }
}
