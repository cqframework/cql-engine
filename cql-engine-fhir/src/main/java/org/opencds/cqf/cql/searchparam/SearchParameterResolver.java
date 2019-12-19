package org.opencds.cqf.cql.searchparam;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.RuntimeSearchParam.RuntimeSearchParamStatusEnum;
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
        Map<String, RuntimeSearchParam> params = this.searchParamRegistry.getActiveSearchParams(dataType);

        String combinedPath = String.join(".", dataType, path);
        
        for (Entry<String, RuntimeSearchParam> entry : params.entrySet()) {
            RuntimeSearchParam param = entry.getValue();
            if (param.getStatus() != RuntimeSearchParamStatusEnum.RETIRED && param.getPath().equals(combinedPath))  {
                if (paramType == null || param.getParamType().equals(paramType)) {
                    return param;
                }
            }
        }

        return null;
    }

    public Pair<String, IQueryParameterType> createSearchParameter(String dataType, String path, String value) {
        if (path == null || dataType == null) {
            return null;
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
}