package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

public class FhirQueryGeneratorFactory {
    /**
     * Creates a FHIR version-specific FhirQueryGenerator
     * @param fhirVersionEnum the version of FHIR to create a converter for
     * @param searchParameterResolver the SearchParameterResolver instance the Generator should use
     * @param terminologyProvider the TerminologyProvider instance the Generator should use
     * @return a BaseFhirQueryGenerator
     * @throws IllegalArgumentException if the FHIR version specified is not supported
     */
    public BaseFhirQueryGenerator create(FhirVersionEnum fhirVersionEnum, SearchParameterResolver searchParameterResolver,
                                         TerminologyProvider terminologyProvider) {
        switch (fhirVersionEnum) {
            case DSTU3:
                return new Dstu3FhirQueryGenerator(searchParameterResolver, terminologyProvider);
            case R4:
                return new R4FhirQueryGenerator(searchParameterResolver, terminologyProvider);
            default:
                throw new IllegalArgumentException(String.format("Unsupported FHIR version for FHIR Query Generation: %s", fhirVersionEnum));
        }
    }

    /**
     * Creates a FHIR version-specific FhirQueryGenerator
     * @param fhirVersionEnum the version of FHIR to create a converter for
     * @param searchParameterResolver the SearchParameterResolver instance the Generator should use
     * @param terminologyProvider the TerminologyProvider instance the Generator should use
     * @param shouldExpandValueSets configuration indicating whether or not ValueSets should be expanded for querying
     *                              via list of codes as opposed to using the :in modifier.
     * @param maxCodesPerQuery configuration indicating how many codes, at most, should be included on a query string
     * @param pageSize configuration indicating what the _count should be on the query
     * @return a BaseFhirQueryGenerator
     * @throws IllegalArgumentException if the FHIR version specified is not supported
     */
    public BaseFhirQueryGenerator create(FhirVersionEnum fhirVersionEnum, SearchParameterResolver searchParameterResolver,
                                         TerminologyProvider terminologyProvider, Boolean shouldExpandValueSets,
                                         Integer maxCodesPerQuery, Integer pageSize) {
        BaseFhirQueryGenerator baseFhirQueryGenerator = create(fhirVersionEnum, searchParameterResolver, terminologyProvider);
        if (shouldExpandValueSets != null) {
            baseFhirQueryGenerator.setExpandValueSets(shouldExpandValueSets);
        }
        if (maxCodesPerQuery != null) {
            baseFhirQueryGenerator.setMaxCodesPerQuery(maxCodesPerQuery);
        }
        if (pageSize != null) {
            baseFhirQueryGenerator.setPageSize(pageSize);
        }

        return baseFhirQueryGenerator;
    }
}
