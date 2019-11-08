package org.opencds.cqf.cql.model;

import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.r4.model.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public class R4FhirModelResolver extends FhirModelResolver<Base> {

	public R4FhirModelResolver() {
        this(FhirContext.forR4());
	}

	public R4FhirModelResolver(FhirContext fhirContext) {
        super(fhirContext, (x, y) -> x.equalsDeep(y));
        this.setPackageName("org.hl7.fhir.r4.model");

        if (fhirContext.getVersion().getVersion() != FhirVersionEnum.R4) {
            throw new IllegalArgumentException("The supplied context is not configured for DSTU3");
        }
	}

	@Override
    public Object createInstance(String typeName) {
        String className = resolveClassName(typeName);
        if (className.indexOf('$') >= 0) {
            className += "EnumFactory";
            return new org.hl7.fhir.r4.model.Enumeration((EnumFactory)createInstance(resolveClass(className)));
        }

        return createInstance(resolveClass(className));
    }

    @Override
    public Class resolveType(Object value) {
        if (value instanceof org.hl7.fhir.r4.model.Enumeration) {
            String className = ((org.hl7.fhir.r4.model.Enumeration)value).getEnumFactory().getClass().getName();
            try {
                className = className.substring(0, className.indexOf("EnumFactory"));
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Could not resolve type %s", className));
            }
        }

        return super.resolveType(value);
    }


    @Override
    public void setValue(Object target, String path, Object value) {
        if (target instanceof Enumeration && path.equals("value")) {
            ((Enumeration)target).setValueAsString((String)value);
            return;
        }

        super.setValue(target, path, value);
    }

    @Override
    protected Object resolveProperty(Object target, String path) {
        if (target instanceof Enumeration && path.equals("value")) {
            return ((Enumeration)target).getValueAsString();
        }

        // This is kind of a hack to get around contained resources - HAPI doesn't have ResourceContainer type for STU3
        if (target instanceof Resource && ((Resource) target).fhirType().equals(path))
        {
            return target;
        }

        return super.resolveProperty(target, path);
	}

	@Override
	public String resolveClassName(String typeName) {
        // Common bindings such as RequestIntent are not marked as such in the structure definitions for R4
        // As a result, even though the XML schema creates these as common types (and so they show up as common
        // types in the model info for R4), the generated classes for clients don't build them this way
        // Reflection is really the only way to resolve this at this point....

        // TODO: Use the bindingName extension on the StructureDefinition for this
        switch (typeName) {
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
            case "uuid": typeName = "UuidType"; break;
            case "id": typeName = "IdType"; break;
            case "oid": typeName = "OidType"; break;
            case "AccountStatus": typeName = "Account$AccountStatus"; break;
            case "ActivityDefinitionKind": typeName = "ActivityDefinition$ActivityDefinitionKind"; break;
            case "RequestIntent": typeName = "ActivityDefinition$RequestIntent"; break;
            case "RequestPriority": typeName = "ActivityDefinition$RequestPriority"; break;
            case "ActivityParticipantType": typeName = "ActivityDefinition$ActivityParticipantType"; break;
            case "AdverseEventActuality": typeName = "AdverseEvent$AdverseEventActuality"; break;
            case "AllergyIntoleranceType": typeName = "AllergyIntolerance$AllergyIntoleranceType"; break;
            case "AllergyIntoleranceCategory": typeName = "AllergyIntolerance$AllergyIntoleranceCategory"; break;
            case "AllergyIntoleranceCriticality": typeName = "AllergyIntolerance$AllergyIntoleranceCriticality"; break;
            case "AllergyIntoleranceSeverity": typeName = "AllergyIntolerance$AllergyIntoleranceSeverity"; break;
            case "AppointmentStatus": typeName = "Appointment$AppointmentStatus"; break;
            case "ParticipantRequired": typeName = "Appointment$ParticipantRequired"; break;
            case "ParticipationStatus": typeName = "Appointment$ParticipationStatus"; break;
            case "ParticipantStatus": typeName = "AppointmentResponse$ParticipantStatus"; break;
            case "AuditEventAction": typeName = "AuditEvent$AuditEventAction"; break;
            case "AuditEventOutcome": typeName = "AuditEvent$AuditEventOutcome"; break;
            case "AuditEventAgentNetworkType": typeName = "AuditEvent$AuditEventAgentNetworkType"; break;
            case "BiologicallyDerivedProductCategory": typeName = "BiologicallyDerivedProduct$BiologicallyDerivedProductCategory"; break;
            case "BiologicallyDerivedProductStatus": typeName = "BiologicallyDerivedProduct$BiologicallyDerivedProductStatus"; break;
            case "BiologicallyDerivedProductStorageScale": typeName = "BiologicallyDerivedProduct$BiologicallyDerivedProductStorageScale"; break;
            case "BundleType": typeName = "Bundle$BundleType"; break;
            case "SearchEntryMode": typeName = "Bundle$SearchEntryMode"; break;
            case "HTTPVerb": typeName = "Bundle$HTTPVerb"; break;
            case "CapabilityStatementKind": typeName = "CapabilityStatement$CapabilityStatementKind"; break;
            case "RestfulCapabilityMode": typeName = "CapabilityStatement$RestfulCapabilityMode"; break;
            case "TypeRestfulInteraction": typeName = "CapabilityStatement$TypeRestfulInteraction"; break;
            case "ResourceVersionPolicy": typeName = "CapabilityStatement$ResourceVersionPolicy"; break;
            case "ConditionalReadStatus": typeName = "CapabilityStatement$ConditionalReadStatus"; break;
            case "ConditionalDeleteStatus": typeName = "CapabilityStatement$ConditionalDeleteStatus"; break;
            case "ReferenceHandlingPolicy": typeName = "CapabilityStatement$ReferenceHandlingPolicy"; break;
            case "SystemRestfulInteraction": typeName = "CapabilityStatement$SystemRestfulInteraction"; break;
            case "EventCapabilityMode": typeName = "CapabilityStatement$EventCapabilityMode"; break;
            case "DocumentMode": typeName = "CapabilityStatement$DocumentMode"; break;
            case "CarePlanStatus": typeName = "CarePlan$CarePlanStatus"; break;
            case "CarePlanIntent": typeName = "CarePlan$CarePlanIntent"; break;
            case "CarePlanActivityKind": typeName = "CarePlan$CarePlanActivityKind"; break;
            case "CarePlanActivityStatus": typeName = "CarePlan$CarePlanActivityStatus"; break;
            case "CareTeamStatus": typeName = "CareTeam$CareTeamStatus"; break;
            case "CatalogEntryRelationType": typeName = "CatalogEntry$CatalogEntryRelationType"; break;
            case "ChargeItemStatus": typeName = "ChargeItem$ChargeItemStatus"; break;
            case "ChargeItemDefinitionPriceComponentType": typeName = "ChargeItemDefinition$ChargeItemDefinitionPriceComponentType"; break;
            case "ClaimStatus": typeName = "Claim$ClaimStatus"; break;
            case "Use": typeName = "Claim$Use"; break;
            case "ClaimResponseStatus": typeName = "ClaimResponse$ClaimResponseStatus"; break;
            //case "Use": typeName = "ClaimResponse$Use"; break;
            case "RemittanceOutcome": typeName = "ClaimResponse$RemittanceOutcome"; break;
            case "ClinicalImpressionStatus": typeName = "ClinicalImpression$ClinicalImpressionStatus"; break;
            case "CodeSystemHierarchyMeaning": typeName = "CodeSystem$CodeSystemHierarchyMeaning"; break;
            case "CodeSystemContentMode": typeName = "CodeSystem$CodeSystemContentMode"; break;
            case "FilterOperator": typeName = "CodeSystem$FilterOperator"; break;
            case "PropertyType": typeName = "CodeSystem$PropertyType"; break;
            case "CommunicationStatus": typeName = "Communication$CommunicationStatus"; break;
            case "CommunicationPriority": typeName = "Communication$CommunicationPriority"; break;
            case "CommunicationRequestStatus": typeName = "CommunicationRequest$CommunicationRequestStatus"; break;
            //case "CommunicationPriority": typeName = "CommunicationRequest$CommunicationPriority"; break;
            case "CompartmentType": typeName = "CompartmentDefinition$CompartmentType"; break;
            case "CompositionStatus": typeName = "Composition$CompositionStatus"; break;
            case "DocumentConfidentiality": typeName = "Composition$DocumentConfidentiality"; break;
            case "CompositionAttestationMode": typeName = "Composition$CompositionAttestationMode"; break;
            case "DocumentRelationshipType": typeName = "Composition$DocumentRelationshipType"; break;
            case "SectionMode": typeName = "Composition$SectionMode"; break;
            case "ConceptMapGroupUnmappedMode": typeName = "ConceptMap$ConceptMapGroupUnmappedMode"; break;
            case "ConsentState": typeName = "Consent$ConsentState"; break;
            case "ConsentProvisionType": typeName = "Consent$ConsentProvisionType"; break;
            case "ConsentDataMeaning": typeName = "Consent$ConsentDataMeaning"; break;
            case "ContractStatus": typeName = "Contract$ContractStatus"; break;
            case "ContractPublicationStatus": typeName = "Contract$ContractPublicationStatus"; break;
            case "CoverageStatus": typeName = "Coverage$CoverageStatus"; break;
            case "EligibilityRequestStatus": typeName = "CoverageEligibilityRequest$EligibilityRequestStatus"; break;
            case "EligibilityRequestPurpose": typeName = "CoverageEligibilityRequest$EligibilityRequestPurpose"; break;
            case "EligibilityResponseStatus": typeName = "CoverageEligibilityResponse$EligibilityResponseStatus"; break;
            case "EligibilityResponsePurpose": typeName = "CoverageEligibilityResponse$EligibilityResponsePurpose"; break;
            case "DetectedIssueStatus": typeName = "DetectedIssue$DetectedIssueStatus"; break;
            case "DetectedIssueSeverity": typeName = "DetectedIssue$DetectedIssueSeverity"; break;
            case "UDIEntryType": typeName = "Device$UDIEntryType"; break;
            case "FHIRDeviceStatus": typeName = "Device$FHIRDeviceStatus"; break;
            case "DeviceNameType": typeName = "Device$DeviceNameType"; break;
            //case "DeviceNameType": typeName = "DeviceDefinition$DeviceNameType"; break;
            case "DeviceMetricOperationalStatus": typeName = "DeviceMetric$DeviceMetricOperationalStatus"; break;
            case "DeviceMetricColor": typeName = "DeviceMetric$DeviceMetricColor"; break;
            case "DeviceMetricCategory": typeName = "DeviceMetric$DeviceMetricCategory"; break;
            case "DeviceMetricCalibrationType": typeName = "DeviceMetric$DeviceMetricCalibrationType"; break;
            case "DeviceMetricCalibrationState": typeName = "DeviceMetric$DeviceMetricCalibrationState"; break;
            case "DeviceRequestStatus": typeName = "DeviceRequest$DeviceRequestStatus"; break;
            //case "RequestIntent": typeName = "DeviceRequest$RequestIntent"; break;
            //case "RequestPriority": typeName = "DeviceRequest$RequestPriority"; break;
            case "DeviceUseStatementStatus": typeName = "DeviceUseStatement$DeviceUseStatementStatus"; break;
            case "DiagnosticReportStatus": typeName = "DiagnosticReport$DiagnosticReportStatus"; break;
            case "ReferredDocumentStatus": typeName = "DocumentReference$ReferredDocumentStatus"; break;
            //case "DocumentRelationshipType": typeName = "DocumentReference$DocumentRelationshipType"; break;
            case "ExposureState": typeName = "EffectEvidenceSynthesis$ExposureState"; break;
            case "EncounterStatus": typeName = "Encounter$EncounterStatus"; break;
            //case "EncounterStatus": typeName = "Encounter$EncounterStatus"; break;
            case "EncounterLocationStatus": typeName = "Encounter$EncounterLocationStatus"; break;
            case "EndpointStatus": typeName = "Endpoint$EndpointStatus"; break;
            case "EnrollmentRequestStatus": typeName = "EnrollmentRequest$EnrollmentRequestStatus"; break;
            case "EnrollmentResponseStatus": typeName = "EnrollmentResponse$EnrollmentResponseStatus"; break;
            case "EpisodeOfCareStatus": typeName = "EpisodeOfCare$EpisodeOfCareStatus"; break;
            //case "EpisodeOfCareStatus": typeName = "EpisodeOfCare$EpisodeOfCareStatus"; break;
            case "EvidenceVariableType": typeName = "EvidenceVariable$EvidenceVariableType"; break;
            case "GroupMeasure": typeName = "EvidenceVariable$GroupMeasure"; break;
            case "ExampleScenarioActorType": typeName = "ExampleScenario$ExampleScenarioActorType"; break;
            case "FHIRResourceType": typeName = "ExampleScenario$FHIRResourceType"; break;
            case "ExplanationOfBenefitStatus": typeName = "ExplanationOfBenefit$ExplanationOfBenefitStatus"; break;
            //case "Use": typeName = "ExplanationOfBenefit$Use"; break;
            //case "RemittanceOutcome": typeName = "ExplanationOfBenefit$RemittanceOutcome"; break;
            case "FamilyHistoryStatus": typeName = "FamilyMemberHistory$FamilyHistoryStatus"; break;
            case "FlagStatus": typeName = "Flag$FlagStatus"; break;
            case "GoalLifecycleStatus": typeName = "Goal$GoalLifecycleStatus"; break;
            case "GraphCompartmentUse": typeName = "GraphDefinition$GraphCompartmentUse"; break;
            case "CompartmentCode": typeName = "GraphDefinition$CompartmentCode"; break;
            case "GraphCompartmentRule": typeName = "GraphDefinition$GraphCompartmentRule"; break;
            case "GroupType": typeName = "Group$GroupType"; break;
            case "GuidanceResponseStatus": typeName = "GuidanceResponse$GuidanceResponseStatus"; break;
            case "DaysOfWeek": typeName = "HealthcareService$DaysOfWeek"; break;
            case "ImagingStudyStatus": typeName = "ImagingStudy$ImagingStudyStatus"; break;
            case "ImmunizationStatus": typeName = "Immunization$ImmunizationStatus"; break;
            case "ImmunizationEvaluationStatus": typeName = "ImmunizationEvaluation$ImmunizationEvaluationStatus"; break;
            case "SPDXLicense": typeName = "ImplementationGuide$SPDXLicense"; break;
            case "GuidePageGeneration": typeName = "ImplementationGuide$GuidePageGeneration"; break;
            case "GuideParameterCode": typeName = "ImplementationGuide$GuideParameterCode"; break;
            case "InvoiceStatus": typeName = "Invoice$InvoiceStatus"; break;
            case "InvoicePriceComponentType": typeName = "Invoice$InvoicePriceComponentType"; break;
            case "LinkageType": typeName = "Linkage$LinkageType"; break;
            case "ListStatus": typeName = "List$ListStatus"; break;
            case "ListMode": typeName = "List$ListMode"; break;
            case "LocationStatus": typeName = "Location$LocationStatus"; break;
            case "LocationMode": typeName = "Location$LocationMode"; break;
            //case "DaysOfWeek": typeName = "Location$DaysOfWeek"; break;
            case "MeasureReportStatus": typeName = "MeasureReport$MeasureReportStatus"; break;
            case "MeasureReportType": typeName = "MeasureReport$MeasureReportType"; break;
            case "MediaStatus": typeName = "Media$MediaStatus"; break;
            case "MedicationStatus": typeName = "Medication$MedicationStatus"; break;
            case "MedicationAdministrationStatus": typeName = "MedicationAdministration$MedicationAdministrationStatus"; break;
            case "MedicationDispenseStatus": typeName = "MedicationDispense$MedicationDispenseStatus"; break;
            case "MedicationKnowledgeStatus": typeName = "MedicationKnowledge$MedicationKnowledgeStatus"; break;
            case "MedicationRequestStatus": typeName = "MedicationRequest$MedicationRequestStatus"; break;
            case "MedicationRequestIntent": typeName = "MedicationRequest$MedicationRequestIntent"; break;
            case "MedicationRequestPriority": typeName = "MedicationRequest$MedicationRequestPriority"; break;
            case "MedicationStatementStatus": typeName = "MedicationStatement$MedicationStatementStatus"; break;
            case "MessageSignificanceCategory": typeName = "MessageDefinition$MessageSignificanceCategory"; break;
            case "MessageheaderResponseRequest": typeName = "MessageDefinition$MessageheaderResponseRequest"; break;
            case "ResponseType": typeName = "MessageHeader$ResponseType"; break;
            case "SequenceType": typeName = "MolecularSequence$SequenceType"; break;
            case "OrientationType": typeName = "MolecularSequence$OrientationType"; break;
            case "StrandType": typeName = "MolecularSequence$StrandType"; break;
            case "QualityType": typeName = "MolecularSequence$QualityType"; break;
            case "RepositoryType": typeName = "MolecularSequence$RepositoryType"; break;
            case "NamingSystemType": typeName = "NamingSystem$NamingSystemType"; break;
            case "NamingSystemIdentifierType": typeName = "NamingSystem$NamingSystemIdentifierType"; break;
            case "NutritionOrderStatus": typeName = "NutritionOrder$NutritionOrderStatus"; break;
            case "NutritiionOrderIntent": typeName = "NutritionOrder$NutritiionOrderIntent"; break;
            case "ObservationStatus": typeName = "Observation$ObservationStatus"; break;
            case "ObservationDataType": typeName = "ObservationDefinition$ObservationDataType"; break;
            case "ObservationRangeCategory": typeName = "ObservationDefinition$ObservationRangeCategory"; break;
            case "OperationKind": typeName = "OperationDefinition$OperationKind"; break;
            case "OperationParameterUse": typeName = "OperationDefinition$OperationParameterUse"; break;
            case "IssueSeverity": typeName = "OperationOutcome$IssueSeverity"; break;
            case "IssueType": typeName = "OperationOutcome$IssueType"; break;
            case "LinkType": typeName = "Patient$LinkType"; break;
            case "PaymentNoticeStatus": typeName = "PaymentNotice$PaymentNoticeStatus"; break;
            case "PaymentReconciliationStatus": typeName = "PaymentReconciliation$PaymentReconciliationStatus"; break;
            case "IdentityAssuranceLevel": typeName = "Person$IdentityAssuranceLevel"; break;
            //case "RequestPriority": typeName = "PlanDefinition$RequestPriority"; break;
            case "ActionConditionKind": typeName = "PlanDefinition$ActionConditionKind"; break;
            case "ActionRelationshipType": typeName = "PlanDefinition$ActionRelationshipType"; break;
            case "ActionParticipantType": typeName = "PlanDefinition$ActionParticipantType"; break;
            case "ActionGroupingBehavior": typeName = "PlanDefinition$ActionGroupingBehavior"; break;
            case "ActionSelectionBehavior": typeName = "PlanDefinition$ActionSelectionBehavior"; break;
            case "ActionRequiredBehavior": typeName = "PlanDefinition$ActionRequiredBehavior"; break;
            case "ActionPrecheckBehavior": typeName = "PlanDefinition$ActionPrecheckBehavior"; break;
            case "ActionCardinalityBehavior": typeName = "PlanDefinition$ActionCardinalityBehavior"; break;
            //case "DaysOfWeek": typeName = "PractitionerRole$DaysOfWeek"; break;
            case "ProcedureStatus": typeName = "Procedure$ProcedureStatus"; break;
            case "ProvenanceEntityRole": typeName = "Provenance$ProvenanceEntityRole"; break;
            case "QuestionnaireItemType": typeName = "Questionnaire$QuestionnaireItemType"; break;
            case "QuestionnaireItemOperator": typeName = "Questionnaire$QuestionnaireItemOperator"; break;
            case "EnableWhenBehavior": typeName = "Questionnaire$EnableWhenBehavior"; break;
            case "QuestionnaireResponseStatus": typeName = "QuestionnaireResponse$QuestionnaireResponseStatus"; break;
            case "RequestStatus": typeName = "RequestGroup$RequestStatus"; break;
            //case "RequestIntent": typeName = "RequestGroup$RequestIntent"; break;
           // case "RequestPriority": typeName = "RequestGroup$RequestPriority"; break;
            //case "RequestPriority": typeName = "RequestGroup$RequestPriority"; break;
            //case "ActionConditionKind": typeName = "RequestGroup$ActionConditionKind"; break;
            //case "ActionRelationshipType": typeName = "RequestGroup$ActionRelationshipType"; break;
            //case "ActionGroupingBehavior": typeName = "RequestGroup$ActionGroupingBehavior"; break;
            //case "ActionSelectionBehavior": typeName = "RequestGroup$ActionSelectionBehavior"; break;
            //case "ActionRequiredBehavior": typeName = "RequestGroup$ActionRequiredBehavior"; break;
            //case "ActionPrecheckBehavior": typeName = "RequestGroup$ActionPrecheckBehavior"; break;
            //case "ActionCardinalityBehavior": typeName = "RequestGroup$ActionCardinalityBehavior"; break;
            case "ResearchElementType": typeName = "ResearchElementDefinition$ResearchElementType"; break;
            case "VariableType": typeName = "ResearchElementDefinition$VariableType"; break;
            //case "GroupMeasure": typeName = "ResearchElementDefinition$GroupMeasure"; break;
            //case "GroupMeasure": typeName = "ResearchElementDefinition$GroupMeasure"; break;
            case "ResearchStudyStatus": typeName = "ResearchStudy$ResearchStudyStatus"; break;
            case "ResearchSubjectStatus": typeName = "ResearchSubject$ResearchSubjectStatus"; break;
            case "RiskAssessmentStatus": typeName = "RiskAssessment$RiskAssessmentStatus"; break;
            case "XPathUsageType": typeName = "SearchParameter$XPathUsageType"; break;
            case "SearchComparator": typeName = "SearchParameter$SearchComparator"; break;
            case "SearchModifierCode": typeName = "SearchParameter$SearchModifierCode"; break;
            case "ServiceRequestStatus": typeName = "ServiceRequest$ServiceRequestStatus"; break;
            case "ServiceRequestIntent": typeName = "ServiceRequest$ServiceRequestIntent"; break;
            case "ServiceRequestPriority": typeName = "ServiceRequest$ServiceRequestPriority"; break;
            case "SlotStatus": typeName = "Slot$SlotStatus"; break;
            case "SpecimenStatus": typeName = "Specimen$SpecimenStatus"; break;
            case "SpecimenContainedPreference": typeName = "SpecimenDefinition$SpecimenContainedPreference"; break;
            case "StructureDefinitionKind": typeName = "StructureDefinition$StructureDefinitionKind"; break;
            case "ExtensionContextType": typeName = "StructureDefinition$ExtensionContextType"; break;
            case "TypeDerivationRule": typeName = "StructureDefinition$TypeDerivationRule"; break;
            case "StructureMapModelMode": typeName = "StructureMap$StructureMapModelMode"; break;
            case "StructureMapGroupTypeMode": typeName = "StructureMap$StructureMapGroupTypeMode"; break;
            case "StructureMapInputMode": typeName = "StructureMap$StructureMapInputMode"; break;
            case "StructureMapSourceListMode": typeName = "StructureMap$StructureMapSourceListMode"; break;
            case "StructureMapContextType": typeName = "StructureMap$StructureMapContextType"; break;
            case "StructureMapTargetListMode": typeName = "StructureMap$StructureMapTargetListMode"; break;
            case "StructureMapTransform": typeName = "StructureMap$StructureMapTransform"; break;
            case "SubscriptionStatus": typeName = "Subscription$SubscriptionStatus"; break;
            case "SubscriptionChannelType": typeName = "Subscription$SubscriptionChannelType"; break;
            case "FHIRSubstanceStatus": typeName = "Substance$FHIRSubstanceStatus"; break;
            case "SupplyDeliveryStatus": typeName = "SupplyDelivery$SupplyDeliveryStatus"; break;
            case "SupplyRequestStatus": typeName = "SupplyRequest$SupplyRequestStatus"; break;
            //case "RequestPriority": typeName = "SupplyRequest$RequestPriority"; break;
            case "TaskStatus": typeName = "Task$TaskStatus"; break;
            case "TaskIntent": typeName = "Task$TaskIntent"; break;
            case "TaskPriority": typeName = "Task$TaskPriority"; break;
            //case "CapabilityStatementKind": typeName = "TerminologyCapabilities$CapabilityStatementKind"; break;
            case "CodeSearchSupport": typeName = "TerminologyCapabilities$CodeSearchSupport"; break;
            case "TestReportStatus": typeName = "TestReport$TestReportStatus"; break;
            case "TestReportResult": typeName = "TestReport$TestReportResult"; break;
            case "TestReportParticipantType": typeName = "TestReport$TestReportParticipantType"; break;
            case "TestReportActionResult": typeName = "TestReport$TestReportActionResult"; break;
            //case "TestReportActionResult": typeName = "TestReport$TestReportActionResult"; break;
            case "TestScriptRequestMethodCode": typeName = "TestScript$TestScriptRequestMethodCode"; break;
            case "AssertionDirectionType": typeName = "TestScript$AssertionDirectionType"; break;
            case "AssertionOperatorType": typeName = "TestScript$AssertionOperatorType"; break;
           // case "TestScriptRequestMethodCode": typeName = "TestScript$TestScriptRequestMethodCode"; break;
            case "AssertionResponseTypes": typeName = "TestScript$AssertionResponseTypes"; break;
            //case "FilterOperator": typeName = "ValueSet$FilterOperator"; break;
            case "Status": typeName = "VerificationResult$Status"; break;
            case "VisionStatus": typeName = "VisionPrescription$VisionStatus"; break;
            case "VisionEyes": typeName = "VisionPrescription$VisionEyes"; break;
            case "VisionBase": typeName = "VisionPrescription$VisionBase"; break;
            case "FHIRDefinedType": typeName = "Enumerations$FHIRDefinedType"; break;
            case "FHIRAllTypes": typeName = "Enumerations$FHIRAllTypes"; break;
        }

        return typeName;
	}

	@Override
    public String getContextPath(String contextType, String dataType) {
        switch (contextType) {
            case "Device":
                switch (dataType) {
                    case "Account":
                        return "subject";
                    case "ActivityDefinition":
                        break;
                    case "AdverseEvent":
                        break;
                    case "AllergyIntolerance":
                        break;
                    case "Appointment":
                        return "actor";
                    case "AppointmentResponse":
                        return "actor";
                    case "AuditEvent":
                        return "agent";
                    case "Basic":
                        break;
                    case "Binary":
                        break;
                    case "BiologicallyDerivedProduct":
                        break;
                    case "BodyStructure":
                        break;
                    case "Bundle":
                        break;
                    case "CapabilityStatement":
                        break;
                    case "CarePlan":
                        break;
                    case "CareTeam":
                        break;
                    case "CatalogEntry":
                        break;
                    case "ChargeItem":
                        break;
                    case "ChargeItemDefinition":
                        break;
                    case "Claim":
                        break;
                    case "ClaimResponse":
                        break;
                    case "ClinicalImpression":
                        break;
                    case "CodeSystem":
                        break;
                    case "Communication":
                        break;
                    case "CommunicationRequest":
                        break;
                    case "CompartmentDefinition":
                        break;
                    case "Composition":
                        return "author";
                    case "ConceptMap":
                        break;
                    case "Condition":
                        break;
                    case "Consent":
                        break;
                    case "Contract":
                        break;
                    case "Coverage":
                        break;
                    case "CoverageEligibilityRequest":
                        break;
                    case "CoverageEligibilityResponse":
                        break;
                    case "DetectedIssue":
                        return "author";
                    case "Device":
                        break;
                    case "DeviceDefinition":
                        break;
                    case "DeviceMetric":
                        break;
                    case "DeviceRequest":
                        break;
                    case "DeviceUseStatement":
                        return "device";
                    case "DiagnosticReport":
                        return "subject";
                    case "DocumentManifest":
                        break;
                    case "DocumentReference":
                        break;
                    case "EffectEvidenceSynthesis":
                        break;
                    case "Encounter":
                        break;
                    case "Endpoint":
                        break;
                    case "EnrollmentRequest":
                        break;
                    case "EnrollmentResponse":
                        break;
                    case "EpisodeOfCare":
                        break;
                    case "EventDefinition":
                        break;
                    case "Evidence":
                        break;
                    case "EvidenceVariable":
                        break;
                    case "ExampleScenario":
                        break;
                    case "ExplanationOfBenefit":
                        break;
                    case "FamilyMemberHistory":
                        break;
                    case "Flag":
                        return "author";
                    case "Goal":
                        break;
                    case "GraphDefinition":
                        break;
                    case "Group":
                        return "member";
                    case "GuidanceResponse":
                        break;
                    case "HealthcareService":
                        break;
                    case "ImagingStudy":
                        break;
                    case "Immunization":
                        break;
                    case "ImmunizationEvaluation":
                        break;
                    case "ImmunizationRecommendation":
                        break;
                    case "ImplementationGuide":
                        break;
                    case "InsurancePlan":
                        break;
                    case "Invoice":
                        return "participant";
                    case "Library":
                        break;
                    case "Linkage":
                        break;
                    case "List":
                        break;
                    case "Location":
                        break;
                    case "Measure":
                        break;
                    case "MeasureReport":
                        break;
                    case "Media":
                        return "subject";
                    case "Medication":
                        break;
                    case "MedicationAdministration":
                        return "device";
                    case "MedicationDispense":
                        break;
                    case "MedicationKnowledge":
                        break;
                    case "MedicationRequest":
                        break;
                    case "MedicationStatement":
                        break;
                    case "MedicinalProduct":
                        break;
                    case "MedicinalProductAuthorization":
                        break;
                    case "MedicinalProductContraindication":
                        break;
                    case "MedicinalProductIndication":
                        break;
                    case "MedicinalProductIngredient":
                        break;
                    case "MedicinalProductInteraction":
                        break;
                    case "MedicinalProductManufactured":
                        break;
                    case "MedicinalProductPackaged":
                        break;
                    case "MedicinalProductPharmaceutical":
                        break;
                    case "MedicinalProductUndesirableEffect":
                        break;
                    case "MessageDefinition":
                        break;
                    case "MessageHeader":
                        return "target";
                    case "MolecularSequence":
                        break;
                    case "NamingSystem":
                        break;
                    case "NutritionOrder":
                        break;
                    case "Observation":
                        break;
                    case "ObservationDefinition":
                        break;
                    case "OperationDefinition":
                        break;
                    case "OperationOutcome":
                        break;
                    case "Organization":
                        break;
                    case "OrganizationAffiliation":
                        break;
                    case "Patient":
                        break;
                    case "PaymentNotice":
                        break;
                    case "PaymentReconciliation":
                        break;
                    case "Person":
                        break;
                    case "PlanDefinition":
                        break;
                    case "Practitioner":
                        break;
                    case "PractitionerRole":
                        break;
                    case "Procedure":
                        break;
                    case "Provenance":
                        return "agent";
                    case "Questionnaire":
                        break;
                    case "QuestionnaireResponse":
                        return "author";
                    case "RelatedPerson":
                        break;
                    case "RequestGroup":
                        return "author";
                    case "ResearchDefinition":
                        break;
                    case "ResearchElementDefinition":
                        break;
                    case "ResearchStudy":
                        break;
                    case "ResearchSubject":
                        break;
                    case "RiskAssessment":
                        return "performer";
                    case "RiskEvidenceSynthesis":
                        break;
                    case "Schedule":
                        return "actor";
                    case "SearchParameter":
                        break;
                    case "ServiceRequest":
                        break;
                    case "Slot":
                        break;
                    case "Specimen":
                        return "subject";
                    case "SpecimenDefinition":
                        break;
                    case "StructureDefinition":
                        break;
                    case "StructureMap":
                        break;
                    case "Subscription":
                        break;
                    case "Substance":
                        break;
                    case "SubstanceNucleicAcid":
                        break;
                    case "SubstancePolymer":
                        break;
                    case "SubstanceProtein":
                        break;
                    case "SubstanceReferenceInformation":
                        break;
                    case "SubstanceSourceMaterial":
                        break;
                    case "SubstanceSpecification":
                        break;
                    case "SupplyDelivery":
                        break;
                    case "SupplyRequest":
                        return "requester";
                    case "Task":
                        break;
                    case "TerminologyCapabilities":
                        break;
                    case "TestReport":
                        break;
                    case "TestScript":
                        break;
                    case "ValueSet":
                        break;
                    case "VerificationResult":
                        break;
                    case "VisionPrescription":
                        break;
                }
                break;
            case "Encounter":
                switch (dataType) {
                    case "Account":
                        break;
                    case "ActivityDefinition":
                        break;
                    case "AdverseEvent":
                        break;
                    case "AllergyIntolerance":
                        break;
                    case "Appointment":
                        break;
                    case "AppointmentResponse":
                        break;
                    case "AuditEvent":
                        break;
                    case "Basic":
                        break;
                    case "Binary":
                        break;
                    case "BiologicallyDerivedProduct":
                        break;
                    case "BodyStructure":
                        break;
                    case "Bundle":
                        break;
                    case "CapabilityStatement":
                        break;
                    case "CarePlan":
                        return "encounter";
                    case "CareTeam":
                        return "encounter";
                    case "CatalogEntry":
                        break;
                    case "ChargeItem":
                        return "context";
                    case "ChargeItemDefinition":
                        break;
                    case "Claim":
                        return "encounter";
                    case "ClaimResponse":
                        break;
                    case "ClinicalImpression":
                        return "encounter";
                    case "CodeSystem":
                        break;
                    case "Communication":
                        return "encounter";
                    case "CommunicationRequest":
                        return "encounter";
                    case "CompartmentDefinition":
                        break;
                    case "Composition":
                        return "encounter";
                    case "ConceptMap":
                        break;
                    case "Condition":
                        return "encounter";
                    case "Consent":
                        break;
                    case "Contract":
                        break;
                    case "Coverage":
                        break;
                    case "CoverageEligibilityRequest":
                        break;
                    case "CoverageEligibilityResponse":
                        break;
                    case "DetectedIssue":
                        break;
                    case "Device":
                        break;
                    case "DeviceDefinition":
                        break;
                    case "DeviceMetric":
                        break;
                    case "DeviceRequest":
                        return "encounter";
                    case "DeviceUseStatement":
                        break;
                    case "DiagnosticReport":
                        return "encounter";
                    case "DocumentManifest":
                        return "related-ref";
                    case "DocumentReference":
                        return "encounter";
                    case "EffectEvidenceSynthesis":
                        break;
                    case "Encounter":
                        return "{def}";
                    case "Endpoint":
                        break;
                    case "EnrollmentRequest":
                        break;
                    case "EnrollmentResponse":
                        break;
                    case "EpisodeOfCare":
                        break;
                    case "EventDefinition":
                        break;
                    case "Evidence":
                        break;
                    case "EvidenceVariable":
                        break;
                    case "ExampleScenario":
                        break;
                    case "ExplanationOfBenefit":
                        return "encounter";
                    case "FamilyMemberHistory":
                        break;
                    case "Flag":
                        break;
                    case "Goal":
                        break;
                    case "GraphDefinition":
                        break;
                    case "Group":
                        break;
                    case "GuidanceResponse":
                        break;
                    case "HealthcareService":
                        break;
                    case "ImagingStudy":
                        break;
                    case "Immunization":
                        break;
                    case "ImmunizationEvaluation":
                        break;
                    case "ImmunizationRecommendation":
                        break;
                    case "ImplementationGuide":
                        break;
                    case "InsurancePlan":
                        break;
                    case "Invoice":
                        break;
                    case "Library":
                        break;
                    case "Linkage":
                        break;
                    case "List":
                        break;
                    case "Location":
                        break;
                    case "Measure":
                        break;
                    case "MeasureReport":
                        break;
                    case "Media":
                        return "encounter";
                    case "Medication":
                        break;
                    case "MedicationAdministration":
                        return "context";
                    case "MedicationDispense":
                        break;
                    case "MedicationKnowledge":
                        break;
                    case "MedicationRequest":
                        return "encounter";
                    case "MedicationStatement":
                        break;
                    case "MedicinalProduct":
                        break;
                    case "MedicinalProductAuthorization":
                        break;
                    case "MedicinalProductContraindication":
                        break;
                    case "MedicinalProductIndication":
                        break;
                    case "MedicinalProductIngredient":
                        break;
                    case "MedicinalProductInteraction":
                        break;
                    case "MedicinalProductManufactured":
                        break;
                    case "MedicinalProductPackaged":
                        break;
                    case "MedicinalProductPharmaceutical":
                        break;
                    case "MedicinalProductUndesirableEffect":
                        break;
                    case "MessageDefinition":
                        break;
                    case "MessageHeader":
                        break;
                    case "MolecularSequence":
                        break;
                    case "NamingSystem":
                        break;
                    case "NutritionOrder":
                        return "encounter";
                    case "Observation":
                        return "encounter";
                    case "ObservationDefinition":
                        break;
                    case "OperationDefinition":
                        break;
                    case "OperationOutcome":
                        break;
                    case "Organization":
                        break;
                    case "OrganizationAffiliation":
                        break;
                    case "Patient":
                        break;
                    case "PaymentNotice":
                        break;
                    case "PaymentReconciliation":
                        break;
                    case "Person":
                        break;
                    case "PlanDefinition":
                        break;
                    case "Practitioner":
                        break;
                    case "PractitionerRole":
                        break;
                    case "Procedure":
                        return "encounter";
                    case "Provenance":
                        break;
                    case "Questionnaire":
                        break;
                    case "QuestionnaireResponse":
                        return "encounter";
                    case "RelatedPerson":
                        break;
                    case "RequestGroup":
                        return "encounter";
                    case "ResearchDefinition":
                        break;
                    case "ResearchElementDefinition":
                        break;
                    case "ResearchStudy":
                        break;
                    case "ResearchSubject":
                        break;
                    case "RiskAssessment":
                        break;
                    case "RiskEvidenceSynthesis":
                        break;
                    case "Schedule":
                        break;
                    case "SearchParameter":
                        break;
                    case "ServiceRequest":
                        return "encounter";
                    case "Slot":
                        break;
                    case "Specimen":
                        break;
                    case "SpecimenDefinition":
                        break;
                    case "StructureDefinition":
                        break;
                    case "StructureMap":
                        break;
                    case "Subscription":
                        break;
                    case "Substance":
                        break;
                    case "SubstanceNucleicAcid":
                        break;
                    case "SubstancePolymer":
                        break;
                    case "SubstanceProtein":
                        break;
                    case "SubstanceReferenceInformation":
                        break;
                    case "SubstanceSourceMaterial":
                        break;
                    case "SubstanceSpecification":
                        break;
                    case "SupplyDelivery":
                        break;
                    case "SupplyRequest":
                        break;
                    case "Task":
                        break;
                    case "TerminologyCapabilities":
                        break;
                    case "TestReport":
                        break;
                    case "TestScript":
                        break;
                    case "ValueSet":
                        break;
                    case "VerificationResult":
                        break;
                    case "VisionPrescription":
                        return "encounter";
                }
                break;
            case "Patient":
                switch (dataType) {
                    case "Account":
                        return "subject";
                    case "ActivityDefinition":
                        break;
                    case "AdverseEvent":
                        return "subject";
                    case "AllergyIntolerance":
                        return "patient";
                    case "Appointment":
                        return "actor";
                    case "AppointmentResponse":
                        return "actor";
                    case "AuditEvent":
                        return "patient";
                    case "Basic":
                        return "patient";
                    case "Binary":
                        break;
                    case "BiologicallyDerivedProduct":
                        break;
                    case "BodyStructure":
                        return "patient";
                    case "Bundle":
                        break;
                    case "CapabilityStatement":
                        break;
                    case "CarePlan":
                        return "patient";
                    case "CareTeam":
                        return "patient";
                    case "CatalogEntry":
                        break;
                    case "ChargeItem":
                        return "subject";
                    case "ChargeItemDefinition":
                        break;
                    case "Claim":
                        return "patient";
                    case "ClaimResponse":
                        return "patient";
                    case "ClinicalImpression":
                        return "subject";
                    case "CodeSystem":
                        break;
                    case "Communication":
                        return "subject";
                    case "CommunicationRequest":
                        return "subject";
                    case "CompartmentDefinition":
                        break;
                    case "Composition":
                        return "subject";
                    case "ConceptMap":
                        break;
                    case "Condition":
                        return "patient";
                    case "Consent":
                        return "patient";
                    case "Contract":
                        break;
                    case "Coverage":
                        return "patient";
                    case "CoverageEligibilityRequest":
                        return "patient";
                    case "CoverageEligibilityResponse":
                        return "patient";
                    case "DetectedIssue":
                        return "patient";
                    case "Device":
                        break;
                    case "DeviceDefinition":
                        break;
                    case "DeviceMetric":
                        break;
                    case "DeviceRequest":
                        return "subject";
                    case "DeviceUseStatement":
                        return "subject";
                    case "DiagnosticReport":
                        return "subject";
                    case "DocumentManifest":
                        return "subject";
                    case "DocumentReference":
                        return "subject";
                    case "EffectEvidenceSynthesis":
                        break;
                    case "Encounter":
                        return "patient";
                    case "Endpoint":
                        break;
                    case "EnrollmentRequest":
                        return "subject";
                    case "EnrollmentResponse":
                        break;
                    case "EpisodeOfCare":
                        return "patient";
                    case "EventDefinition":
                        break;
                    case "Evidence":
                        break;
                    case "EvidenceVariable":
                        break;
                    case "ExampleScenario":
                        break;
                    case "ExplanationOfBenefit":
                        return "patient";
                    case "FamilyMemberHistory":
                        return "patient";
                    case "Flag":
                        return "patient";
                    case "Goal":
                        return "patient";
                    case "GraphDefinition":
                        break;
                    case "Group":
                        return "member";
                    case "GuidanceResponse":
                        break;
                    case "HealthcareService":
                        break;
                    case "ImagingStudy":
                        return "patient";
                    case "Immunization":
                        return "patient";
                    case "ImmunizationEvaluation":
                        return "patient";
                    case "ImmunizationRecommendation":
                        return "patient";
                    case "ImplementationGuide":
                        break;
                    case "InsurancePlan":
                        break;
                    case "Invoice":
                        return "subject";
                    case "Library":
                        break;
                    case "Linkage":
                        break;
                    case "List":
                        return "subject";
                    case "Location":
                        break;
                    case "Measure":
                        break;
                    case "MeasureReport":
                        return "patient";
                    case "Media":
                        return "subject";
                    case "Medication":
                        break;
                    case "MedicationAdministration":
                        return "patient";
                    case "MedicationDispense":
                        return "patient";
                    case "MedicationKnowledge":
                        break;
                    case "MedicationRequest":
                        return "subject";
                    case "MedicationStatement":
                        return "subject";
                    case "MedicinalProduct":
                        break;
                    case "MedicinalProductAuthorization":
                        break;
                    case "MedicinalProductContraindication":
                        break;
                    case "MedicinalProductIndication":
                        break;
                    case "MedicinalProductIngredient":
                        break;
                    case "MedicinalProductInteraction":
                        break;
                    case "MedicinalProductManufactured":
                        break;
                    case "MedicinalProductPackaged":
                        break;
                    case "MedicinalProductPharmaceutical":
                        break;
                    case "MedicinalProductUndesirableEffect":
                        break;
                    case "MessageDefinition":
                        break;
                    case "MessageHeader":
                        break;
                    case "MolecularSequence":
                        return "patient";
                    case "NamingSystem":
                        break;
                    case "NutritionOrder":
                        return "patient";
                    case "Observation":
                        return "subject";
                    case "ObservationDefinition":
                        break;
                    case "OperationDefinition":
                        break;
                    case "OperationOutcome":
                        break;
                    case "Organization":
                        break;
                    case "OrganizationAffiliation":
                        break;
                    case "Patient":
                        return "_id";
                    case "PaymentNotice":
                        break;
                    case "PaymentReconciliation":
                        break;
                    case "Person":
                        return "patient";
                    case "PlanDefinition":
                        break;
                    case "Practitioner":
                        break;
                    case "PractitionerRole":
                        break;
                    case "Procedure":
                        return "patient";
                    case "Provenance":
                        return "patient";
                    case "Questionnaire":
                        break;
                    case "QuestionnaireResponse":
                        return "subject";
                    case "RelatedPerson":
                        return "patient";
                    case "RequestGroup":
                        return "subject";
                    case "ResearchDefinition":
                        break;
                    case "ResearchElementDefinition":
                        break;
                    case "ResearchStudy":
                        break;
                    case "ResearchSubject":
                        return "individual";
                    case "RiskAssessment":
                        return "subject";
                    case "RiskEvidenceSynthesis":
                        break;
                    case "Schedule":
                        return "actor";
                    case "SearchParameter":
                        break;
                    case "ServiceRequest":
                        return "subject";
                    case "Slot":
                        break;
                    case "Specimen":
                        return "subject";
                    case "SpecimenDefinition":
                        break;
                    case "StructureDefinition":
                        break;
                    case "StructureMap":
                        break;
                    case "Subscription":
                        break;
                    case "Substance":
                        break;
                    case "SubstanceNucleicAcid":
                        break;
                    case "SubstancePolymer":
                        break;
                    case "SubstanceProtein":
                        break;
                    case "SubstanceReferenceInformation":
                        break;
                    case "SubstanceSourceMaterial":
                        break;
                    case "SubstanceSpecification":
                        break;
                    case "SupplyDelivery":
                        return "patient";
                    case "SupplyRequest":
                        return "subject";
                    case "Task":
                        break;
                    case "TerminologyCapabilities":
                        break;
                    case "TestReport":
                        break;
                    case "TestScript":
                        break;
                    case "ValueSet":
                        break;
                    case "VerificationResult":
                        break;
                    case "VisionPrescription":
                        return "patient";
                }
                break;
            case "Practitioner":
                switch (dataType) {
                    case "Account":
                        return "subject";
                    case "ActivityDefinition":
                        break;
                    case "AdverseEvent":
                        return "recorder";
                    case "AllergyIntolerance":
                        break;
                    case "Appointment":
                        return "actor";
                    case "AppointmentResponse":
                        return "actor";
                    case "AuditEvent":
                        return "agent";
                    case "Basic":
                        return "author";
                    case "Binary":
                        break;
                    case "BiologicallyDerivedProduct":
                        break;
                    case "BodyStructure":
                        break;
                    case "Bundle":
                        break;
                    case "CapabilityStatement":
                        break;
                    case "CarePlan":
                        return "performer";
                    case "CareTeam":
                        return "participant";
                    case "CatalogEntry":
                        break;
                    case "ChargeItem":
                        break;
                    case "ChargeItemDefinition":
                        break;
                    case "Claim":
                        break;
                    case "ClaimResponse":
                        return "requestor";
                    case "ClinicalImpression":
                        return "assessor";
                    case "CodeSystem":
                        break;
                    case "Communication":
                        break;
                    case "CommunicationRequest":
                        break;
                    case "CompartmentDefinition":
                        break;
                    case "Composition":
                        break;
                    case "ConceptMap":
                        break;
                    case "Condition":
                        return "asserter";
                    case "Consent":
                        break;
                    case "Contract":
                        break;
                    case "Coverage":
                        break;
                    case "CoverageEligibilityRequest":
                        break;
                    case "CoverageEligibilityResponse":
                        return "requestor";
                    case "DetectedIssue":
                        return "author";
                    case "Device":
                        break;
                    case "DeviceDefinition":
                        break;
                    case "DeviceMetric":
                        break;
                    case "DeviceRequest":
                        break;
                    case "DeviceUseStatement":
                        break;
                    case "DiagnosticReport":
                        return "performer";
                    case "DocumentManifest":
                        break;
                    case "DocumentReference":
                        break;
                    case "EffectEvidenceSynthesis":
                        break;
                    case "Encounter":
                        break;
                    case "Endpoint":
                        break;
                    case "EnrollmentRequest":
                        break;
                    case "EnrollmentResponse":
                        break;
                    case "EpisodeOfCare":
                        return "care-manager";
                    case "EventDefinition":
                        break;
                    case "Evidence":
                        break;
                    case "EvidenceVariable":
                        break;
                    case "ExampleScenario":
                        break;
                    case "ExplanationOfBenefit":
                        break;
                    case "FamilyMemberHistory":
                        break;
                    case "Flag":
                        return "author";
                    case "Goal":
                        break;
                    case "GraphDefinition":
                        break;
                    case "Group":
                        return "member";
                    case "GuidanceResponse":
                        break;
                    case "HealthcareService":
                        break;
                    case "ImagingStudy":
                        break;
                    case "Immunization":
                        return "performer";
                    case "ImmunizationEvaluation":
                        break;
                    case "ImmunizationRecommendation":
                        break;
                    case "ImplementationGuide":
                        break;
                    case "InsurancePlan":
                        break;
                    case "Invoice":
                        return "participant";
                    case "Library":
                        break;
                    case "Linkage":
                        return "author";
                    case "List":
                        return "source";
                    case "Location":
                        break;
                    case "Measure":
                        break;
                    case "MeasureReport":
                        break;
                    case "Media":
                        break;
                    case "Medication":
                        break;
                    case "MedicationAdministration":
                        return "performer";
                    case "MedicationDispense":
                        break;
                    case "MedicationKnowledge":
                        break;
                    case "MedicationRequest":
                        return "requester";
                    case "MedicationStatement":
                        return "source";
                    case "MedicinalProduct":
                        break;
                    case "MedicinalProductAuthorization":
                        break;
                    case "MedicinalProductContraindication":
                        break;
                    case "MedicinalProductIndication":
                        break;
                    case "MedicinalProductIngredient":
                        break;
                    case "MedicinalProductInteraction":
                        break;
                    case "MedicinalProductManufactured":
                        break;
                    case "MedicinalProductPackaged":
                        break;
                    case "MedicinalProductPharmaceutical":
                        break;
                    case "MedicinalProductUndesirableEffect":
                        break;
                    case "MessageDefinition":
                        break;
                    case "MessageHeader":
                        break;
                    case "MolecularSequence":
                        break;
                    case "NamingSystem":
                        break;
                    case "NutritionOrder":
                        return "provider";
                    case "Observation":
                        return "performer";
                    case "ObservationDefinition":
                        break;
                    case "OperationDefinition":
                        break;
                    case "OperationOutcome":
                        break;
                    case "Organization":
                        break;
                    case "OrganizationAffiliation":
                        break;
                    case "Patient":
                        return "general-practitioner";
                    case "PaymentNotice":
                        return "provider";
                    case "PaymentReconciliation":
                        return "requestor";
                    case "Person":
                        return "practitioner";
                    case "PlanDefinition":
                        break;
                    case "Practitioner":
                        return "{def}";
                    case "PractitionerRole":
                        return "practitioner";
                    case "Procedure":
                        return "performer";
                    case "Provenance":
                        return "agent";
                    case "Questionnaire":
                        break;
                    case "QuestionnaireResponse":
                        break;
                    case "RelatedPerson":
                        break;
                    case "RequestGroup":
                        break;
                    case "ResearchDefinition":
                        break;
                    case "ResearchElementDefinition":
                        break;
                    case "ResearchStudy":
                        return "principalinvestigator";
                    case "ResearchSubject":
                        break;
                    case "RiskAssessment":
                        return "performer";
                    case "RiskEvidenceSynthesis":
                        break;
                    case "Schedule":
                        return "actor";
                    case "SearchParameter":
                        break;
                    case "ServiceRequest":
                        break;
                    case "Slot":
                        break;
                    case "Specimen":
                        return "collector";
                    case "SpecimenDefinition":
                        break;
                    case "StructureDefinition":
                        break;
                    case "StructureMap":
                        break;
                    case "Subscription":
                        break;
                    case "Substance":
                        break;
                    case "SubstanceNucleicAcid":
                        break;
                    case "SubstancePolymer":
                        break;
                    case "SubstanceProtein":
                        break;
                    case "SubstanceReferenceInformation":
                        break;
                    case "SubstanceSourceMaterial":
                        break;
                    case "SubstanceSpecification":
                        break;
                    case "SupplyDelivery":
                        break;
                    case "SupplyRequest":
                        return "requester";
                    case "Task":
                        break;
                    case "TerminologyCapabilities":
                        break;
                    case "TestReport":
                        break;
                    case "TestScript":
                        break;
                    case "ValueSet":
                        break;
                    case "VerificationResult":
                        break;
                    case "VisionPrescription":
                        return "prescriber";
                }
                break;
            case "RelatedPerson":
                switch (dataType) {
                    case "Account":
                        break;
                    case "ActivityDefinition":
                        break;
                    case "AdverseEvent":
                        return "recorder";
                    case "AllergyIntolerance":
                        return "asserter";
                    case "Appointment":
                        return "actor";
                    case "AppointmentResponse":
                        return "actor";
                    case "AuditEvent":
                        break;
                    case "Basic":
                        return "author";
                    case "Binary":
                        break;
                    case "BiologicallyDerivedProduct":
                        break;
                    case "BodyStructure":
                        break;
                    case "Bundle":
                        break;
                    case "CapabilityStatement":
                        break;
                    case "CarePlan":
                        return "performer";
                    case "CareTeam":
                        return "participant";
                    case "CatalogEntry":
                        break;
                    case "ChargeItem":
                        break;
                    case "ChargeItemDefinition":
                        break;
                    case "Claim":
                        return "payee";
                    case "ClaimResponse":
                        break;
                    case "ClinicalImpression":
                        break;
                    case "CodeSystem":
                        break;
                    case "Communication":
                        break;
                    case "CommunicationRequest":
                        break;
                    case "CompartmentDefinition":
                        break;
                    case "Composition":
                        return "author";
                    case "ConceptMap":
                        break;
                    case "Condition":
                        return "asserter";
                    case "Consent":
                        break;
                    case "Contract":
                        break;
                    case "Coverage":
                        break;
                    case "CoverageEligibilityRequest":
                        break;
                    case "CoverageEligibilityResponse":
                        break;
                    case "DetectedIssue":
                        break;
                    case "Device":
                        break;
                    case "DeviceDefinition":
                        break;
                    case "DeviceMetric":
                        break;
                    case "DeviceRequest":
                        break;
                    case "DeviceUseStatement":
                        break;
                    case "DiagnosticReport":
                        break;
                    case "DocumentManifest":
                        break;
                    case "DocumentReference":
                        return "author";
                    case "EffectEvidenceSynthesis":
                        break;
                    case "Encounter":
                        return "participant";
                    case "Endpoint":
                        break;
                    case "EnrollmentRequest":
                        break;
                    case "EnrollmentResponse":
                        break;
                    case "EpisodeOfCare":
                        break;
                    case "EventDefinition":
                        break;
                    case "Evidence":
                        break;
                    case "EvidenceVariable":
                        break;
                    case "ExampleScenario":
                        break;
                    case "ExplanationOfBenefit":
                        return "payee";
                    case "FamilyMemberHistory":
                        break;
                    case "Flag":
                        break;
                    case "Goal":
                        break;
                    case "GraphDefinition":
                        break;
                    case "Group":
                        break;
                    case "GuidanceResponse":
                        break;
                    case "HealthcareService":
                        break;
                    case "ImagingStudy":
                        break;
                    case "Immunization":
                        break;
                    case "ImmunizationEvaluation":
                        break;
                    case "ImmunizationRecommendation":
                        break;
                    case "ImplementationGuide":
                        break;
                    case "InsurancePlan":
                        break;
                    case "Invoice":
                        return "recipient";
                    case "Library":
                        break;
                    case "Linkage":
                        break;
                    case "List":
                        break;
                    case "Location":
                        break;
                    case "Measure":
                        break;
                    case "MeasureReport":
                        break;
                    case "Media":
                        break;
                    case "Medication":
                        break;
                    case "MedicationAdministration":
                        return "performer";
                    case "MedicationDispense":
                        break;
                    case "MedicationKnowledge":
                        break;
                    case "MedicationRequest":
                        break;
                    case "MedicationStatement":
                        return "source";
                    case "MedicinalProduct":
                        break;
                    case "MedicinalProductAuthorization":
                        break;
                    case "MedicinalProductContraindication":
                        break;
                    case "MedicinalProductIndication":
                        break;
                    case "MedicinalProductIngredient":
                        break;
                    case "MedicinalProductInteraction":
                        break;
                    case "MedicinalProductManufactured":
                        break;
                    case "MedicinalProductPackaged":
                        break;
                    case "MedicinalProductPharmaceutical":
                        break;
                    case "MedicinalProductUndesirableEffect":
                        break;
                    case "MessageDefinition":
                        break;
                    case "MessageHeader":
                        break;
                    case "MolecularSequence":
                        break;
                    case "NamingSystem":
                        break;
                    case "NutritionOrder":
                        break;
                    case "Observation":
                        return "performer";
                    case "ObservationDefinition":
                        break;
                    case "OperationDefinition":
                        break;
                    case "OperationOutcome":
                        break;
                    case "Organization":
                        break;
                    case "OrganizationAffiliation":
                        break;
                    case "Patient":
                        return "link";
                    case "PaymentNotice":
                        break;
                    case "PaymentReconciliation":
                        break;
                    case "Person":
                        return "link";
                    case "PlanDefinition":
                        break;
                    case "Practitioner":
                        break;
                    case "PractitionerRole":
                        break;
                    case "Procedure":
                        return "performer";
                    case "Provenance":
                        return "agent";
                    case "Questionnaire":
                        break;
                    case "QuestionnaireResponse":
                        break;
                    case "RelatedPerson":
                        return "{def}";
                    case "RequestGroup":
                        return "participant";
                    case "ResearchDefinition":
                        break;
                    case "ResearchElementDefinition":
                        break;
                    case "ResearchStudy":
                        break;
                    case "ResearchSubject":
                        break;
                    case "RiskAssessment":
                        break;
                    case "RiskEvidenceSynthesis":
                        break;
                    case "Schedule":
                        return "actor";
                    case "SearchParameter":
                        break;
                    case "ServiceRequest":
                        return "performer";
                    case "Slot":
                        break;
                    case "Specimen":
                        break;
                    case "SpecimenDefinition":
                        break;
                    case "StructureDefinition":
                        break;
                    case "StructureMap":
                        break;
                    case "Subscription":
                        break;
                    case "Substance":
                        break;
                    case "SubstanceNucleicAcid":
                        break;
                    case "SubstancePolymer":
                        break;
                    case "SubstanceProtein":
                        break;
                    case "SubstanceReferenceInformation":
                        break;
                    case "SubstanceSourceMaterial":
                        break;
                    case "SubstanceSpecification":
                        break;
                    case "SupplyDelivery":
                        break;
                    case "SupplyRequest":
                        return "requester";
                    case "Task":
                        break;
                    case "TerminologyCapabilities":
                        break;
                    case "TestReport":
                        break;
                    case "TestScript":
                        break;
                    case "ValueSet":
                        break;
                    case "VerificationResult":
                        break;
                    case "VisionPrescription":
                        break;
                }
                break;
        }

        return null;
    }

}