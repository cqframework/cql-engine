package org.opencds.cqf.cql.engine.exception;

import org.opencds.cqf.cql.engine.debug.SourceLocator;

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

    public CqlException(String message, SourceLocator sourceLocator) {
        super(message);
        this.sourceLocator = sourceLocator;
    }

    public CqlException(String message, Throwable cause, SourceLocator sourceLocator) {
        super(message, cause);
        this.sourceLocator = sourceLocator;
    }

    public CqlException(Throwable cause, SourceLocator sourceLocator) {
        this(cause);
        this.sourceLocator = sourceLocator;
    }

    public CqlException(String message, SourceLocator sourceLocator, Severity severity) {
        this(message, sourceLocator);
        this.severity = severity;
    }

    public CqlException(String message, Throwable cause, SourceLocator sourceLocator, Severity severity) {
        this(message, cause, sourceLocator);
        this.severity = severity;
    }

    private Severity severity = Severity.ERROR;
    public Severity getSeverity() {
        return severity;
    }

    private SourceLocator sourceLocator;
    public SourceLocator getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(SourceLocator sourceLocator) {
        this.sourceLocator = sourceLocator;
    }
}
