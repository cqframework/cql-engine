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
            VersionedIdentifier libraryId = context.getCurrentLibrary().getIdentifier();

            ExpressionResult er = null;

            if (!context.isExpressionInCache(libraryId, this.getName())) {
                er = ExpressionResult.newInstance();
                context.getSubscriptionContext().addSubscriber(er.getId(), er);
            } else {
                er = context.getExpressionResultFromCache(libraryId, this.getName());
                context.getEvaluatedResources().addAll(er.getEvaluatedResource());
                context.getSubscriptionContext().notifySubscribers(er.getEvaluatedResource());
                context.clearEvaluatedResources();
                return er.getResult();
            }

            Object result = this.getExpression().evaluate(context);

            if (!context.isExpressionInCache(libraryId, this.getName())) {
                context.addExpressionToCache(libraryId, this.getName(), er.withResult(result));
                context.getSubscriptionContext().notifySubscribers(context.getEvaluatedResources());
                context.clearEvaluatedResources();
            }
            context.getSubscriptionContext().removeSubscriber(er.getId());
            return result;
        } finally {
            if (this.getContext() != null) {
                context.exitContext();
            }
        }
    }
}
