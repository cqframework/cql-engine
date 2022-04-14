package org.opencds.cqf.cql.engine.fhir.exception;

public class FhirVersionMisMatchException extends Exception{
    private static final long serialVersionUID = 01L;

    String message;

    /* Constructor of custom FhirVersionMisMatchException class
     * @param str is the description of the exception
     */
    public FhirVersionMisMatchException(String str) {
        super(str);
        message = str;
    }

    public String toString() {
        return ("FhirVersionMisMatchException occurred: " + message);
    }
}
