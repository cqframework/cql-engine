package org.opencds.cqf.cql.engine.fhir.exception;

public class FhirVersionMisMatchException extends Exception{
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
