package org.opencds.cqf.cql.retrieve;

public abstract class Dstu3FhirRetrieveProvider extends FhirRetrieveProvider {
	
	@Override
	protected boolean isPatientCompartment(String dataType)
    {
        switch (dataType)
        {
            case "Account":
            case "AdverseEvent":
            case "AllergyIntolerance":
            case "Appointment":
            case "AppointmentResponse":
            case "AuditEvent":
            case "Basic":
            case "BodySite":
            case "CarePlan":
            case "CareTeam":
            case "ChargeItem":
            case "Claim":
            case "ClaimResponse":
            case "ClinicalImpression":
            case "Communication":
            case "CommunicationRequest":
            case "Composition":
            case "Condition":
            case "Consent":
            case "Coverage":
            case "DetectedIssue":
            case "DeviceRequest":
            case "DeviceUseStatement":
            case "DiagnosticReport":
            case "DocumentManifest":
            case "EligibilityRequest":
            case "Encounter":
            case "EnrollmentRequest":
            case "EpisodeOfCare":
            case "ExplanationOfBenefit":
            case "FamilyMemberHistory":
            case "Flag":
            case "Goal":
            case "Group":
            case "ImagingManifest":
            case "ImagingStudy":
            case "Immunization":
            case "ImmunizationRecommendation":
            case "List":
            case "MeasureReport":
            case "Media":
            case "MedicationAdministration":
            case "MedicationDispense":
            case "MedicationRequest":
            case "MedicationStatement":
            case "NutritionOrder":
            case "Observation":
            case "Patient":
            case "Person":
            case "Procedure":
            case "ProcedureRequest":
            case "Provenance":
            case "QuestionnaireResponse":
            case "ReferralRequest":
            case "RelatedPerson":
            case "RequestGroup":
            case "ResearchSubject":
            case "RiskAssessment":
            case "Schedule":
            case "Specimen":
            case "SupplyDelivery":
            case "SupplyRequest":
            case "VisionPrescription": return true;
            default: return false;
        }
	}
}