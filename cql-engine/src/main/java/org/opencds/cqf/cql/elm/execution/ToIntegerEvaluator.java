package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
ToInteger(argument String) Integer

The ToInteger operator converts the value of its argument to an Integer value.
The operator accepts strings using the following format:
  (+|-)?#0
Meaning an optional polarity indicator, followed by any number of digits (including none), followed by at least one digit.
Note that the integer value returned by this operator must be a valid value in the range representable for Integer values in CQL.
If the input string is not formatted correctly, or cannot be interpreted as a valid Integer value, a run-time error is thrown.
If the argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/14/2016
 */
public class ToIntegerEvaluator extends org.cqframework.cql.elm.execution.ToInteger {

    public static Object toInteger(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            try {
                return Integer.parseInt((String) operand);
            }
            catch (NumberFormatException nfe) {
                try {
                    Double ret = Double.parseDouble((String) operand);
                    if (ret > (Integer) MaxValueEvaluator.maxValue("Integer")) {
                        throw new IllegalArgumentException("Integer exceeds the maximum value allowed");
                    }
                    else if (ret < (Integer) MinValueEvaluator.minValue("Integer")) {
                        throw new IllegalArgumentException("Integer precedes the minimum value allowed");
                    }
                    return ret.intValue();
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Unable to convert given string to Integer");
                }
            }
        }
        throw new IllegalArgumentException(String.format("Cannot cast a value of type %s as Boolean - use String values.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), toInteger(operand), operand);
    }
}
