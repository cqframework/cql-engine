package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Date;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;

import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

/*

    ConvertsToDateTime(argument Date) Boolean
    ConvertsToDateTime(argument String) Boolean

    The ConvertsToDateTime operator returns true if its argument can be converted to a DateTime value. See the ToDateTime
        operator for a description of the supported conversions.

    If the input string is not formatted correctly, or does not represent a valid DateTime value, the result is false.

    As with date and time literals, DateTime values may be specified to any precision. If no timezone offset is supplied,
        the timezone offset of the evaluation request timestamp is assumed.

    If the argument is null, the result is null.

*/

public class ConvertsToDateTimeEvaluator extends org.cqframework.cql.elm.execution.ConvertsToDateTime {

    public static Boolean convertsToDateTime(Object argument, ZoneOffset offset) {
        if (argument == null) {
            return null;
        }

        if (argument instanceof String) {
            try {
                new DateTime((String) argument, offset);
            } catch (DateTimeParseException dtpe) {
                return false;
            }
            return true;
        }

        else if (argument instanceof Date) {
            try {
                new DateTime(
                        TemporalHelper.getDefaultOffset(),
                        ((Date) argument).getDate().getYear(),
                        ((Date) argument).getDate().getMonthValue(),
                        ((Date) argument).getDate().getDayOfMonth(),
                        0, 0, 0, 0
                );
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        throw new InvalidOperatorArgument(
                "ConvertsToDateTime(String) or ConvertsToDateTime(Date)",
                String.format("ConvertsToDateTime(%s)", argument.getClass().getName())
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return convertsToDateTime(operand, context.getEvaluationDateTime().getEvaluationOffset());
    }
}
