package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

public class ConceptRefEvaluator extends org.cqframework.cql.elm.execution.ConceptRef {

    @Override
    protected Object internalEvaluate(Context context) {
        boolean enteredLibrary = context.enterLibrary(this.getLibraryName());
        try {
            return context.resolveConceptRef(this.getName()).evaluate(context);
        }
        finally {
            context.exitLibrary(enteredLibrary);
        }
    }

}
