package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class SplitEvaluator extends Split {

    @Override
    public Object evaluate(Context context) {
        Object stringToSplit = getStringToSplit().evaluate(context);
        Object separator = getSeparator().evaluate(context);

        if (stringToSplit == null) {
            return null;
        }

        ArrayList<Object> result = new ArrayList<Object>();
        if (separator == null) {
            result.add(stringToSplit);
        }
        else {
            for (String string : ((String)stringToSplit).split((String)separator)) {
                result.add(string);
            }
        }
        return result;
    }
}
