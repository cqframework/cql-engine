package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;
import java.util.List;

/*
PopulationStdDev(argument List<Decimal>) Decimal
PopulationStdDev(argument List<Quantity>) Quantity

The PopulationStdDev operator returns the statistical standard deviation of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Return types: BigDecimal & Quantity
*/

public class PopulationStdDevEvaluator extends org.cqframework.cql.elm.execution.PopulationStdDev {

    public static Object popStdDev(Object source) {

        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {

            if (((List) source).isEmpty()) {
                return null;
            }

            Object variance = PopulationVarianceEvaluator.popVariance(source);

            return variance instanceof BigDecimal ?
                    PowerEvaluator.power(variance, new BigDecimal("0.5")) :
                    new Quantity().withValue((BigDecimal) PowerEvaluator.power(((Quantity) variance).getValue(),
                            new BigDecimal("0.5"))).withUnit(((Quantity) variance).getUnit());
        }

        throw new InvalidOperatorArgument(
                "PopulationStdDev(List<Decimal>) or PopulationStdDev(List<Quantity>)",
                String.format("PopulationStdDev(%s)", source.getClass().getName())
        );
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        return popStdDev(source);
    }
}
