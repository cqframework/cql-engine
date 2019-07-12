package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.Collection;
import java.util.List;

public class ExpressionDefEvaluator extends org.cqframework.cql.elm.execution.ExpressionDef {

    @Override
    protected Object internalEvaluate(Context context) {
        if (this.getContext() != null) {
            context.enterContext(this.getContext());
        }
        try {
            if (context.isExpressionCachingEnabled() && context.isExpressionInCache(this.getName())) {
                context.setCurrentExpression(this.getName());
                return context.getExpressionResultFromCache(this.getName());
            }

            Object result;
            try {
                context.setCurrentExpression(this.getName());
                result = this.getExpression().evaluate(context);
            }
            finally {
                context.popCurrentExpression();
            }
            
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
