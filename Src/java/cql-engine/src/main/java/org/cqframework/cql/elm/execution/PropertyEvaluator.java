package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class PropertyEvaluator extends Property {

    @Override
    public Object evaluate(Context context) {
        Object target = null;

        if (this.getSource() != null) {
            target = getSource().evaluate(context);
        }
        else if (this.getScope() != null) {
            target = context.resolveVariable(this.getScope(), true).getValue();
        }

        if (target == null) {
            return null;
        }

        return context.resolvePath(target, this.getPath());
    }
}
