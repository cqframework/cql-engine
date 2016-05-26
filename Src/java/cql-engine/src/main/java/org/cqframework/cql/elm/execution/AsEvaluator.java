package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class AsEvaluator extends As {

    private Class resolveType(Context context) {
        if (this.getAsTypeSpecifier() != null) {
            return context.resolveType(this.getAsTypeSpecifier());
        }

        return context.resolveType(this.getAsType());
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        if (operand != null) {
            Class clazz = resolveType(context);
            if (clazz.isAssignableFrom(operand.getClass())) {
                return operand;
            }
            else if (this.isStrict()) {
                throw new IllegalArgumentException(String.format("Cannot cast a value of type %s as %s.", operand.getClass().getName(), clazz.getName()));
            }
            else {
                return null;
            }
        }

        return null;
    }
}
