package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;

/*
Ln(argument Decimal) Decimal

The Ln operator computes the natural logarithm of its argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LnEvaluator extends org.cqframework.cql.elm.execution.Ln {

    public static Object ln(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal){
            BigDecimal retVal;
            try {
                retVal = new BigDecimal(Math.log(((BigDecimal) operand).doubleValue()));
            }
            catch (NumberFormatException nfe){
                if (((BigDecimal) operand).compareTo(new BigDecimal(0)) < 0) {
                    return null;
                }

                else if (((BigDecimal) operand).compareTo(new BigDecimal(0)) == 0) {
                    throw new ArithmeticException("Results in negative infinity");
                }
                else {
                    throw new NumberFormatException();
                }
            }
            return Value.verifyPrecision(retVal);
        }

        throw new IllegalArgumentException(String.format("Cannot perform Natural Log operation with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), ln(operand), operand);
    }
}
