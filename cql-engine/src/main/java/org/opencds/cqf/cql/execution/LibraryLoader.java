package org.opencds.cqf.cql.execution;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;

/**
 * Created by Bryn on 9/12/2016.
 */
public interface LibraryLoader {
    Library load(VersionedIdentifier libraryIdentifier);
}
