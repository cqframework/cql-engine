package org.opencds.cqf.cql.engine.debug;

import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.elm.execution.Executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugLibraryResultEntry {
    private String libraryName;
    public String getLibraryName() {
        return this.libraryName;
    }

    public DebugLibraryResultEntry(String libraryName) {
        this.libraryName = libraryName;
        this.results = new HashMap<String, List<DebugResultEntry>>();
    }

    private Map<String, List<DebugResultEntry>> results;

    public void logDebugResultEntry(Executable node, Object result) {
        String nodeId = ((Element)node).getLocalId();
        if (!results.containsKey(nodeId)) {
            results.put(nodeId, new ArrayList<DebugResultEntry>());
        }
        List<DebugResultEntry> debugResults = results.get(nodeId);
        debugResults.add(new DebugResultEntry(result));
    }
}
