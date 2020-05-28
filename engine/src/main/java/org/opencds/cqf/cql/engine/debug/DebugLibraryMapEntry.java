package org.opencds.cqf.cql.engine.debug;

import org.cqframework.cql.elm.execution.Element;
import org.opencds.cqf.cql.engine.elm.execution.Executable;

import java.util.HashMap;
import java.util.Map;

public class DebugLibraryMapEntry {
    private String libraryName;
    public String getLibraryName() {
        return this.libraryName;
    }

    private Map<String, DebugMapEntry> entries;

    public DebugLibraryMapEntry(String libraryName) {
        this.libraryName = libraryName;
        entries = new HashMap<String, DebugMapEntry>();
    }

    public boolean shouldDebug(Executable node) {
        if (node instanceof Element) {
            Element element = (Element)node;
            if (element != null) {
                return entries.containsKey(element.getLocalId());
            }
        }

        return false;
    }

    public void addEntry(String localId, DebugMapEntry entry) {
        entries.put(localId, entry);
    }
}
