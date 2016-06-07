package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class SubstringEvaluator extends Substring {

    @Override
    public Object evaluate(Context context) {
        Object stringValue = getStringToSub().evaluate(context);
        Object startIndexValue = getStartIndex().evaluate(context);
        Object lengthValue = getLength() == null ? null : getLength().evaluate(context);

        if (stringValue == null || startIndexValue == null) {
            return null;
        }

        String string = (String)stringValue;
        Integer startIndex = (Integer)startIndexValue;

        if (startIndex < 0 || startIndex >= string.length()) {
            return null;
        }

        if (lengthValue == null) {
            return string.substring(startIndex);
        }
        else {
            int endIndex = startIndex + (Integer)lengthValue;
            if (endIndex > string.length()) {
                endIndex = string.length();
            }

            if (endIndex < startIndex) {
                return null;
            }

            return string.substring(startIndex, endIndex);
        }
    }
}
