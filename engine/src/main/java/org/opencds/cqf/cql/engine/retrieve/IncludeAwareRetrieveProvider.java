package org.opencds.cqf.cql.engine.retrieve;

public interface IncludeAwareRetrieveProvider {
    Iterable<Object> retrieve(Request request);
}
