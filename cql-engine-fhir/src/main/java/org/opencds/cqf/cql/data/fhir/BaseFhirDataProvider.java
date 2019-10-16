package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.data.CompositeDataProvider;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.data.RetrieveProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirDataProvider extends CompositeDataProvider {

    public FhirDataProvider(FhirContext fhirContext, RetrieveProvider retrieveProvider) {
        super(new FhirTypeProvider(fhirContext), retrieveProvider);
    }

    // Data members
    protected FhirContext fhirContext;
  


    // getters & setters
    public FhirContext getFhirContext() {
        return fhirContext;
    }
    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        fhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 10000);
    }

    public String getEndpoint() {
        return endpoint;
    }
    public BaseFhirDataProvider setEndpoint(String endpoint){
        this.endpoint = endpoint;
        this.fhirClient = getFhirContext().newRestfulGenericClient(endpoint);
        return this;
    }

    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }
    public BaseFhirDataProvider setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
        return this;
    }

    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public BaseFhirDataProvider setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
        return this;
    }

    public boolean isSearchUsingPOST() {
        return searchUsingPOST;
    }
    public BaseFhirDataProvider setSearchUsingPOST (boolean searchUsingPOST) {
        this.searchUsingPOST = searchUsingPOST;
        expandValueSets = true;
        return this;
    }

    public IGenericClient getFhirClient() {
        return fhirClient;
    }


        // DataProvider methods
    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String contextPath, String dataType, String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath, Interval dateRange) {
        return null;
    }

    protected String convertPathToSearchParam(String type, String path) {
        path = path.replace(".value", "");
        switch (type) {
            case "Condition":
                if (path.equals("bodySite")) return "body-site";
                else if (path.equals("clinicalStatus")) return "clinical-status";
                else if (path.equals("dateRecorded")) return "date-recorded";
                else if (path.contains("evidence")) return "evidence";
                else if (path.equals("onsetDateTime")) return "onset";
                else if (path.equals("onsetPeriod")) return "onset";
                else if (path.contains("onset")) return "onset-info";
                else if (path.contains("stage")) return "stage";
                break;
            case "DiagnosticOrder":
                if (path.contains("actor")) return "actor";
                else if (path.contains("bodySite")) return "bodysite";
                else if (path.contains("code")) return "code";
                else if (path.contains("item") && path.contains("dateTime")) return "item-date";
                else if (path.contains(("dateTime"))) return "event-date";
                else if (path.contains("item") && path.contains("event") && path.contains("status")) return "item-past-status";
                else if (path.contains("item") && path.contains("status")) return "item-status";
                else if (path.contains(("status"))) return "event-status";
                else if (path.contains(("specimen"))) return "specimen";
            case "MedicationOrder":
                if (path.equals("medicationCodeableConcept")) return "code";
                else if (path.equals("medicationReference")) return "medication";
                else if (path.contains("dateWritten")) return "datewritten";
                break;
            case "NutritionOrder":
                if (path.contains("additiveType")) return "additive";
                else if (path.equals("dateTime")) return "datetime";
                else if (path.contains("baseFormulaType")) return "formula";
                else if (path.contains("oralDiet")) return "oraldiet";
                else if (path.equals("orderer")) return "provider";
                else if (path.contains("supplement")) return "supplement";
                break;
            case "VisionPrescription":
                if (path.equals("dateWritten")) return "datewritten";
                break;
            default:
                if (path.startsWith("effective")) return "date";
                else if (path.equals("period")) return "date";
                else if (path.equals("vaccineCode")) return "vaccine-code";
                break;
        }
        return path.replace('.', '-').toLowerCase();
    }
    

}
