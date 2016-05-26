package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class InstanceEvaluator extends Instance {

    @Override
    public Object evaluate(Context context) {
        Class clazz = context.resolveType(this.getClassType());
        try {
            Object object = clazz.newInstance();
            for (InstanceElement element : this.getElement()) {
                Object value = element.getValue().evaluate(context);
                context.setValue(object, element.getName(), value);
            }

            return object;
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("Could not create an instance of class %s.", clazz.getName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not create an instance of class %s.", clazz.getName()));
        }
    }
}
