package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

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
        if (operand instanceof DateTime) {
            return ((DateTime)operand).getTimezoneOffset();
        }

        throw new IllegalArgumentException(String.format("Cannot perform TimezoneFrom operation with arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), timezoneFrom(operand), operand);
    }
}
