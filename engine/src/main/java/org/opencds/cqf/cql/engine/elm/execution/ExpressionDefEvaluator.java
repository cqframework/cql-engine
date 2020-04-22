package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

public class ExpressionDefEvaluator extends org.cqframework.cql.elm.execution.ExpressionDef {

    @Override
    protected Object internalEvaluate(Context context) {
        if (this.getContext() != null) {
            context.enterContext(this.getContext());
        }
        try {
            if (context.isExpressionCachingEnabled() && context.isExpressionInCache(this.getName())) {
                return context.getExpressionResultFromCache(this.getName());
            }

            Object result = this.getExpression().evaluate(context);

            if (context.isExpressionCachingEnabled() && !context.isExpressionInCache(this.getName())) {
                context.addExpressionToCache(this.getName(), result);
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
