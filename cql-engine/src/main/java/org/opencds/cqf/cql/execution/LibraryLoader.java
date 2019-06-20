package org.opencds.cqf.cql.execution;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;

public interface LibraryLoader {
    Library load(VersionedIdentifier libraryIdentifier);
}
