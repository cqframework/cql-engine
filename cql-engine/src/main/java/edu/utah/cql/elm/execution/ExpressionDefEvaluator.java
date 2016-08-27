package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;

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
            return this.getExpression().evaluate(context);
        }
        finally {
            if (this.getContext() != null) {
                context.exitContext();
            }
        }
    }
}
