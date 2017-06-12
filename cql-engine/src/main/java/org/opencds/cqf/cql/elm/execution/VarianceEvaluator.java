package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
Variance(argument List<Decimal>) Decimal
Variance(argument List<Quantity>) Quantity

The Variance operator returns the statistical variance of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
 * Created by Chris Schuler on 6/14/2016
 */
public class VarianceEvaluator extends org.cqframework.cql.elm.execution.Variance {

    public static Object variance(Object source) {

        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {

            if (((List) source).isEmpty()) {
                return null;
            }

            Object mean = AvgEvaluator.avg(source);

            List<Object> newVals = new ArrayList<>();

            for (Object element : (Iterable) source) {
                if (element != null) {
                    newVals.add(MultiplyEvaluator.multiply(
                            SubtractEvaluator.subtract(element, mean),
                            SubtractEvaluator.subtract(element, mean))
                    );
                }
            }

            return DivideEvaluator.divide(SumEvaluator.sum(newVals), new BigDecimal(newVals.size() - 1)); // slight variation to Avg
        }

        throw new IllegalArgumentException(String.format("Cannot perform Variance operation with argument of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), variance(source), source);
    }
}
