package org.opencds.cqf.cql.execution;

import java.util.HashMap;
import java.util.Map;

import org.cqframework.cql.elm.execution.VersionedIdentifier;

public class EvaluationResult {

    public Map<VersionedIdentifier, LibraryResult> libraryResults;

    public EvaluationResult() {
        this.libraryResults = new HashMap<>();
    }

    public LibraryResult forLibrary(VersionedIdentifier libraryIdentifier) {
        return this.libraryResults.get(libraryIdentifier);
    }
}