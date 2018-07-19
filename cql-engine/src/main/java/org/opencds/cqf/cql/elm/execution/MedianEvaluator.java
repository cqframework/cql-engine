package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.CqlList;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

/*
Median(argument List<Decimal>) Decimal
Median(argument List<Quantity>) Quantity

The Median operator returns the median of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/13/2016
 */
public class MedianEvaluator extends org.cqframework.cql.elm.execution.Median {

    public static Object median(Object source) {
        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            Iterable element = (Iterable) source;
            Iterator itr = element.iterator();

            if (!itr.hasNext()) { // empty
                return null;
            }

            ArrayList<Object> values = new ArrayList<>();
            while (itr.hasNext()) {
                Object value = itr.next();
                if (value != null) {
                    values.add(value);
                }
            }

            if (values.isEmpty()) { // all null
                return null;
            }

            values.sort(new CqlList().valueSort);

            if (values.size() % 2 != 0) {
                return values.get(values.size() / 2);
            } else {
                if (values.get(0) instanceof Integer) { // size of list is even
                    return TruncatedDivideEvaluator.div(
                            AddEvaluator.add(values.get(values.size() / 2), values.get((values.size() / 2) - 1)), 2
                    );
                } else if (values.get(0) instanceof BigDecimal || values.get(0) instanceof Quantity) {
                    return DivideEvaluator.divide(
                            AddEvaluator.add(values.get(values.size() / 2), values.get((values.size() / 2) - 1)), new BigDecimal("2.0")
                    );
                }
            }
        }

        throw new IllegalArgumentException(String.format("Invalid instance '%s' for Median operation.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), median(source), source);
    }
}
