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
            System.out.println("library: " + libraryId + "|"+ this.getName());

            ExpressionResult er = null;

            if (!context.isExpressionInCache(libraryId, this.getName())) {
                er = ExpressionResult.newInstance();
                System.out.println("Adding subscriber:" + er.getId());
                context.getSubscriptionContext().addSubscriber(er.getId(), er);
            } else {
                er = context.getExpressionResultFromCache(libraryId, this.getName());
                //context.getSubscriptionContext().notifySubscribers(er.getEvaluatedResource());
                //context.clearEvaluatedResources();
                context.getEvaluatedResources().addAll(er.getEvaluatedResource());
                context.getSubscriptionContext().notifySubscribers(er.getEvaluatedResource());
                context.clearEvaluatedResources();
                return er.getResult();
            }

//            if ( context.isExpressionInCache(libraryId, this.getName())) {
//
//                context.getEvaluatedResources().addAll(context.getExpressionEvaluatedResourceFromCache(libraryId, this.getName()));
//                return context.getExpressionResultFromCache(libraryId, this.getName());
//            }

            Object result = this.getExpression().evaluate(context);

            if (!context.isExpressionInCache(libraryId, this.getName())) {

                context.addExpressionToCache(libraryId, this.getName(), er
                    .withResult(result));
                context.getSubscriptionContext().notifySubscribers(context.getEvaluatedResources());
                context.clearEvaluatedResources();
                System.out.println("After clear:"+ context.getEvaluatedResources());
            }
            context.getSubscriptionContext().removeSubscriber(er.getId());
            return result;
        }
        finally {
            if (this.getContext() != null) {
                context.exitContext();
            }
        }
    }
}
