package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.runtime.Time;

/*
time from(argument DateTime) Time

NOTE: this is within the purview of DateTimeComponentFrom
  Description available in that class
*/

public class TimeFromEvaluator extends org.cqframework.cql.elm.execution.TimeFrom {

    public static Object timeFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            int hour;
            if (((DateTime)operand).getPrecision().toDateTimeIndex() > 2) {
                hour = ((DateTime)operand).getDateTime().getHour();
            }
            else {
                return null;
            }

            int minute;
            if (((DateTime)operand).getPrecision().toDateTimeIndex() > 3) {
                minute = ((DateTime)operand).getDateTime().getMinute();
            }
            else {
                return new Time(TemporalHelper.zoneToOffset(((DateTime)operand).getDateTime().getOffset()), hour);
            }

            int second;
            if (((DateTime)operand).getPrecision().toDateTimeIndex() > 4) {
                second = ((DateTime)operand).getDateTime().getSecond();
            }
            else {
                return new Time(TemporalHelper.zoneToOffset(((DateTime)operand).getDateTime().getOffset()), hour, minute);
            }

            int millisecond;
            if (((DateTime)operand).getPrecision().toDateTimeIndex() > 5) {
                millisecond = ((DateTime)operand).getDateTime().get(Precision.MILLISECOND.toChronoField());
            }
            else {
                return new Time(TemporalHelper.zoneToOffset(((DateTime)operand).getDateTime().getOffset()), hour, minute, second);
            }

            return new Time(TemporalHelper.zoneToOffset(((DateTime)operand).getDateTime().getOffset()), hour, minute, second, millisecond);
        }

        throw new IllegalArgumentException(String.format("Cannot TimeFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), timeFrom(operand), operand);
    }
}
