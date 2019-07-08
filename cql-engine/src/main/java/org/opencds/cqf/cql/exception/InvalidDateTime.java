package org.opencds.cqf.cql.exception;

public class InvalidDateTime extends CqlException {
    public InvalidDateTime(String message) {
        super(message);
    }

    public InvalidDateTime(String message, Throwable cause) {
        super(message, cause);
    }
}
