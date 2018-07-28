package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.runtime.Time;

/*
timezone from(argument DateTime) Decimal
timezone from(argument Time) Decimal

NOTE: this is within the purview of DateTimeComponentFrom
  Description available in that class
*/

/**
 * Created by Chris Schuler on 6/22/2016
 */
public class TimezoneFromEvaluator extends org.cqframework.cql.elm.execution.TimezoneFrom {

    public static Object timezoneFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            return TemporalHelper.zoneToOffset(((DateTime) operand).getDateTime().getOffset());
        }

        if (operand instanceof Time) {
            return TemporalHelper.zoneToOffset(((Time) operand).getTime().getOffset());
        }

        throw new IllegalArgumentException(String.format("Cannot perform TimezoneFrom operation with arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return timezoneFrom(operand);
    }
}
