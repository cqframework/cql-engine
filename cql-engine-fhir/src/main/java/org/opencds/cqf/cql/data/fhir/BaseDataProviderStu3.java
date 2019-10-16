package org.opencds.cqf.cql.data.fhir;

public class BaseDataProviderStu3 extends BaseFhirDataProvider {

    @Override
    protected String convertPathToSearchParam(String type, String path) {
        path = path.replace(".value", "");
        if (path.equals("id")) {
            path = "_id";
        }
        switch (type) {
            case "AllergyIntolerance":
                if (path.equals("clinicalStatus")) return "clinical-status";
                else if (path.contains("substance")) return "code";
                else if (path.equals("assertedDate")) return "date";
                else if (path.equals("lastOccurrence")) return "last-date";
                else if (path.startsWith("reaction")) {
                    if (path.endsWith("manifestation")) return "manifestation";
                    else if (path.endsWith("onset")) return "onset";
                    else if (path.endsWith("exposureRoute")) return "route";
                    else if (path.endsWith("severity")) return "severity";
                }
                else if (path.equals("verificationStatus")) return "verification-status";
                break;
            case "Claim":
                if (path.contains("careTeam")) return "care-team";
                else if (path.contains("payee")) return "payee";
                break;
            case "Condition":
                if (path.equals("abatementDateTime")) return "abatement-date";
                else if (path.equals("abatementPeriod")) return "abatement-date";
                else if (path.equals("abatementRange")) return "abatement-age";
                else if (path.equals("onsetDateTime")) return "onset-date";
                else if (path.equals("onsetPeriod")) return "onset-date";
                else if (path.equals("onsetRange")) return "onset-age";
                break;
            case "MedicationDispense":
                if (path.equals("medication")) return "code";
                break;
            case "MedicationRequest":
                if (path.equals("authoredOn")) return "authoredon";
                else if (path.equals("medication")) return "code";
                else if (path.equals("medicationCodeableConcept")) return "code";
                else if (path.equals("medicationReference")) return "medication";
                else if (path.contains("event")) return "date";
                else if (path.contains("performer")) return "intended-dispenser";
                else if (path.contains("requester")) return "requester";
                break;
            case "MedicationStatement":
                if (path.equals("medication")) return "code";
                break;
            case "NutritionOrder":
                if (path.contains("additiveType")) return "additive";
                else if (path.equals("dateTime")) return "datetime";
                else if (path.contains("baseFormulaType")) return "formula";
                else if (path.contains("oralDiet")) return "oraldiet";
                else if (path.equals("orderer")) return "provider";
                else if (path.contains("supplement")) return "supplement";
                break;
            case "ProcedureRequest":
                if (path.equals("authoredOn")) return "authored";
                else if (path.equals("basedOn")) return "based-on";
                else if (path.equals("bodySite")) return "body-site";
                else if (path.equals("context")) return "encounter";
                else if (path.equals("performerType")) return "performer-type";
                else if (path.contains("requester")) return "requester";
                break;
            case "ReferralRequest":
                if (path.equals("authoredOn")) return "authored";
                else if (path.equals("basedOn")) return "based-on";
                else if (path.equals("context")) return "encounter";
                else if (path.equals("groupIdentifier")) return "group-identifier";
                else if (path.equals("occurrence")) return "occurrence-date";
                else if (path.contains("requester")) return "requester";
                else if (path.equals("serviceRequested")) return "service";
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
