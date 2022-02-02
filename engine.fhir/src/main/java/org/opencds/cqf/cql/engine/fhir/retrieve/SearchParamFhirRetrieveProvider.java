package org.opencds.cqf.cql.engine.fhir.retrieve;

import java.util.List;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.opencds.cqf.cql.engine.fhir.exception.FhirVersionMisMatchException;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.retrieve.TerminologyAwareRetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;

import ca.uhn.fhir.context.FhirContext;

public abstract class SearchParamFhirRetrieveProvider extends TerminologyAwareRetrieveProvider {

    private static final int DEFAULT_MAX_CODES_PER_QUERY = 64;

    protected FhirContext fhirContext;
    protected SearchParameterResolver searchParameterResolver;
    protected Integer pageSize;
    protected int maxCodesPerQuery;
    private BaseFhirQueryGenerator fhirQueryGenerator;
    private ModelResolver modelResolver;

    protected SearchParamFhirRetrieveProvider(SearchParameterResolver searchParameterResolver) {
        this.searchParameterResolver = searchParameterResolver;
        this.fhirContext = searchParameterResolver.getFhirContext();
        this.maxCodesPerQuery = DEFAULT_MAX_CODES_PER_QUERY;
    }

    protected SearchParamFhirRetrieveProvider(SearchParameterResolver searchParameterResolver, ModelResolver modelResolver) {
        this(searchParameterResolver);
        this.modelResolver = modelResolver;
    }

    public void setPageSize(Integer value) {
        if (value == null || value < 1) {
            throw new IllegalArgumentException("value must be a non-null integer > 0");
        }

        this.pageSize = value;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public BaseFhirQueryGenerator getFhirQueryGenerator() {
        return fhirQueryGenerator;
    }

    public void setFhirQueryGenerator(BaseFhirQueryGenerator fhirQueryGenerator) {
        this.fhirQueryGenerator = fhirQueryGenerator;
    }

    public ModelResolver getModelResolver() {
        return modelResolver;
    }

    public void setModelResolver(ModelResolver modelResolver) {
        this.modelResolver = modelResolver;
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
    public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange) {

        List<SearchParameterMap> queries = null;

        if (this.fhirContext != null && modelResolver != null) {
            try {
                if (this.fhirContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
                    fhirQueryGenerator = new Dstu3FhirQueryGenerator(searchParameterResolver, terminologyProvider, (Dstu3FhirModelResolver) this.modelResolver);
                } else if (this.fhirContext.getVersion().getVersion().equals(FhirVersionEnum.R4)) {
                    fhirQueryGenerator = new R4FhirQueryGenerator(searchParameterResolver, terminologyProvider, (R4FhirModelResolver) this.modelResolver);
                }
            } catch (FhirVersionMisMatchException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }
        if (fhirQueryGenerator != null) {
            fhirQueryGenerator.setExpandValueSets(this.expandValueSets);
            fhirQueryGenerator.setMaxCodesPerQuery(maxCodesPerQuery);
            if (pageSize != null) {
                fhirQueryGenerator.setPageSize(pageSize);
            }

            queries = fhirQueryGenerator.setupQueries(context, contextPath, contextValue, dataType,
                templateId, codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);
        }

        return this.executeQueries(dataType, queries);
    }
}