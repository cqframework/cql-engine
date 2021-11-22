package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DataRequirement;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class R4FhirQueryGenerator extends BaseFhirQueryGenerator {
    public R4FhirQueryGenerator(SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProvider) {
        super(searchParameterResolver, terminologyProvider, FhirContext.forR4());
    }

    public List<String> generateFhirQueries(DataRequirement dataRequirement, CapabilityStatement capabilityStatement) {
        List<String> queries = new ArrayList<>();

        String codePath = null;
        List<Code> codes = null;
        String valueSet = null;

        if (dataRequirement.hasCodeFilter()) {
            for (DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent : dataRequirement.getCodeFilter()) {
                if (!codeFilterComponent.hasPath()) continue;
                codePath = codeFilterComponent.getPath();

                if (codeFilterComponent.hasValueSetElement()) {
                    valueSet = codeFilterComponent.getValueSet();
                }

                if (codeFilterComponent.hasCode()) {
                    codes = new ArrayList<Code>();

                    List<Coding> codeFilterValueCodings = codeFilterComponent.getCode();
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

        String datePath = null;
        String dateLowPath = null;
        String dateHighPath = null;
        Interval dateRange = null;
//        if (dataRequirement.hasDateFilter()) {
//            for (DataRequirement.DataRequirementDateFilterComponent dateFilterComponent : dataRequirement.getDateFilter()) {
//                if (dateFilterComponent.hasPath() && dateFilterComponent.hasSearchParam()) {
//                    throw new UnsupportedOperationException(String.format("Either a path or a searchParam must be provided, but not both"));
//                }
//
//                if (dateFilterComponent.hasPath()) {
//                    datePath = dateFilterComponent.getPath();
//                } else if (dateFilterComponent.hasSearchParam()) {
//                    datePath = dateFilterComponent.getSearchParam();
//                }
//
//                Type dateFilterValue = dateFilterComponent.getValue();
//                if (dateFilterValue instanceof DateTimeType) {
//
//                } else if (dateFilterValue instanceof Duration) {
//
//                } else if (dateFilterValue instanceof Period) {
//
//                }
//            }
//        }

        List<SearchParameterMap> maps = new ArrayList<SearchParameterMap>();
        maps = setupQueries(null, null, null, dataRequirement.getType(), null,
            codePath, codes, valueSet, datePath, null, null, null);

        for (SearchParameterMap map : maps) {
            String query = null;
            try {
                query = URLDecoder.decode(map.toNormalizedQueryString(context), "UTF-8");
            } catch (Exception ex) {
                query = map.toNormalizedQueryString(context);
            }
            queries.add(dataRequirement.getType() + query);
        }

        return queries;
    }
}