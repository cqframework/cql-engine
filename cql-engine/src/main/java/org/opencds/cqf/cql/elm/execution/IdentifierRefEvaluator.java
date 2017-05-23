package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.IdentifierRef;
import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher on 5/22/2017.
 */
public class IdentifierRefEvaluator extends IdentifierRef {

    @Override
    public Object evaluate(Context context) {

        String name = this.getName();

        if (name == null) {
            return null;
        }

        return context.resolveIdentifierRef(name);
    }
}
