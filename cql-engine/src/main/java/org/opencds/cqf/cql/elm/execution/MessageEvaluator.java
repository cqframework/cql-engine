package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher Schuler on 6/13/2017.
 */
public class MessageEvaluator extends org.cqframework.cql.elm.execution.Message {

    public static Object message(Object source, Boolean condition, String code, String severity, String message) {
        if (severity == null) {
            severity = "message";
        }

        // TODO
        throw new NotImplementedException("Message operator has not been implemented.");
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        Boolean condition = (Boolean) getCondition().evaluate(context);
        String code = (String) getCode().evaluate(context);
        String severity = (String) getSeverity().evaluate(context);
        String message = (String) getMessage().evaluate(context);

        return message(source, condition, code, severity, message);
    }
}
