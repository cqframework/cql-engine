package org.opencds.cqf.cql.searchparam;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.*;

public class SearchParameterResolver {

    private ISearchParamRegistry searchParamRegistry;

    public SearchParameterResolver(ISearchParamRegistry searchParamRegistry) {
        this.searchParamRegistry = searchParamRegistry;
    }

    public RuntimeSearchParam getSearchParameterDefinition(String dataType, String path) {
        return this.getSearchParameterDefinition(dataType, path, (RestSearchParameterTypeEnum)null);
    }

    public RuntimeSearchParam getSearchParameterDefinition(String dataType, String path, RestSearchParameterTypeEnum paramType) {
        if (dataType == null || path == null) {
            return null;
        }

        Map<String, RuntimeSearchParam> params = this.searchParamRegistry.getActiveSearchParams(dataType);

        // There are certain cases where the path changes depending on the requested data type
        // MedicationRequest.medication.as(CodeableConcept)
        // MedicationRequest.medication.as(Reference)
        // So this handles the case that it's been written as such
        String castPath = getCastPath(paramType);

        String combinedPath = String.join(".", dataType, path);

        if (castPath != null) {
            castPath = String.join(".", combinedPath, castPath);
        }
        
        // TODO: There's an option to get paths as parts, and comparing path segment by segment
        // could be more robust. Something to think about.
        for (Entry<String, RuntimeSearchParam> entry : params.entrySet()) {
            RuntimeSearchParam param = entry.getValue();
            if (param.getPath().equals(combinedPath) || (castPath != null && param.getPath().equals(castPath))) {
                // Regardless of whether the path matches, if we've requested a specific type it must match
                if (paramType == null || param.getParamType().equals(paramType)) {
                    return param;
                }
            }
        }

        return null;
    }

    public Pair<String, IQueryParameterType> createSearchParameter(String dataType, String path, String value) {
        // Special case for Id
        // TODO: all the other "system" parameters (_language, etc.)
        if (path.equals("id")) {
            return Pair.of("_id", new TokenParam(value));
        }


        RuntimeSearchParam searchParam = this.getSearchParameterDefinition(dataType, path);
        if (searchParam == null) {
            return null;
        }

        String name = searchParam.getName();

        switch (searchParam.getParamType()) {

            case TOKEN:
                return Pair.of(name, new TokenParam(value));
            case REFERENCE:
                return Pair.of(name, new ReferenceParam(value));
            case QUANTITY:
                return Pair.of(name, new QuantityParam(value));
            case STRING:
                return Pair.of(name, new StringParam(value));
            case NUMBER: 
                return Pair.of(name, new NumberParam(value));
            case URI:
                return Pair.of(name, new UriParam(value));

            // Don't know how to handle these yet.
            case DATE:
            case HAS:
            case COMPOSITE:
            case SPECIAL:
        }

        return null;
    }

    private String getCastPath(RestSearchParameterTypeEnum paramType)
    {
        if (paramType == null) {
            return null;
        }

        switch(paramType) {
            case TOKEN:
                return "as(CodeableConcept)";
            case REFERENCE:
                return "as(Reference)";

            default:
                return null;
        }
    }
}