package org.opencds.cqf.cql.engine.fhir.exception;

public class UnknownType extends DataProviderException {
    private static final long serialVersionUID = 1L;

    public UnknownType(String message) {
        super(message);
    }
}
