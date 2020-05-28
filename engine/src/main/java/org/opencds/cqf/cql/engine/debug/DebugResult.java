package org.opencds.cqf.cql.engine.debug;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.elm.execution.Executable;

import java.util.Map;

public class DebugResult {
    private Map<String, DebugLibraryResultEntry> libraryResults;

    public void logDebugResult(Executable node, Library currentLibrary, Object result) {
        DebugLibraryResultEntry libraryResultEntry = libraryResults.get(currentLibrary.getIdentifier().getId());
        if (libraryResultEntry != null) {
            libraryResultEntry.logDebugResultEntry(node, result);
        }
    }
}
