package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.dstu3.JpaResourceProviderDstu3;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.util.Collection;

/**
 * Created by Chris Schuler on 12/15/2016.
 */
public class JpaFhirDataProvider extends BaseFhirDataProvider {

    // need these to access the dao
    private Collection<IResourceProvider> providers;

    public JpaFhirDataProvider(Collection<IResourceProvider> providers) {
        this.providers = providers;
    }

    private String endpoint;
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);
    }
    public JpaFhirDataProvider withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return this;
    }

    private TerminologyProvider terminologyProvider;
    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }
    public void setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
    }

    private boolean expandValueSets;
    public boolean getExpandValueSets() {
        return expandValueSets;
    }
    public void setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
    }

    private IGenericClient fhirClient;

    // TODO: It would be nice not to have to expose this, but I needed it in the MeasureEvaluator....
    public IGenericClient getFhirClient() {
        return fhirClient;
    }

    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange)
    {

        SearchParameterMap map = new SearchParameterMap();

        if (templateId != null && !templateId.equals("")) {
            // do something?
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (dataType == null) {
            throw new IllegalArgumentException("A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
        }

        if (context != null && context.equals("Patient") && contextValue != null) {
            map.add(getPatientSearchParam(dataType), (ReferenceAndListParam)contextValue);
        }

        if (codePath != null && !codePath.equals("")) {

            if (terminologyProvider != null && expandValueSets) {
                ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                codes = terminologyProvider.expand(valueSetInfo);
            }
//            else {
//                map.add(convertPathToSearchParam(dataType, codePath), );
//            }
            if (codes != null) {
                for (Code code : codes) {
                    TokenParam codeParam = new TokenParam(code.getSystem(), code.getCode());
                    map.add(convertPathToSearchParam(dataType, codePath), codeParam);
                }
            }
        }

        if (dateRange != null) {
            DateParam low = null;
            DateParam high = null;
            if (dateRange.getLow() != null) {
                low = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath));
            }

            if (dateRange.getHigh() != null) {
                high = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath));
            }
            DateRangeParam rangeParam;
            if (low == null && high != null) {
                rangeParam = new DateRangeParam(high);
            }
            else if (high == null && low != null) {
                rangeParam = new DateRangeParam(low);
            }
            else
                rangeParam = new DateRangeParam(high, low);
            map.add(convertPathToSearchParam(dataType, datePath), rangeParam);
        }

        return (Iterable<Object>) resolveResourceProvider(dataType).getDao().search(map);
    }

    public JpaResourceProviderDstu3<? extends IAnyResource> resolveResourceProvider(String datatype) {
        String className = datatype + "ResourceProvider";
        for (IResourceProvider resProvider : providers) {
            if (resProvider.getResourceType().getSimpleName().equals(className))
                return (JpaResourceProviderDstu3<? extends IAnyResource>) resProvider;
        }
        throw new IllegalArgumentException("Unable to resolve resource provider for :" + datatype);
    }

    private String getPatientSearchParam(String dataType) {
        switch (dataType) {
            case "Patient":
                return "_id";
            case "Observation":
            case "RiskAssessment":
                return "subject";
            default: return "patient";
        }
    }

    private String convertPathToSearchParam(String dataType, String codePath) {
        return codePath.replace('.', '-');
    }
}
