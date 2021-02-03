package org.opencds.cqf.cql.engine.fhir.searchparam;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CapabilityStatementIndex {

    Map<String, CapabilityStatementIndexEntry> entries = new HashMap<>();

    class CapabilityStatementIndexEntry {
        public Set<String> interactions;
        public Set<String> searchParams;
    }

    public void putResource(String resourceType, Set<String> interactions, Set<String> searchParams) {
        Objects.requireNonNull(resourceType, "resourceType can not be null");
        CapabilityStatementIndexEntry entry = new CapabilityStatementIndexEntry();
        entry.interactions = interactions;
        entry.searchParams = searchParams;

        this.entries.put(resourceType, entry);
    }

    public Boolean supportsResource(String resourceType) {
        return this.entries.containsKey(resourceType);
    }

    public Boolean supportsInteraction(String resourceType, String interaction) {
        CapabilityStatementIndexEntry entry = this.entries.get(resourceType);

        if (entry == null || entry.interactions == null) {
            return false;
        }

        return entry.interactions.contains(interaction);
    }

    public Boolean supportsSearchParam(String resourceType, String searchParam) {
        CapabilityStatementIndexEntry entry = this.entries.get(resourceType);

        if (entry == null || entry.searchParams == null) {
            return false;
        }

        return entry.searchParams.contains(searchParam);
    }
}
