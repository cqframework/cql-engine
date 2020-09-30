package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.engine.execution.Context;

public class ExpressionDefEvaluator extends org.cqframework.cql.elm.execution.ExpressionDef {

    @Override
    protected Object internalEvaluate(Context context) {
        if (this.getContext() != null) {
            context.enterContext(this.getContext());
        }
        try {
            VersionedIdentifier libraryId = context.getCurrentLibrary().getIdentifier();
            if (context.isExpressionCachingEnabled() && context.isExpressionInCache(libraryId, this.getName())) {
                return context.getExpressionResultFromCache(libraryId, this.getName());
            }

            Object result = this.getExpression().evaluate(context);

            if (context.isExpressionCachingEnabled() && !context.isExpressionInCache(libraryId, this.getName())) {
                context.addExpressionToCache(libraryId, this.getName(), result);
            }

            return result;
        }
        finally {
            if (this.getContext() != null) {
                context.exitContext();
            }
        }
    }
}
