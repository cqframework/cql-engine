package org.cqframework.cql.terminology.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.terminology.CodeSystemInfo;
import org.cqframework.cql.terminology.TerminologyProvider;
import org.cqframework.cql.terminology.ValueSetInfo;

/**
 * Created by Bryn on 8/15/2016.
 */
public class FhirTerminologyProvider implements TerminologyProvider {
    private String endpoint;
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        fhirContext = FhirContext.forDstu3();
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);
    }
    public FhirTerminologyProvider withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return this;
    }

    private FhirContext fhirContext;
    private IGenericClient fhirClient;

    @Override
    public boolean in(Code code, ValueSetInfo valueSet) {
        // TODO: Implement as ValueSet/$validate-code
        // http://hl7.org/fhir/2016Sep/valueset-operations.html#validate-code

        return false;
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) {
        // TODO: Implement as ValueSet/$expand
        // http://hl7.org/fhir/2016Sep/valueset-operations.html#expand
        return null;
    }

    @Override
    public Code lookup(Code code, CodeSystemInfo codeSystem) {
        // TODO: Implement as CodeSystem/$lookup
        // http://hl7.org/fhir/2016Sep/codesystem-operations.html#lookup
        return null;
    }
}
