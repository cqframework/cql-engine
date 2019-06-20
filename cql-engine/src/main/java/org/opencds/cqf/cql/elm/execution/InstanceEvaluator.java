package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class InstanceEvaluator extends org.cqframework.cql.elm.execution.Instance {

    @Override
    public Object evaluate(Context context) {
        Object object = context.createInstance(this.getClassType());
        for (org.cqframework.cql.elm.execution.InstanceElement element : this.getElement()) {
            Object value = element.getValue().evaluate(context);
            context.setValue(object, element.getName(), value);
        }

        return object;
    }
}
