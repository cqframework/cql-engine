package org.opencds.cqf.cql.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public class HL7FhirModelResolver extends Dstu3FhirModelResolver {

	public HL7FhirModelResolver() {
		this.fhirContext = FhirContext.forDstu2Hl7Org();
    }
    
    public HL7FhirModelResolver(FhirContext fhirContext) {
        super(fhirContext);
        
        if (fhirContext.getVersion().getVersion() != FhirVersionEnum.DSTU2_HL7ORG) {
            throw new IllegalArgumentException("The supplied context is not configured for DSTU2_HL7ORG");
        }
	}

	@Override
	protected void setPackageName() {
		this.packageName = "org.hl7.fhir.instance.model";
	}

	@Override
	public String resolveClassName(String typeName) {
		// TODO: Obviously would like to be able to automate this, but there is no programmatic way of which I'm aware
        // For the primitive types, not such a big deal.
        // For the enumerations, the type names are generated from the binding name in the spreadsheet, which doesn't make it to the StructureDefinition,
        // and the schema has no way of indicating whether the enum will be common (i.e. in Enumerations) or per resource
        switch (typeName) {
            // primitives
            case "base64Binary": typeName = "Base64BinaryType"; break;
            case "boolean": typeName = "BooleanType"; break;
            case "dateTime": typeName = "DateTimeType"; break;
            case "date": typeName = "DateType"; break;
            case "decimal": typeName = "DecimalType"; break;
            case "instant": typeName = "InstantType"; break;
            case "integer": typeName = "IntegerType"; break;
            case "positiveInt": typeName = "PositiveIntType"; break;
            case "unsignedInt": typeName = "UnsignedIntType"; break;
            case "string": typeName = "StringType"; break;
            case "code": typeName = "CodeType"; break;
            case "markdown": typeName = "MarkdownType"; break;
            case "time": typeName = "TimeType"; break;
            case "uri": typeName = "UriType"; break;
            case "uuid": typeName = "UriType"; break;
            case "id": typeName = "IdType"; break;
            case "oid": typeName = "OidType"; break;
            // enums
            case "AccountStatus": typeName = "Account$AccountStatus"; break;
            case "AddressUse": typeName = "Address$AddressUse"; break;
            case "AddressType": typeName = "Address$AddressType"; break;
            case "AllergyIntoleranceStatus": typeName = "AllergyIntolerance$AllergyIntoleranceStatus"; break;
            case "AllergyIntoleranceCriticality": typeName = "AllergyIntolerance$AllergyIntoleranceCriticality"; break;
            case "AllergyIntoleranceType": typeName = "AllergyIntolerance$AllergyIntoleranceType"; break;
            case "AllergyIntoleranceCategory": typeName = "AllergyIntolerance$AllergyIntoleranceCategory"; break;
            case "AllergyIntoleranceCertainty": typeName = "AllergyIntolerance$AllergyIntoleranceCertainty"; break;
            case "AllergyIntoleranceSeverity": typeName = "AllergyIntolerance$AllergyIntoleranceSeverity"; break;
            case "AppointmentStatus": typeName = "Appointment$AppointmentStatus"; break;
            case "ParticipantRequired": typeName = "Appointment$ParticipantRequired"; break;
            case "ParticipationStatus": typeName = "Appointment$ParticipationStatus"; break;
            case "ParticipantStatus": typeName = "AppointmentResponse$ParticipantStatus"; break;
            case "AuditEventAction": typeName = "AuditEvent$AuditEventAction"; break;
            case "AuditEventOutcome": typeName = "AuditEvent$AuditEventOutcome"; break;
            case "AuditEventParticipantNetworkType": typeName = "AuditEvent$AuditEventParticipantNetworkType"; break;
            case "BundleType": typeName = "Bundle$BundleType"; break;
            case "HTTPVerb": typeName = "Bundle$HTTPVerb"; break;
            case "SearchEntryMode": typeName = "Bundle$SearchEntryMode"; break;
            case "CarePlanStatus": typeName = "CarePlan$CarePlanStatus"; break;
            case "CarePlanRelationship": typeName = "CarePlan$CarePlanRelationship"; break;
            case "CarePlanActivityStatus": typeName = "CarePlan$CarePlanActivityStatus"; break;
            case "ClaimType": typeName = "Claim$ClaimType"; break;
            case "Use": typeName = "Claim$Use"; break;
            case "ClinicalImpressionStatus": typeName = "ClinicalImpression$ClinicalImpressionStatus"; break;
            case "CommunicationStatus": typeName = "Communication$CommunicationStatus"; break;
            case "CommunicationRequestStatus": typeName = "CommunicationRequest$CommunicationRequestStatus"; break;
            case "CompositionStatus": typeName = "Composition$CompositionStatus"; break;
            case "CompositionAttestationMode": typeName = "Composition$CompositionAttestationMode"; break;
            case "ConditionVerificationStatus": typeName = "Condition$ConditionVerificationStatus"; break;
            case "ConformanceStatementKind": typeName = "Conformance$ConformanceStatementKind"; break;
            case "UnknownContentCode": typeName = "Conformance$UnknownContentCode"; break;
            case "RestfulConformanceMode": typeName = "Conformance$RestfulConformanceMode"; break;
            case "TypeRestfulInteraction": typeName = "Conformance$TypeRestfulInteraction"; break;
            case "ResourceVersionPolicy": typeName = "Conformance$ResourceVersionPolicy"; break;
            case "ConditionalDeleteStatus": typeName = "Conformance$ConditionalDeleteStatus"; break;
            case "SearchModifierCode": typeName = "Conformance$SearchModifierCode"; break;
            case "SystemRestfulInteraction": typeName = "Conformance$SystemRestfulInteraction"; break;
            case "TransactionMode": typeName = "Conformance$TransactionMode"; break;
            case "MessageSignificanceCategory": typeName = "Conformance$MessageSignificanceCategory"; break;
            case "ConformanceEventMode": typeName = "Conformance$ConformanceEventMode"; break;
            case "DocumentMode": typeName = "Conformance$DocumentMode"; break;
            case "ContactPointSystem": typeName = "ContactPoint$ContactPointSystem"; break;
            case "ContactPointUse": typeName = "ContactPoint$ContactPointUse"; break;
            case "DataElementStringency": typeName = "DataElement$DataElementStringency"; break;
            case "DetectedIssueSeverity": typeName = "DetectedIssue$DetectedIssueSeverity"; break;
            case "DeviceStatus": typeName = "Device$DeviceStatus"; break;
            case "MeasmntPrinciple": typeName = "DeviceComponent$MeasmntPrinciple"; break;
            case "DeviceMetricOperationalStatus": typeName = "DeviceMetric$DeviceMetricOperationalStatus"; break;
            case "DeviceMetricColor": typeName = "DeviceMetric$DeviceMetricColor"; break;
            case "DeviceMetricCategory": typeName = "DeviceMetric$DeviceMetricCategory"; break;
            case "DeviceMetricCalibrationType": typeName = "DeviceMetric$DeviceMetricCalibrationType"; break;
            case "DeviceMetricCalibrationState": typeName = "DeviceMetric$DeviceMetricCalibrationState"; break;
            case "DeviceUseRequestStatus": typeName = "DeviceUseRequest$DeviceUseRequestStatus"; break;
            case "DeviceUseRequestPriority": typeName = "DeviceUseRequest$DeviceUseRequestPriority"; break;
            case "DiagnosticOrderStatus": typeName = "DiagnosticOrder$DiagnosticOrderStatus"; break;
            case "DiagnosticOrderPriority": typeName = "DiagnosticOrder$DiagnosticOrderPriority"; break;
            case "DiagnosticReportStatus": typeName = "DiagnosticReport$DiagnosticReportStatus"; break;
            case "DocumentRelationshipType": typeName = "DocumentReference$DocumentRelationshipType"; break;
            case "PropertyRepresentation": typeName = "ElementDefinition$PropertyRepresentation"; break;
            case "SlicingRules": typeName = "ElementDefinition$SlicingRules"; break;
            case "AggregationMode": typeName = "ElementDefinition$AggregationMode"; break;
            case "ConstraintSeverity": typeName = "ElementDefinition$ConstraintSeverity"; break;
            case "EncounterState": typeName = "Encounter$EncounterState"; break;
            case "EncounterClass": typeName = "Encounter$EncounterClass"; break;
            case "EncounterLocationStatus": typeName = "Encounter$EncounterLocationStatus"; break;
            case "AdministrativeGender": typeName = "Enumerations$AdministrativeGender"; break;
            case "AgeUnits": typeName = "Enumerations$AgeUnits"; break;
            case "BindingStrength": typeName = "Enumerations$BindingStrength"; break;
            case "ConceptMapEquivalence": typeName = "Enumerations$ConceptMapEquivalence"; break;
            case "ConformanceResourceStatus": typeName = "Enumerations$ConformanceResourceStatus"; break;
            case "DataAbsentReason": typeName = "Enumerations$DataAbsentReason"; break;
            case "DataType": typeName = "Enumerations$DataType"; break;
            case "DocumentReferenceStatus": typeName = "Enumerations$DocumentReferenceStatus"; break;
            case "FHIRDefinedType": typeName = "Enumerations$FHIRDefinedType"; break;
            case "MessageEvent": typeName = "Enumerations$MessageEvent"; break;
            case "NoteType": typeName = "Enumerations$NoteType"; break;
            case "RemittanceOutcome": typeName = "Enumerations$RemittanceOutcome"; break;
            case "ResourceType": typeName = "Enumerations$ResourceType"; break;
            case "SearchParamType": typeName = "Enumerations$SearchParamType"; break;
            case "SpecialValues": typeName = "Enumerations$SpecialValues"; break;
            case "EpisodeOfCareStatus": typeName = "EpisodeOfCare$EpisodeOfCareStatus"; break;
            case "FamilyHistoryStatus": typeName = "FamilyMemberHistory$FamilyHistoryStatus"; break;
            case "FlagStatus": typeName = "Flag$FlagStatus"; break;
            case "GoalStatus": typeName = "Goal$GoalStatus"; break;
            case "GroupType": typeName = "Group$GroupType"; break;
            case "DaysOfWeek": typeName = "HealthcareService$DaysOfWeek"; break;
            case "NameUse": typeName = "HumanName$NameUse"; break;
            case "IdentifierUse": typeName = "Identifier$IdentifierUse"; break;
            case "InstanceAvailability": typeName = "ImagingStudy$InstanceAvailability"; break;
            case "GuideDependencyType": typeName = "ImplementationGuide$GuideDependencyType"; break;
            case "GuidePageKind": typeName = "ImplementationGuide$GuidePageKind"; break;
            case "GuideResourcePurpose": typeName = "ImplementationGuide$GuideResourcePurpose"; break;
            case "ListMode": typeName = "List_$ListMode"; break;
            case "ListStatus": typeName = "List_$ListStatus"; break;
            case "LocationMode": typeName = "Location$LocationMode"; break;
            case "LocationStatus": typeName = "Location$LocationStatus"; break;
            case "DigitalMediaType": typeName = "Media$DigitalMediaType"; break;
            case "MedicationAdministrationStatus": typeName = "MedicationAdministration$MedicationAdministrationStatus"; break;
            case "MedicationDispenseStatus": typeName = "MedicationDispense$MedicationDispenseStatus"; break;
            case "MedicationOrderStatus": typeName = "MedicationOrder$MedicationOrderStatus"; break;
            case "MedicationStatementStatus": typeName = "MedicationStatement$MedicationStatementStatus"; break;
            case "ResponseType": typeName = "MessageHeader$ResponseType"; break;
            case "NamingSystemIdentifierType": typeName = "NamingSystem$NamingSystemIdentifierType"; break;
            case "NamingSystemType": typeName = "NamingSystem$NamingSystemType"; break;
            case "NarrativeStatus": typeName = "Narrative$NarrativeStatus"; break;
            case "NutritionOrderStatus": typeName = "NutritionOrder$NutritionOrderStatus"; break;
            case "ObservationRelationshipType": typeName = "Observation$ObservationRelationshipType"; break;
            case "ObservationStatus": typeName = "Observation$ObservationStatus"; break;
            case "OperationKind": typeName = "OperationDefinition$OperationKind"; break;
            case "OperationParameterUse": typeName = "OperationDefinition$OperationParameterUse"; break;
            case "IssueSeverity": typeName = "OperationOutcome$IssueSeverity"; break;
            case "IssueType": typeName = "OperationOutcome$IssueType"; break;
            case "OrderStatus": typeName = "OrderResponse$OrderStatus"; break;
            case "LinkType": typeName = "Patient$LinkType"; break;
            case "IdentityAssuranceLevel": typeName = "Person$IdentityAssuranceLevel"; break;
            case "ProcedureStatus": typeName = "Procedure$ProcedureStatus"; break;
            case "ProcedureRequestStatus": typeName = "ProcedureRequest$ProcedureRequestStatus"; break;
            case "ProcedureRequestPriority": typeName = "ProcedureRequest$ProcedureRequestPriority"; break;
            case "ActionList": typeName = "ProcessRequest$ActionList"; break;
            case "ProvenanceEntityRole": typeName = "Provenance$ProvenanceEntityRole"; break;
            case "QuantityComparator": typeName = "Quantity$QuantityComparator"; break;
            case "QuestionnaireStatus": typeName = "Questionnaire$QuestionnaireStatus"; break;
            case "AnswerFormat": typeName = "Questionnaire$AnswerFormat"; break;
            case "QuestionnaireResponseStatus": typeName = "QuestionnaireResponse$QuestionnaireResponseStatus"; break;
            case "ReferralStatus": typeName = "ReferralRequest$ReferralStatus"; break;
            case "XPathUsageType": typeName = "SearchParameter$XPathUsageType"; break;
            case "SlotStatus": typeName = "Slot$SlotStatus"; break;
            case "SpecimenStatus": typeName = "Specimen$SpecimenStatus"; break;
            case "StructureDefinitionKind": typeName = "StructureDefinition$StructureDefinitionKind"; break;
            case "ExtensionContext": typeName = "StructureDefinition$ExtensionContext"; break;
            case "SubscriptionStatus": typeName = "Subscription$SubscriptionStatus"; break;
            case "SubscriptionChannelType": typeName = "Subscription$SubscriptionChannelType"; break;
            case "SupplyDeliveryStatus": typeName = "SupplyDelivery$SupplyDeliveryStatus"; break;
            case "SupplyRequestStatus": typeName = "SupplyRequest$SupplyRequestStatus"; break;
            case "ContentType": typeName = "TestScript$ContentType"; break;
            case "AssertionDirectionType": typeName = "TestScript$AssertionDirectionType"; break;
            case "AssertionOperatorType": typeName = "TestScript$AssertionOperatorType"; break;
            case "AssertionResponseTypes": typeName = "TestScript$AssertionResponseTypes"; break;
            case "UnitsOfTime": typeName = "Timing$UnitsOfTime"; break;
            case "EventTiming": typeName = "Timing$EventTiming"; break;
            case "FilterOperator": typeName = "ValueSet$FilterOperator"; break;
            case "VisionEyes": typeName = "VisionPrescription$VisionEyes"; break;
            case "VisionBase": typeName = "VisionPrescription$VisionBase"; break;
        }

        return typeName;
	}
}