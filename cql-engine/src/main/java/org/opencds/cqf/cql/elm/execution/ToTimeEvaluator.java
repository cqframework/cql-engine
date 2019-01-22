package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Time;

/*
ToTime(argument String) Time

The ToTime operator converts the value of its argument to a Time value.
The operator expects the string to be formatted using ISO-8601 time representation:
  Thh:mm:ss.fff(+|-)hh:mm
In addition, the string must be interpretable as a valid time-of-day value.
For example, the following are valid string representations for time-of-day values:
'T14:30:00.0Z'                // 2:30PM UTC
'T14:30:00.0-07:00'           // 2:30PM Mountain Standard (GMT-7:00)
If the input string is not formatted correctly, or does not represent a valid time-of-day value, a run-time error is thrown.
As with time-of-day literals, time-of-day values may be specified to any precision.
If no timezone is supplied, the timezone of the evaluation request timestamp is assumed.
If the argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 7/12/2016
 */
public class ToTimeEvaluator extends org.cqframework.cql.elm.execution.ToTime {

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            return new Time((String) operand, context.getEvaluationDateTime().getEvaluationOffset())
                    .withEvaluationOffset(context.getEvaluationDateTime().getEvaluationOffset());
        }

        throw new IllegalArgumentException("Cannot perform the ToTime operation with argument of type " + operand.getClass().getName());
    }
}
