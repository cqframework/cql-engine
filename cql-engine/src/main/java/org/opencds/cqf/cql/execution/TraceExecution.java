package org.opencds.cqf.cql.execution;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Christopher Schuler on 6/9/2017.
 */
public class TraceExecution {

    private Deque<String> trace;

    TraceExecution() {
        trace = new ArrayDeque<>();
    }

    void logEntry(Class clazz, Object... operands) {
        Integer i = 0;
        StringBuilder entry = new StringBuilder().append(clazz.getSimpleName()).append(" Entry: ");

        for (Object operand : operands) {
            entry.append("Operand").append((++i).toString()).append(": ").append(operand == null ? "null" : operand.toString());

            if(i < operands.length) {
                entry.append(", ");
            }
        }

        trace.add(entry.toString());
    }

    Object logExit(Class clazz, Object result) {
        String exit = clazz.getSimpleName() + " Returns: ";
        trace.add(exit + (result == null ? "null" : result.toString()));

        return result;
    }

    void logError(Class clazz, String message) {
        String error = clazz.getSimpleName() + " Error: " + message;
        trace.add(error);
    }

    Object logTrace(Class clazz, Object ... criteria) {
        Integer i = 0;
        StringBuilder entry = new StringBuilder().append(clazz.getSimpleName()).append(" Entry: ");

        for (Object operand : criteria) {
            if (i == 0) {
                String exit = clazz.getSimpleName() + " Returns: ";
                trace.push(exit + (operand == null ? "null" : operand.toString()));
                ++i;
            }
            else {
                entry.append("Operand").append((i++).toString()).append(": ").append(operand == null ? "null" : operand.toString());

                if(i < criteria.length) {
                    entry.append(", ");
                }
            }
        }

        trace.push(entry.toString());
        return criteria[0];
    }

    String getTraceString() {
        StringBuilder builder = new StringBuilder();
        for (String entry : trace) {
            builder.append(entry).append("\n");
        }

        return builder.toString();
    }
}
