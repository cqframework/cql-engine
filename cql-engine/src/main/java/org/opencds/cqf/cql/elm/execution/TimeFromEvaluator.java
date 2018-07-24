package org.opencds.cqf.cql.elm.execution;

import org.joda.time.Partial;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
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
            if (((DateTime)operand).getPartial().size() > 3) {
                hour = ((DateTime)operand).getJodaDateTime().getHourOfDay();
            }
            else {
                return null;
            }

            int minute;
            if (((DateTime)operand).getPartial().size() > 4) {
                minute = ((DateTime)operand).getJodaDateTime().getMinuteOfHour();
            }
            else {
                return new Time(new Partial(Time.getFields(1), new int[]{hour}), ((DateTime)operand).getTimezone());
            }

            int second;
            if (((DateTime)operand).getPartial().size() > 5) {
                second = ((DateTime)operand).getJodaDateTime().getSecondOfMinute();
            }
            else {
                return new Time(new Partial(Time.getFields(2), new int[]{hour, minute}), ((DateTime)operand).getTimezone());
            }

            int millisecond;
            if (((DateTime)operand).getPartial().size() > 6) {
                millisecond = ((DateTime)operand).getJodaDateTime().getMillisOfSecond();
            }
            else {
                return new Time(new Partial(Time.getFields(3), new int[]{hour, minute, second}), ((DateTime)operand).getTimezone());
            }

            return new Time(new Partial(Time.getFields(4), new int[]{hour, minute, second, millisecond}), ((DateTime)operand).getTimezone());
        }

        throw new IllegalArgumentException(String.format("Cannot TimeFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), timeFrom(operand), operand);
    }
}
