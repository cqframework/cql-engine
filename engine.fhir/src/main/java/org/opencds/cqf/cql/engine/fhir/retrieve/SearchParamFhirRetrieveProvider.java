package org.opencds.cqf.cql.engine.fhir.retrieve;

import java.util.List;

import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.retrieve.TerminologyModelAwareRetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;

import ca.uhn.fhir.context.FhirContext;

public abstract class SearchParamFhirRetrieveProvider extends TerminologyModelAwareRetrieveProvider {

    private static final int DEFAULT_MAX_CODES_PER_QUERY = 64;

    protected FhirContext fhirContext;
    protected SearchParameterResolver searchParameterResolver;
    protected Integer pageSize;
    protected int maxCodesPerQuery;

    protected SearchParamFhirRetrieveProvider(SearchParameterResolver searchParameterResolver) {
        this.searchParameterResolver = searchParameterResolver;
        this.fhirContext = searchParameterResolver.getFhirContext();
        this.maxCodesPerQuery = DEFAULT_MAX_CODES_PER_QUERY;
    }

    public void setPageSize(Integer value) {
        if( value == null || value < 1 ) {
            throw new IllegalArgumentException("value must be a non-null integer > 0");
        }

        this.pageSize = value;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setMaxCodesPerQuery(int value) {

        if (value < 1) {
            throw new IllegalArgumentException("value must be > 0");
        }

        this.maxCodesPerQuery = value;
    }

    public int getMaxCodesPerQuery() {
        return this.maxCodesPerQuery;
    }

    protected abstract Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries);


    @Override
    public Iterable<Object> retrieve(ModelResolver modelResolver, String context, String contextPath, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange) {

        BaseFhirQueryGenerator fhirQueryGenerator =
            FhirQueryGeneratorFactory.create(modelResolver, searchParameterResolver,
                terminologyProvider, this.expandValueSets, this.maxCodesPerQuery, this.pageSize);

        List<SearchParameterMap> queries = fhirQueryGenerator.setupQueries(context, contextPath, contextValue, dataType,
            templateId, codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);

        return this.executeQueries(dataType, queries);
    }
}