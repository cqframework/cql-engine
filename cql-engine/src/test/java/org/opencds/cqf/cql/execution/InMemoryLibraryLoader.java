package org.opencds.cqf.cql.execution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;

public class InMemoryLibraryLoader implements LibraryLoader {

    private Map<VersionedIdentifier, Library> libraries = new HashMap<>();
    
    public InMemoryLibraryLoader(Collection<Library> libraries) {

        for (Library library : libraries) {
            this.libraries.put(library.getIdentifier(), library);
        }
    }

    public Library load(VersionedIdentifier libraryIdentifier) {
        Library library = this.libraries.get(libraryIdentifier);
        if (library == null) {
            throw new IllegalArgumentException(String.format("Unable to load library %s", libraryIdentifier.getId()));
        }

        return library;
    }
}