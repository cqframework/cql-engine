package org.opencds.cqf.cql.engine.elm.execution;

import java.time.format.DateTimeParseException;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Time;

/*

ToTime(argument String) Time

The ToTime operator converts the value of its argument to a Time value.
The operator expects the string to be formatted using ISO-8601 time representation:
  Thh:mm:ss.fff(+|-)hh:mm
In addition, the string must be interpretable as a valid time-of-day value.
For example, the following are valid string representations for time-of-day values:
'T14:30:00.0Z'                // 2:30PM UTC
'T14:30:00.0-07:00'           // 2:30PM Mountain Standard (GMT-7:00)
If the input string is not formatted correctly, or does not represent a valid time-of-day value, the result is null.
As with time-of-day literals, time-of-day values may be specified to any precision.
If no timezone is supplied, the timezone of the evaluation request timestamp is assumed.
If the argument is null, the result is null.

*/

public class ToTimeEvaluator extends org.cqframework.cql.elm.execution.ToTime {

    @Override
    protected Object internalEvaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            try {
                return new Time((String) operand, context.getEvaluationDateTime().getEvaluationOffset())
                        .withEvaluationOffset(context.getEvaluationDateTime().getEvaluationOffset());
            } catch (DateTimeParseException dtpe) {
                return null;
            }
        }

        throw new InvalidOperatorArgument(
                "ToTime(String)",
                "ToTime(%s)" + operand.getClass().getName()
        );
    }
}
