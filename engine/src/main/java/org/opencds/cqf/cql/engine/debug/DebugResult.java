package org.opencds.cqf.cql.engine.debug;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.elm.execution.Executable;

import java.util.HashMap;
import java.util.Map;

public class DebugResult {
    private Map<String, DebugLibraryResultEntry> libraryResults;

    public DebugResult() {
        libraryResults = new HashMap<String, DebugLibraryResultEntry>();
    }

    public void logDebugResult(Executable node, Library currentLibrary, Object result) {
        try {
            DebugLibraryResultEntry libraryResultEntry = libraryResults.get(currentLibrary.getIdentifier().getId());
            if (libraryResultEntry == null) {
                libraryResultEntry = new DebugLibraryResultEntry(currentLibrary.getIdentifier().getId());
                libraryResults.put(libraryResultEntry.getLibraryName(), libraryResultEntry);
            }
            if (libraryResultEntry != null) {
                libraryResultEntry.logDebugResultEntry(node, result);
            }
        }
        catch (Exception e) {
            // do nothing, an exception logging debug helps no one
        }
    }
}
