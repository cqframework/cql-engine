package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

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
If the input string is not formatted correctly, or does not represent a valid date/time value, a run-time error is thrown.
As with date/time literals, date/time values may be specified to any precision. If no timezone is supplied,
  the timezone of the evaluation request timestamp is assumed.
If the argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 7/11/2016
 */
public class ToDateTimeEvaluator extends org.cqframework.cql.elm.execution.ToDateTime {

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            return new DateTime((String) operand, context.getEvaluationDateTime().getEvaluationOffset())
                            .withEvaluationOffset(context.getEvaluationDateTime().getEvaluationOffset());
        }

        throw new IllegalArgumentException("Cannot perform the ToDateTime operation with argument of type " + operand.getClass().getName());
    }
}
