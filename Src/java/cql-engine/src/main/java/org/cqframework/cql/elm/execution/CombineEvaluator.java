package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/**
 * Created by Bryn on 5/25/2016.
 */
public class CombineEvaluator extends Combine {
    @Override
    public Object evaluate(Context context) {
        Object sourceVal = this.getSource().evaluate(context);
        String separator = this.getSeparator() == null ? "" : (String) this.getSeparator().evaluate(context);

        if (sourceVal == null || separator == null) {
            return null;
        } else {
            if (sourceVal instanceof Iterable) {
                StringBuffer buffer = new StringBuffer("");
                Iterator iterator = ((Iterable) sourceVal).iterator();
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

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s'.", this.getClass().getSimpleName(), sourceVal.getClass().getName()));
    }
}
