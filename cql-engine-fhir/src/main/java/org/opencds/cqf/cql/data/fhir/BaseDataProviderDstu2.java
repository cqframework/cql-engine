package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;

/**
 * Created by Christopher Schuler on 6/15/2017.
 */
public abstract class BaseDataProviderDstu2 extends BaseFhirDataProvider {

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes,
                                     String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange)
    {
        return null;
    }

    @Override
    protected String resolveClassName(String typeName) {
        return null;
    }

    @Override
    protected Object fromJavaPrimitive(Object value, Object target) {
        return null;
    }

    @Override
    protected Object toJavaPrimitive(Object result, Object source) {
        return null;
    }

    @Override
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
