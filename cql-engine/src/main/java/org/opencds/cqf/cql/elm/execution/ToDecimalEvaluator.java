package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;

/*
ToDecimal(argument String) Decimal

The ToDecimal operator converts the value of its argument to a Decimal value.
The operator accepts strings using the following format:
  (+|-)?#0(.0#)?
Meaning an optional polarity indicator, followed by any number of digits (including none), followed by at least one digit,
  followed optionally by a decimal point, at least one digit, and any number of additional digits (including none).
Note that the decimal value returned by this operator must be limited in precision and scale to the maximum precision and
  scale representable for Decimal values within CQL.
If the input string is not formatted correctly, or cannot be interpreted as a valid Decimal value, a run-time error is thrown.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ToDecimalEvaluator extends org.cqframework.cql.elm.execution.ToDecimal {

    public static Object toDecimal(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal) {
            return operand;
        }

        if (operand instanceof Integer) {
            return new BigDecimal((Integer)operand);
        }

        if (operand instanceof String) {
            try {
                if (((String) operand).contains(".")) {
                    String[] decimalSplit = ((String) operand).split("\\.");
                    if ((decimalSplit[0].contains("-") || decimalSplit[0].contains("+")) && decimalSplit[0].length() == 1) {
                        throw new IllegalArgumentException("Invalid String representation of Decimal");
                    }
                    else if (decimalSplit[0].length() == 0) {
                        throw new IllegalArgumentException("Invalid String representation of Decimal");
                    }
                }
                return Value.validateDecimal(new BigDecimal((String)operand));
            }
            catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Unable to convert given string to Decimal");
            }
        }

        throw new IllegalArgumentException(String.format("Cannot call ToDecimal operator with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), toDecimal(operand), operand);
    }
}
