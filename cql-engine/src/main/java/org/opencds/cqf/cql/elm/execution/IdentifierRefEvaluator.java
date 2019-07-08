package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.IdentifierRef;
import org.opencds.cqf.cql.execution.Context;

public class IdentifierRefEvaluator extends IdentifierRef {

    @Override
    protected Object internalEvaluate(Context context) {

        String name = this.getName();

        if (name == null) {
            return null;
        }

        return context.resolveIdentifierRef(name);
    }
}
