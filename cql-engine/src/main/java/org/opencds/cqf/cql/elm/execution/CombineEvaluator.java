package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Combine(source List<String>) String
Combine(source List<String>, separator String) String

The Combine operator combines a list of strings, optionally separating each string with the given separator.
If either argument is null, or any element in the source list of strings is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class CombineEvaluator extends org.cqframework.cql.elm.execution.Combine {

    public static Object combine(Object source, String separator) {

        if (source == null || separator == null) {
            return null;
        }

        else {
            if (source instanceof Iterable) {
                StringBuffer buffer = new StringBuffer("");
                Iterator iterator = ((Iterable) source).iterator();
                boolean first = true;

                while (iterator.hasNext()) {
                    Object item = iterator.next();

                    if (item == null) {
                        return null;
                    }

                    if (!first) {
                        buffer.append(separator);
                    }

                    else {
                        first = false;
                    }
                    buffer.append((String)item);
                }
                return buffer.toString();
            }
        }
        throw new IllegalArgumentException(String.format("Cannot Combine arguments of type '%s'.", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = this.getSource().evaluate(context);
        String separator = this.getSeparator() == null ? "" : (String) this.getSeparator().evaluate(context);

        return context.logTrace(this.getClass(), combine(source, separator), source);
    }
}
