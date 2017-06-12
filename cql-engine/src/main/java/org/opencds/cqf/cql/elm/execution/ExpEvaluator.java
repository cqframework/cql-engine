package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
Exp(argument Decimal) Decimal

The Exp operator raises e to the power of its argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExpEvaluator extends org.cqframework.cql.elm.execution.Exp {

    public static Object exp(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal){
            BigDecimal retVal;
            try {
                retVal = new BigDecimal(Math.exp(((BigDecimal)operand).doubleValue()));
            }
            catch (NumberFormatException nfe) {
                if (((BigDecimal)operand).compareTo(new BigDecimal(0)) > 0) {
                    throw new ArithmeticException("Results in positive infinity");
                }
                else if (((BigDecimal)operand).compareTo(new BigDecimal(0)) < 0) {
                    throw new ArithmeticException("Results in negative infinity");
                }
                else {
                    throw new NumberFormatException();
                }
            }
            return retVal;
        }

        throw new IllegalArgumentException(String.format("Cannot perform Exp evaluation with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), exp(operand), operand);
    }
}
