package org.opencds.cqf.cql.elm.execution;

import org.joda.time.Partial;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

/*
date from(argument DateTime) DateTime

NOTE: this is within the purview of DateTimeComponentFrom
  Description available in that class
*/

/**
 * Created by Chris Schuler on 6/22/2016
 */
public class DateFromEvaluator extends org.cqframework.cql.elm.execution.DateFrom {

    public static Object dateFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            int year = ((DateTime)operand).getJodaDateTime().getYear();

            int month;
            if (((DateTime)operand).getPartial().size() > 1) {
                month = ((DateTime)operand).getJodaDateTime().getMonthOfYear();
            }
            else {
                return new DateTime(new Partial(DateTime.getFields(1), new int[]{year}), ((DateTime)operand).getTimezone());
            }

            int day;
            if (((DateTime)operand).getPartial().size() > 2) {
                day = ((DateTime)operand).getJodaDateTime().getDayOfMonth();
            }
            else {
                return new DateTime(new Partial(DateTime.getFields(2), new int[]{year, month}), ((DateTime)operand).getTimezone());
            }

            return new DateTime(new Partial(DateTime.getFields(3), new int[]{year, month, day}), ((DateTime)operand).getTimezone());
        }

        throw new IllegalArgumentException(String.format("Cannot DateFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), dateFrom(operand), operand);
    }
}
