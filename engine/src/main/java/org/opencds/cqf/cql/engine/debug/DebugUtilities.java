package org.opencds.cqf.cql.engine.debug;

import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.elm.execution.Executable;
import org.opencds.cqf.cql.engine.runtime.CqlType;

public class DebugUtilities {
    public static void logDebugResult(Executable node, Library currentLibrary, Object result) {
        System.out.printf("%s.%s: %s%n", currentLibrary != null ? currentLibrary.getIdentifier().getId() : "unknown",
                node instanceof Element ? ((Element)node).getLocalId() : node.getClass().toString(),
                toDebugString(result));
    }

    public static String toDebugString(Object result) {
        if (result instanceof CqlType) {
            return ((CqlType)result).toString();
        }

        if (result instanceof Iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Object element : (Iterable)result) {
                sb.append(toDebugString(element));
                if (first) {
                    first = false;
                }
                else {
                    sb.append(",");
                }
            }
            sb.append("}");

            return sb.toString();
        }

        if (result != null) {
            return result.toString();
        }

        return "<null>";
    }
}
