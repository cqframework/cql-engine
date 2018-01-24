package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Christopher Schuler on 6/13/2017.
 */
public class MessageEvaluator extends org.cqframework.cql.elm.execution.Message {

    final static Logger logger = LoggerFactory.getLogger(MessageEvaluator.class);

    public static Object message(Object source, Boolean condition, String code, String severity, String message) {
        if (severity == null) {
            severity = "message";
        }

        if (condition) {
            StringBuilder messageBuilder = new StringBuilder();
            if (code != null) {
                messageBuilder.append(code).append(": ");
            }
            switch (severity.toLowerCase()) {
                case "message":
                    logger.info(messageBuilder.append(message).toString()); break;
                case "warning":
                    logger.warn(messageBuilder.append(message).toString()); break;
                case "trace":
                    messageBuilder.append(message).append("\n").append(stripPHI(source).toString());
                    logger.debug(messageBuilder.toString()); break;
                case "error":
                    messageBuilder.append(message).append("\n").append(stripPHI(source).toString());
                    logger.error(messageBuilder.toString());
                    throw new RuntimeException(messageBuilder.toString());
            }
        }
        return source;
    }

    private static Object stripPHI(Object source) {
        // TODO
        return source;
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
