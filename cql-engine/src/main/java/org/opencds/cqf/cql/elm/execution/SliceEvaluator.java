package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Schuler on 6/13/2017.
 */
public class SliceEvaluator extends org.cqframework.cql.elm.execution.Slice {

    public static Object slice(Object source, Integer start, Integer end) {
        if (source == null) {
            return null;
        }

        if (source instanceof Iterable) {
            List<Object> ret = new ArrayList<>();

            for (; start < end; ++start) {
                ret.add(((List) source).get(start));
            }

            return ret;
        }

        throw new IllegalArgumentException(String.format("Cannot perform Slice operator with arguments of type: %s", source.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        Integer start = (Integer) getStartIndex().evaluate(context);
        Integer end = (Integer) getEndIndex().evaluate(context);

        return context.logTrace(this.getClass(), slice(source, start, end), source, start, end);
    }
}
