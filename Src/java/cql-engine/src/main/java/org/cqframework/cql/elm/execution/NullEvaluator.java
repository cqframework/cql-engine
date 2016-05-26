package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class NullEvaluator extends Null {

    @Override
    public Object evaluate(Context context) {
        return null;
    }
}
