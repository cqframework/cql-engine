package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.ExpressionResult;

public class ExpressionDefEvaluator extends org.cqframework.cql.elm.execution.ExpressionDef {

    @Override
    protected Object internalEvaluate(Context context) {
        if (this.getContext() != null) {
            context.enterContext(this.getContext());
        }
        try {
            context.pushEvaluatedResourceStack();
            VersionedIdentifier libraryId = context.getCurrentLibrary().getIdentifier();
            if (context.isExpressionCachingEnabled() && context.isExpressionInCache(libraryId, this.getName())) {
                context.getEvaluatedResources().addAll(
                    context.getExpressionEvaluatedResourceFromCache(libraryId, this.getName()));
                return context.getExpressionResultFromCache(libraryId, this.getName()).getResult();
            }

            Object result = this.getExpression().evaluate(context);

            if (context.isExpressionCachingEnabled() && !context.isExpressionInCache(libraryId, this.getName())) {
                ExpressionResult er = ExpressionResult.newInstance();
                er = er.withResult(result).withEvaluatedResource(context.getEvaluatedResources());
                context.addExpressionToCache(libraryId, this.getName(), er);
            }

            return result;
        } finally {
            context.popEvaluatedResourceStack();
            if (this.getContext() != null) {
                context.exitContext();
            }
        }
    }
}