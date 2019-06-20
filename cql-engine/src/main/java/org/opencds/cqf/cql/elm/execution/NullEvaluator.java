package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class NullEvaluator extends org.cqframework.cql.elm.execution.Null {

    @Override
    public Object evaluate(Context context) {
        return null;
    }
}
