package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Value;

import java.util.Iterator;
import java.math.BigDecimal;

/*
Avg(argument List<Decimal>) Decimal
Avg(argument List<Quantity>) Quantity

* The Avg operator returns the average of the non-null elements in the source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Returns values of type BigDecimal or Quantity
*/

/**
 * Created by Chris Schuler on 6/13/2016
 */
public class AvgEvaluator extends org.cqframework.cql.elm.execution.Avg {

    public static Object avg(Object source) {

        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            Iterable elements = (Iterable) source;
            Object avg = null;
            int size = 1;

            for (Object element : elements) {
                if (element == null) {
                    continue;
                }

                if (avg == null) {
                    avg = element;
                }
                else {
                    ++size;
                    avg = AddEvaluator.add(avg, element);
                }
            }

            return DivideEvaluator.divide(avg, new BigDecimal(size));
        }

        throw new IllegalArgumentException(String.format("Invalid instance '%s' for Avg operation.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {

        Object src = getSource().evaluate(context);

        return context.logTrace(this.getClass(), avg(src), src);
    }
}
