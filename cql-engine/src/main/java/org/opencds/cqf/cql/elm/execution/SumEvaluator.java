package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.util.Iterator;
import java.math.BigDecimal;

/*
Sum(argument List<Integer>) Integer
Sum(argument List<Decimal>) Decimal
Sum(argument List<Quantity>) Quantity

The Sum operator returns the sum of non-null elements in the source.
If the source contains no non-null elements, null is returned.
If the list is null, the result is null.
Return types: Integer, BigDecimal & Quantity
*/

/**
 * Created by Chris Schuler on 6/14/2016
 */
public class SumEvaluator extends org.cqframework.cql.elm.execution.Sum {

    public static Object sum(Object source) {
        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            Iterable elements = (Iterable)source;
            Object sum = null;
            for (Object element : elements) {
                if (element == null) {
                    continue;
                }

                if (sum == null) {
                    sum = element;
                }
                else {
                    sum = AddEvaluator.add(sum, element);
                }
            }

            return sum;
        }
        throw new IllegalArgumentException(String.format("Invalid instance '%s' for Sum operation.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), sum(source), source);
    }
}
