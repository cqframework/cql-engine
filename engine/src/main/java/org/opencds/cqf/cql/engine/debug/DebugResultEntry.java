package org.opencds.cqf.cql.engine.debug;

import java.util.List;
import java.util.Map;

public class DebugResultEntry {
    private List<Integer> path;
    private Object value;

    public DebugResultEntry(Object value) {
        this.value = value;
    }
}
