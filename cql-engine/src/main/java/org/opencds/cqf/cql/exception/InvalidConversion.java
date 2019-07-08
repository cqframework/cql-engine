package org.opencds.cqf.cql.exception;

public class InvalidConversion extends CqlException {
    public InvalidConversion(String message) {
        super(message);
    }

    public InvalidConversion(Object from, Object to) {
        super(String.format("Cannot Convert a value of type %s as %s.", from.getClass().getName(), to.getClass().getName()));
    }
}
