package org.opencds.cqf.cql.engine.exception;

public class CqlException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

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
