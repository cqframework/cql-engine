package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;

import java.math.BigDecimal;

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

        BigDecimal offset;
        if (this.getTimezoneOffset() == null) {
            offset = TemporalHelper.zoneToOffset(context.getEvaluationDateTime().getDateTime().getOffset());
        }
        else {
            offset = (BigDecimal) this.getTimezoneOffset().evaluate(context);
        }

        return new DateTime(
                offset,
                TemporalHelper.cleanArray(
                        year,
                        (this.getMonth() == null) ? null : (Integer)this.getMonth().evaluate(context),
                        (this.getDay() == null) ? null : (Integer)this.getDay().evaluate(context),
                        (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context),
                        (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context),
                        (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context),
                        (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context)
                )
        ).withEvaluationOffset(context.getEvaluationDateTime().getDateTime().getOffset());
    }
}
