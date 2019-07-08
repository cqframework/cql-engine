package org.opencds.cqf.cql.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class CqlException extends RuntimeException
{
    public CqlException(String message)
    {
        super(message);
    }
    public CqlException(String message, Throwable cause) {
        super(message, cause);
    }
    public CqlException(Throwable cause) {
        super(cause == null ? null : String.format("Unexpected exception caught during execution: %s", cause.toString(), cause));
        if (cause != null) {
            cause.printStackTrace(System.err);
        }
    }
}
