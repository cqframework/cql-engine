package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.*;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.retrieve.TerminologyAwareRetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import java.util.*;

public class FhirQueryGenerator {
    //From SearchParameterFhirRetrieveProvider
    private static final int DEFAULT_MAX_CODES_PER_QUERY = 64;

    private SearchParameterResolver searchParameterResolver;
    private Integer pageSize;
    private int maxCodesPerQuery;

    //From TerminologyAwareRetrieveProvider
    // TODO: Think about how to best handle the decision to expand value sets... Should it be part of the
    // terminology provider if it detects support for "code:in"? How does that feed back to the retriever?
    protected boolean expandValueSets;
    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public void setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
    }

    ///////////////
    private final int DEFAULT_MAX_URI_LENGTH = 8000;
    private int maxUriLength;

    private FhirContext context;
//    private IGenericClient fhirClient;
    private TerminologyProvider terminologyProvider;

//    private SearchParameterResolver searchParameterResolver;

//    public FhirQueryGenerator(IGenericClient fhirClient, SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProvider) {
    public FhirQueryGenerator(SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProviders) {
        this.searchParameterResolver = searchParameterResolver;
        this.terminologyProvider = terminologyProvider;
        this.maxCodesPerQuery = DEFAULT_MAX_CODES_PER_QUERY;

        this.maxUriLength = DEFAULT_MAX_URI_LENGTH;

        this.context = FhirContext.forR4();//fhirClient.getFhirContext();
//        this.fhirClient = fhirClient;
//        this.terminologyProvider = terminologyProvider;
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

    public int getMaxUriLength() {
        return this.maxUriLength;
    }

    public void setMaxUriLength(int maxUriLength) {
        if (maxUriLength <= 0) {
            throw new IllegalArgumentException("maxUriLength must be > 0");
        }

        this.maxUriLength = maxUriLength;
    }

//    public List<String> generateFhirQueries(List<org.hl7.fhir.r4.model.DataRequirement> dataRequirements, org.hl7.fhir.r4.model.CapabilityStatement capabilityStatement) {
//        List<String> queries = new ArrayList<>();
//
//        for (org.hl7.fhir.r4.model.DataRequirement dataRequirement : dataRequirements) {
//            RuntimeResourceDefinition resourceDef = context.getResourceDefinition(dataRequirement.getType());
//            if (!isPatientCompartmentResource(resourceDef)) return null;
//
////            List<RuntimeSearchParam> params = resourceDef.getSearchParams();
////            List<RuntimeSearchParam> patientSearchParams = getPatientSearchParams(resourceDef);
//            RuntimeSearchParam preferredPatientSearchParam = searchParameterResolver.getPreferredPatientSearchParam(resourceDef);
//
//            String patientRelatedResource = dataRequirement.getType() + "?" + preferredPatientSearchParam.getName() + "=Patient/" + PATIENT_ID_CONTEXT;
//            if (dataRequirement.hasCodeFilter()) {
//                for (org.hl7.fhir.r4.model.DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent : dataRequirement.getCodeFilter()) {
//                    if (!codeFilterComponent.hasPath()) continue;
//                    String path = codeFilterComponent.getPath();
//                    //NOTE: Not sure yet why the mapping was required
////                    String path = mapCodePathToSearchParam(dataRequirement.getType(), codeFilterComponent.getPath());
//                    if (codeFilterComponent.hasValueSetElement()) {
//                        for (String codes : resolveValueSetCodes(codeFilterComponent.getValueSet())) {
//                            queries.add(patientRelatedResource + "&" + path + "=" + codes);
//                        }
//                    }
//                    else if (codeFilterComponent.hasCode()) {
//                        List<org.hl7.fhir.r4.model.Coding> codeFilterValueCodings = codeFilterComponent.getCode();
//                        boolean isFirstCodingInFilter = true;
//                        StringBuilder queryBuilder = new StringBuilder();
//                        for (String code : resolveValueCodingCodes(codeFilterValueCodings)) {
//                            if (isFirstCodingInFilter) {
//                                queryBuilder.append(patientRelatedResource + "&" + path + "=" + code);
//                            } else {
//                                queryBuilder.append("," + code);
//                            }
//
//                            isFirstCodingInFilter = false;
//                        }
//
//                        if (queryBuilder.length() > 0) {
//                            queries.add(queryBuilder.toString());
//                        }
//                    }
//                }
//            }
//            else {
//                queries.add(patientRelatedResource);
//            }
//
////            if (dataRequirement.hasDateFilter()) {
////
////            }
//
////            capabilityStatement.get
//        }
//
//        return queries;
//    }

    public List<String> generateFhirQueries(org.hl7.fhir.r4.model.DataRequirement dataRequirement, org.hl7.fhir.r4.model.CapabilityStatement capabilityStatement) {
        List<String> queries = new ArrayList<>();

        String codePath = null;
        List<Code> codes = null;
        String valueSet = null;

        if (dataRequirement.hasCodeFilter()) {
            for (org.hl7.fhir.r4.model.DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent : dataRequirement.getCodeFilter()) {
                if (!codeFilterComponent.hasPath()) continue;
                codePath = codeFilterComponent.getPath();

                if (codeFilterComponent.hasValueSetElement()) {
                    valueSet = codeFilterComponent.getValueSet();
                }
                else if (codeFilterComponent.hasCode()) {
                    codes = new ArrayList<Code>();

                    List<org.hl7.fhir.r4.model.Coding> codeFilterValueCodings = codeFilterComponent.getCode();
                    for (Coding coding : codeFilterValueCodings) {
                        if (coding.hasCode()) {
                            Code code = new Code();
                            code.setSystem(coding.getSystem());
                            code.setCode(coding.getCode());
                            codes.add(code);
                        }
                    }
                }
            }
        }

        List<SearchParameterMap> maps = new ArrayList<SearchParameterMap>();
        maps = setupQueries(null, null, null, dataRequirement.getType(), null,
            codePath, codes, valueSet, null, null, null, null);

        for (SearchParameterMap map : maps) {
            String query = map.toNormalizedQueryString(context);
            queries.add(dataRequirement.getType() + query);
        }

        return queries;
    }

    public boolean isPatientCompartmentResource(RuntimeResourceDefinition resourceDef) {
        return searchParameterResolver.getPatientSearchParams(resourceDef).size() > 0;
//        boolean result = true;
//
//        List<RuntimeSearchParam>  patientCompartmentSearchParams = resourceDef.getSearchParamsForCompartmentName("Patient");
//        if (patientCompartmentSearchParams != null && patientCompartmentSearchParams.size() > 0) {
//            result = true;
//        }
//
//        return result;
    }

//    public List<String> resolveValueCodingCodes(List<Coding> valueCodings) {
//        List<String> result = new ArrayList<>();
//
//        StringBuilder codes = new StringBuilder();
//        for (Coding coding : valueCodings) {
//            if (coding.hasCode()) {
//                String system = coding.getSystem();
//                String code = coding.getCode();
//
//                codes = getCodesStringBuilder(result, codes, system, code);
//            }
//        }
//
//        result.add(codes.toString());
//        return result;
//    }

//    public List<String> resolveValueSetCodes(String valueSetId) {
//        Bundle bundle = (Bundle)fhirClient.search().forResource("ValueSet").where(ValueSet.URL.matches().value(valueSetId)).execute();
//        if (bundle == null) {
//            // TODO: report missing terminology
//            return null;
//        }
//        List<String> result = new ArrayList<>();
//
//        StringBuilder codes = new StringBuilder();
//        if (bundle.hasEntry() && bundle.getEntry().size() == 1) {
//            if (bundle.getEntry().get(0).hasResource() && bundle.getEntry().get(0).getResource() instanceof ValueSet) {
//                ValueSet valueSet = (ValueSet) bundle.getEntry().get(0).getResource();
//                if (valueSet.hasExpansion() && valueSet.getExpansion().hasContains()) {
//                    for (ValueSet.ValueSetExpansionContainsComponent contains : valueSet.getExpansion().getContains()) {
//                        String system = contains.getSystem();
//                        String code = contains.getCode();
//
//                        codes = getCodesStringBuilder(result, codes, system, code);
//                    }
//                }
//                else if (valueSet.hasCompose() && valueSet.getCompose().hasInclude()) {
//                    for (ValueSet.ConceptSetComponent concepts : valueSet.getCompose().getInclude()) {
//                        String system = concepts.getSystem();
//                        if (concepts.hasConcept()) {
//                            for (ValueSet.ConceptReferenceComponent concept : concepts.getConcept()) {
//                                String code = concept.getCode();
//
//                                codes = getCodesStringBuilder(result, codes, system, code);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        result.add(codes.toString());
//        return result;
//    }
//
//    private StringBuilder getCodesStringBuilder(List<String> ret, StringBuilder codes, String system, String code) {
//        String codeToken = system + "|" + code;
//        int postAppendLength = codes.length() + codeToken.length();
//
//        if (codes.length() > 0 && postAppendLength < this.maxUriLength) {
//            codes.append(",");
//        }
//        else if (postAppendLength > this.maxUriLength) {
//            ret.add(codes.toString());
//            codes = new StringBuilder();
//        }
//        codes.append(codeToken);
//        return codes;
//    }



    // From SearchParamFhirRetrieveProvider
    protected Pair<String, IQueryParameterType> getTemplateParam(String dataType, String templateId) {
        if (templateId == null || templateId.equals("")) {
            return null;
        }

        // Do something?
        return null;
    }

    protected Pair<String, DateRangeParam> getDateRangeParam(String dataType, String datePath, String dateLowPath,
                                                             String dateHighPath, Interval dateRange) {
        if (dateRange == null) {
            return null;
        }

        DateParam low = null;
        DateParam high = null;
        if (dateRange.getLow() != null) {
            low = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS,
                Date.from(((DateTime) dateRange.getLow()).getDateTime().toInstant()));
        }

        if (dateRange.getHigh() != null) {
            high = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS,
                Date.from(((DateTime) dateRange.getHigh()).getDateTime().toInstant()));
        }

        DateRangeParam rangeParam;
        if (low == null && high != null) {
            rangeParam = new DateRangeParam(high);
        } else if (high == null && low != null) {
            rangeParam = new DateRangeParam(low);
        } else {
            rangeParam = new DateRangeParam(low, high);
        }

        RuntimeSearchParam dateParam = this.searchParameterResolver.getSearchParameterDefinition(dataType, datePath, RestSearchParameterTypeEnum.DATE);

        if (dateParam == null) {
            throw new UnsupportedOperationException(String.format("Could not resolve a search parameter with date type for %s.%s ", dataType, datePath));
        }

        return Pair.of(dateParam.getName(), rangeParam);
    }

//    protected Pair<String, IQueryParameterType> getContextParam(String dataType, String context, String contextPath,
//                                                                Object contextValue) {
//        if (context != null && context.equals("Patient") && contextValue != null && contextPath != null) {
////            return this.searchParameterResolver.createSearchParameter(context, dataType, contextPath, (String) contextValue);
//            return this.searchParameterResolver.getPreferredPatientSearchParam(dataType);
//        }
//
//        return null;
//    }

    protected Pair<String, List<TokenOrListParam>> getCodeParams(String dataType, String codePath, Iterable<Code> codes,
                                                                 String valueSet) {
        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (codePath == null || codePath.isEmpty()) {
            return null;
        }

        // TODO: This assumes the code path will always be a token param.
        List<TokenOrListParam> codeParamLists = this.getCodeParams(codes, valueSet);
        if (codeParamLists == null || codeParamLists.isEmpty()) {
            return null;
        }

        RuntimeSearchParam codeParam = this.searchParameterResolver.getSearchParameterDefinition(dataType, codePath, RestSearchParameterTypeEnum.TOKEN);

        if (codeParam == null) {
            return null;
        }

        return Pair.of(codeParam.getName(), codeParamLists);
    }

    // The code params will be either the literal set of codes in the event the data server doesn't have the referenced
    // ValueSet (or doesn't support pulling and caching a ValueSet). If the target server DOES support that then it's
    // "dataType.codePath in ValueSet"
    protected List<TokenOrListParam> getCodeParams(Iterable<Code> codes, String valueSet) {
        if (valueSet != null) {
            if (isExpandValueSets()) {
                if (this.terminologyProvider == null) {
                    throw new IllegalArgumentException(
                        "Expand value sets cannot be used without a terminology provider and no terminology provider is set.");
                }
                ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                codes = this.terminologyProvider.expand(valueSetInfo);
            } else {
                return Collections.singletonList(new TokenOrListParam()
                    .addOr(new TokenParam(valueSet).setModifier(TokenParamModifier.IN)));
            }
        }

        if (codes == null) {
            return Collections.emptyList();
        }

        List<TokenOrListParam> codeParamsList = new ArrayList<>();

        TokenOrListParam codeParams = null;
        int codeCount = 0;
        for (Object code : codes) {
            if (codeCount % this.maxCodesPerQuery == 0) {
                if (codeParams != null) {
                    codeParamsList.add(codeParams);
                }

                codeParams = new TokenOrListParam();
            }

            codeCount++;
            if (code instanceof Code) {
                Code c = (Code)code;
                codeParams.addOr(new TokenParam(c.getSystem(), c.getCode()));
            }
            else if (code instanceof String) {
                String s = (String)code;
                codeParams.addOr(new TokenParam(s));
            }

        }

        if (codeParams != null) {
            codeParamsList.add(codeParams);
        }

        return codeParamsList;
    }

    protected List<SearchParameterMap> setupQueries(String context, String contextPath, Object contextValue,
                                                    String dataType, String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                                    String dateLowPath, String dateHighPath, Interval dateRange) {

        Pair<String, IQueryParameterType> templateParam = this.getTemplateParam(dataType, templateId);
//        Pair<String, IQueryParameterType> contextParam = this.getContextParam(dataType, context, contextPath,
//            contextValue);
        Pair<String, IQueryParameterType> contextParam = this.searchParameterResolver.getPreferredPatientSearchParam(dataType);
        Pair<String, DateRangeParam> dateRangeParam = this.getDateRangeParam(dataType, datePath, dateLowPath,
            dateHighPath, dateRange);
        Pair<String, List<TokenOrListParam>> codeParams = this.getCodeParams(dataType, codePath, codes, valueSet);

        // In the case we filtered to a valueSet without codes, there are no possible results.
        if (valueSet != null && (codeParams == null || codeParams.getValue().isEmpty())) {
            return Collections.emptyList();
        }

        return this.innerSetupQueries(templateParam, contextParam, dateRangeParam, codeParams);
    }

    protected List<SearchParameterMap> innerSetupQueries(Pair<String, IQueryParameterType> templateParam,
                                                         Pair<String, IQueryParameterType> contextParam, Pair<String, DateRangeParam> dateRangeParam,
                                                         Pair<String, List<TokenOrListParam>> codeParams) {

        if (codeParams == null || codeParams.getValue() == null || codeParams.getValue().isEmpty()) {
            return Collections.singletonList(this.getBaseMap(templateParam, contextParam, dateRangeParam));
        }

        List<SearchParameterMap> maps = new ArrayList<>();
        for (TokenOrListParam tolp : codeParams.getValue()) {
            SearchParameterMap base = this.getBaseMap(templateParam, contextParam, dateRangeParam);
            base.add(codeParams.getKey(), tolp);
            maps.add(base);
        }

        return maps;
    }

    protected SearchParameterMap getBaseMap(Pair<String, IQueryParameterType> templateParam,
                                            Pair<String, IQueryParameterType> contextParam, Pair<String, DateRangeParam> dateRangeParam) {

        SearchParameterMap baseMap = new SearchParameterMap();
        baseMap.setLastUpdated(new DateRangeParam());

        if (this.pageSize != null) {
            baseMap.setCount(this.pageSize);
        }

        if (templateParam != null) {
            baseMap.add(templateParam.getKey(), templateParam.getValue());
        }

        if (dateRangeParam != null) {
            baseMap.add(dateRangeParam.getKey(), dateRangeParam.getValue());
        }

        if (contextParam != null) {
            baseMap.add(contextParam.getKey(), contextParam.getValue());
        }

        return baseMap;
    }
}