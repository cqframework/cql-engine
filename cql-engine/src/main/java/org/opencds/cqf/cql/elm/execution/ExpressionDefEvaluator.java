package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExpressionDefEvaluator extends org.cqframework.cql.elm.execution.ExpressionDef {

    @Override
    public Object evaluate(Context context) {
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
