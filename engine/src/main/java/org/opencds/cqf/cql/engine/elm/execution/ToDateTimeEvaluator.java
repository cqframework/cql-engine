package org.opencds.cqf.cql.engine.elm.execution;

import java.time.format.DateTimeParseException;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.TemporalHelper;

/*

ToDateTime(argument String) DateTime

The ToDateTime operator converts the value of its argument to a DateTime value.
The operator expects the string to be formatted using the ISO-8601 date/time representation:
  YYYY-MM-DDThh:mm:ss.fff(+|-)hh:mm
In addition, the string must be interpretable as a valid date/time value.
For example, the following are valid string representations for date/time values:
'2014-01-01'                  // January 1st, 2014
'2014-01-01T14:30:00.0Z'      // January 1st, 2014, 2:30PM UTC
'2014-01-01T14:30:00.0-07:00' // January 1st, 2014, 2:30PM Mountain Standard (GMT-7:00)
If the input string is not formatted correctly, or does not represent a valid date/time value, the result is null.
As with date/time literals, date/time values may be specified to any precision. If no timezone is supplied,
  the timezone of the evaluation request timestamp is assumed.
For the Date overload, the result will be a DateTime with the time components set to zero, except for the timezone offset,
    which will be set to the timezone offset of the evaluation request timestamp.
If the argument is null, the result is null.

*/

public class ToDateTimeEvaluator extends org.cqframework.cql.elm.execution.ToDateTime {

    @Override
    protected Object internalEvaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            try {
                return new DateTime((String) operand, context.getEvaluationDateTime().getEvaluationOffset())
                        .withEvaluationOffset(context.getEvaluationDateTime().getEvaluationOffset());
            } catch (DateTimeParseException dtpe) {
                return null;
            }
        }

        if (operand instanceof Date) {
            return new DateTime(TemporalHelper.getDefaultOffset(), ((Date) operand).getDate().getYear(), ((Date) operand).getDate().getMonthValue(), ((Date) operand).getDate().getDayOfMonth(), 0, 0, 0, 0);
        }

        throw new InvalidOperatorArgument(
                "ToDateTime(String)",
                String.format("ToDateTime(%s)", operand.getClass().getName())
        );
    }
}
