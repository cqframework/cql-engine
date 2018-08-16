package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Count(argument List<T>) Integer

* The Count operator returns the number of non-null elements in the source.
* If the list contains no non-null elements, the result is 0.
* If the list is null, the result is null.
* Always returns Integer
*/

/**
 * Created by Chris Schuler on 6/13/2016
 */
public class CountEvaluator extends org.cqframework.cql.elm.execution.Count {

    public static Object count(Object source) {
        if (source == null) {
            return 0;
        }

        Integer size = 0;

        if (source instanceof Iterable) {
            Iterable element = (Iterable)source;
            Iterator itr = element.iterator();

            if (!itr.hasNext()) { // empty list
                return size;
            }

            while (itr.hasNext()) {
                Object value = itr.next();

                if (value == null) { // skip null
                    continue;
                }

                ++size;
            }

            return size;
        }

        throw new IllegalArgumentException(String.format("Invalid instance '%s' for Count operation.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {

        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), count(source), source);
    }
}
