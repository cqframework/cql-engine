package org.opencds.cqf.cql.retrieve;

import java.util.List;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public class R4FhirRetrieveProvider extends FhirRetrieveProvider {

	@Override
	protected boolean isPatientCompartment(String dataType) {
		switch (dataType) {
		case "Account":
		case "AdverseEvent":
		case "AllergyIntolerance":
		case "Appointment":
		case "AppointmentResponse":
		case "AuditEvent":
		case "Basic":
		case "BodyStructure":
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
		case "CoverageEligibilityRequest":
		case "CoverageEligibilityResponse":
		case "DetectedIssue":
		case "DeviceRequest":
		case "DeviceUseStatement":
		case "DiagnosticReport":
		case "DocumentManifest":
		case "DocumentReference":
		case "Encounter":
		case "EnrollmentRequest":
		case "EpisodeOfCare":
		case "ExplanationOfBenefit":
		case "FamilyMemberHistory":
		case "Flag":
		case "Goal":
		case "Group":
		case "ImagingStudy":
		case "Immunization":
		case "ImmunizationEvaluation":
		case "ImmunizationRecommendation":
		case "Invoice":
		case "List":
		case "MeasureReport":
		case "Media":
		case "MedicationAdministration":
		case "MedicationDispense":
		case "MedicationRequest":
		case "MedicationStatement":
		case "MolecularSequence":
		case "NutritionOrder":
		case "Observation":
		case "Patient":
		case "Person":
		case "Procedure":
		case "Provenance":
		case "QuestionnaireResponse":
		case "RelatedPerson":
		case "RequestGroup":
		case "ResearchSubject":
		case "RiskAssessment":
		case "Schedule":
		case "Specimen":
		case "SupplyDelivery":
		case "SupplyRequest":
		case "VisionPrescription":
			return true;
		default:
			return false;
		}
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		// TODO Auto-generated method stub
		return null;
	}
}