package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.Time;

/*
precision from(argument DateTime) Integer
precision from(argument Time) Integer
timezone from(argument DateTime) Decimal
timezone from(argument Time) Decimal
date from(argument DateTime) DateTime
time from(argument DateTime) Time

The component-from operator returns the specified component of the argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
If the argument is null, or is not specified to the level of precision being extracted, the result is null.
*/

/**
 * Created by Chris Schuler on 6/22/2016
 */
public class DateTimeComponentFromEvaluator extends org.cqframework.cql.elm.execution.DateTimeComponentFrom {

    public static Object dateTimeComponentFrom(Object operand, String precision) {

        if (operand == null) {
            return null;
        }

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        Precision p = Precision.fromString(precision);

        if (operand instanceof DateTime) {
            DateTime dateTime = (DateTime)operand;

            if (p.toDateTimeIndex() > dateTime.getPrecision().toDateTimeIndex()) {
                return null;
            }

            return dateTime.getDateTime().get(p.toChronoField());
        }

        else if (operand instanceof Time) {
            Time time = (Time)operand;

            if (p.toTimeIndex() > time.getPrecision().toTimeIndex()) {
                return null;
            }

            return time.getTime().get(p.toChronoField());
        }

        throw new IllegalArgumentException(String.format("Cannot DateTimeComponentFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        String precision = getPrecision().value();

        return dateTimeComponentFrom(operand, precision);
    }
}
