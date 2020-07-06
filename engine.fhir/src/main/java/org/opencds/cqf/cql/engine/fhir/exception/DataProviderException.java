package org.opencds.cqf.cql.engine.fhir.exception;

import org.opencds.cqf.cql.engine.exception.CqlException;

public class DataProviderException extends CqlException {
    private static final long serialVersionUID = 1L;

    public DataProviderException(String message) {
        super(message);
    }
}
