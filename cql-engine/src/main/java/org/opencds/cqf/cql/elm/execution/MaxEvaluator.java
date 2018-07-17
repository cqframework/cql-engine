package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Max(argument List<Integer>) Integer
Max(argument List<Decimal>) Decimal
Max(argument List<Quantity>) Quantity
Max(argument List<DateTime>) DateTime
Max(argument List<Time>) Time
Max(argument List<String>) String

The Max operator returns the maximum element in the source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Possible return types include: Integer, BigDecimal, Quantity, DateTime, Time, String
*/

/**
 * Created by Chris Schuler on 6/13/2016
 */
public class MaxEvaluator extends org.cqframework.cql.elm.execution.Max {

    public static Object max(Object source) {
        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            Iterable element = (Iterable)source;
            Iterator itr = element.iterator();

            if (!itr.hasNext()) { // empty list
                return null;
            }

            Object max = itr.next();
            while (max == null && itr.hasNext()) {
                max = itr.next();
            }

            while (itr.hasNext()) {
                Object value = itr.next();

                if (value == null) { // skip null
                    continue;
                }

                Boolean greater = GreaterEvaluator.greater(value, max);
                if (greater != null && greater) {
                    max = value;
                }
            }
            return max;
        }

        throw new IllegalArgumentException(String.format("Cannot perform Max operation with arguments of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), max(source), source);
    }
}
