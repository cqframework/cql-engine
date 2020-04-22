package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

public class CodeRefEvaluator extends org.cqframework.cql.elm.execution.CodeRef {

    @Override
    protected Object internalEvaluate(Context context) {
        boolean enteredLibrary = context.enterLibrary(this.getLibraryName());
        try {
            return context.resolveCodeRef(this.getName()).evaluate(context);
        }
        finally {
            context.exitLibrary(enteredLibrary);
        }
    }
}
