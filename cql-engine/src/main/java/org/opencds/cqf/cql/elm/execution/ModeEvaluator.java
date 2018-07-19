package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.CqlList;

import java.util.ArrayList;
import java.util.Iterator;

/*
Mode(argument List<T>) T

The Mode operator returns the statistical mode of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/13/2016
 */
public class ModeEvaluator extends org.cqframework.cql.elm.execution.Mode {

    public static Object mode(Object source) {
        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            Iterable element = (Iterable)source;
            Iterator itr = element.iterator();

            if (!itr.hasNext()) { // empty list
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

            int max = 0;
            Object mode = new Object();
            for (int i = 0; i < values.size(); ++i) {
                int count = 0;
                for (int j = i; j < values.size(); ++j) {
                    Boolean equal = EqualEvaluator.equal(values.get(i), values.get(j));
                    if (equal != null && equal) {
                        ++count;
                    }
                }
                if (count > max) {
                    mode = values.get(i);
                    max = count;
                }
            }
            return mode;
        }
        throw new IllegalArgumentException(String.format("Cannot Mode arguments of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), mode(source), source);
    }
}