package org.opencds.cqf.cql.engine.fhir.terminology;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

public class FhirTerminologyProviderFactory {
    /**
     * Creates a FHIR version-specific FhirQueryGenerator
     * @param fhirVersion the version of FHIR to create a converter for
     * @param fhirClient the IGenericClient the Provider should use
     * @return a TerminologyProvider
     */
    public TerminologyProvider create(FhirVersionEnum fhirVersion, IGenericClient fhirClient) {
        switch (fhirVersion) {
            case DSTU3:
                return new Dstu3FhirTerminologyProvider(fhirClient);
            case R4:
                return new R4FhirTerminologyProvider(fhirClient);
            default:
                throw new IllegalArgumentException(String.format("Unsupported FHIR version for TerminologyProvider: %s", fhirVersion));
        }
    }
}