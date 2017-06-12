package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;
import java.util.List;

/*
StdDev(argument List<Decimal>) Decimal
StdDev(argument List<Quantity>) Quantity

The StdDev operator returns the statistical standard deviation of the elements in source.
If the source contains no non-null elements, null is returned.
If the list is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
 * Created by Chris Schuler on 6/14/2016
 */
public class StdDevEvaluator extends org.cqframework.cql.elm.execution.StdDev {

    public static Object stdDev(Object source) {

        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {

            if (((List) source).isEmpty()) {
                return null;
            }

            Object variance = VarianceEvaluator.variance(source);

            return variance instanceof BigDecimal ?
                    PowerEvaluator.power(variance, new BigDecimal("0.5")) :
                    new Quantity().withValue((BigDecimal) PowerEvaluator.power(((Quantity) variance).getValue(),
                            new BigDecimal("0.5"))).withUnit(((Quantity) variance).getUnit());
        }

        throw new IllegalArgumentException(String.format("Cannot perform Standard Deviation operation with argument of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), stdDev(source), source);
    }
}
