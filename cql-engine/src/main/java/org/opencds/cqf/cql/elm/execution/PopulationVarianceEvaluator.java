package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/*
PopulationVariance(argument List<Decimal>) Decimal
PopulationVariance(argument List<Quantity>) Quantity

The PopulationVariance operator returns the statistical population variance of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
 * Created by Chris Schuler on 6/14/2016
 */
public class PopulationVarianceEvaluator extends org.cqframework.cql.elm.execution.PopulationVariance {

    public static Object popVariance(Object source) {

        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {

            if (((List) source).isEmpty()) {
                return null;
            }

            Object mean = AvgEvaluator.avg(source);

            List<Object> newVals = new ArrayList<>();

            ((List) source).forEach(ae -> newVals.add(
                    MultiplyEvaluator.multiply(
                            SubtractEvaluator.subtract(ae, mean),
                            SubtractEvaluator.subtract(ae, mean))
                    )
            );

            return AvgEvaluator.avg(newVals);
        }

        throw new IllegalArgumentException(String.format("Cannot perform Population Variance operation with argument of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return popVariance(source);
    }
}
