package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.Date;
import org.cqframework.cql.elm.execution.DateTime;
import org.cqframework.cql.elm.execution.Time;
import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.exception.UndefinedResult;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.Value;


import java.math.RoundingMode;
import java.time.temporal.ChronoField;
import java.math.BigDecimal;

/*
Log(argument Decimal, base Decimal) Decimal

The Log operator computes the logarithm of its first argument, using the second argument as the base.
When invoked with Integer arguments, the arguments will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

public class LowBoundaryEvaluator {//extends org.cqframework.cql.elm.execution.LowBoundaryEvaluator {

    public static Object lowBoundry(Object input, Object inputPrecision) {
        if (input == null || inputPrecision == null) {
            return null;
        }

        //  Double base = Math.log(((BigDecimal)precision).doubleValue());
        //     Double value = Math.log(((BigDecimal)input).doubleValue());

        //     if (base == 0) {
        //         return Value.verifyPrecision(new BigDecimal(value));
        //     }

        //     return Value.verifyPrecision(new BigDecimal(value / base));
        if (input instanceof BigDecimal && inputPrecision instanceof Integer) {
            ((BigDecimal)input).setScale((Integer)inputPrecision, RoundingMode.FLOOR);
        }

        if (input instanceof Date) {
            if(inputPrecision instanceof Integer) {
                if((Integer)inputPrecision <= 4) {
                    ChronoField dateOfPrecision = Precision.getDateChronoFieldFromIndex(0);
                    dateOfPrecision.range().getMinimum();
                }
                else if((Integer)inputPrecision <= 6) {
                    ChronoField dateOfPrecision = Precision.getDateChronoFieldFromIndex(1);
                    dateOfPrecision.range().getMinimum();
                }
                else if((Integer)inputPrecision <= 8) {
                    ChronoField dateOfPrecision = Precision.getDateChronoFieldFromIndex(2);
                    dateOfPrecision.range().getMinimum();
                }
                else return null;
            }
        }
        return null;

        // if (input instanceof DateTime) {
        //     ((BigDecimal)input).setScale((Integer)precision, RoundingMode.FLOOR);
        // }

        // if (input instanceof Time) {
        //     ((BigDecimal)input).setScale((Integer)precision, RoundingMode.FLOOR);
        // }

        // throw new InvalidOperatorArgument(
        //         "LowBoundary(Decimal, Integer), LowBoundary(Date, Integer), LowBoundary(DateTime, Integer), or LowBoundary(Time, Integer)",
        //         String.format("LowBoundary(%s, %s)", input.getClass().getName(), precision.getClass().getName())
        // );
    }

    // @Override
    // protected Object internalEvaluate(Context context) {
    //     Object left = getOperand().get(0).evaluate(context);
    //     Object right = getOperand().get(1).evaluate(context);

    //     return lowBoundary(left, right);
    // }
}
