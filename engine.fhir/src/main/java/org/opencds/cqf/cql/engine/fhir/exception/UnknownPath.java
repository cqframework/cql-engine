package org.opencds.cqf.cql.engine.fhir.exception;

public class UnknownPath extends DataProviderException {
    private static final long serialVersionUID = 1L;

    public UnknownPath(String message) {
        super(message);
    }
}
