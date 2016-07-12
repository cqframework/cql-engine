package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Tuple;

/**
 * Created by Bryn on 5/25/2016. Edited by Chris Schuler on 7/11/2016 - Tuple
 */
public class PropertyEvaluator extends Property {

    @Override
    public Object evaluate(Context context) {
        Object target = null;

        if (this.getSource() != null) {
            target = getSource().evaluate(context);
            // Tuple element access
            if (target instanceof Tuple) {
              // NOTE: translator will throw error if Tuple does not contain the specified element -- no need for x.containsKey() check
              return ((Tuple)target).getElements().get(this.getPath());
            }
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
