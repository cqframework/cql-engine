package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.codesystems.DaysOfWeek;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bryn on 9/13/2016.
 */
public abstract class BaseFhirDataProvider implements DataProvider
{
    protected FhirContext fhirContext;

    public BaseFhirDataProvider() {
        this.packageName = "org.hl7.fhir.dstu3.model";
        this.fhirContext = FhirContext.forDstu3();
    }

    // for DSTU2 and earlier support
    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }
    public FhirContext getFhirContext() { return fhirContext; }

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath, Interval dateRange) {
        return null;
    }

    private String packageName;
    @Override
    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
      this.packageName = packageName;
    }

    public BaseFhirDataProvider withPackageName(String packageName) {
      setPackageName(packageName);
      return this;
    }

    protected DateTime toDateTime(Date result) {
        // NOTE: By going through the Java primitive here, we are losing the precision support of the HAPI-DateTimeType
        return DateTime.fromJavaDate(result);
    }

    protected DateTime toDateTime(DateTimeType value) {
        // TODO: Convert tzHour, tzMin and tzSign to a BigDecimal to set TimeZoneOffset
        switch (value.getPrecision()) {
            case YEAR: return new DateTime(value.getYear());
            case MONTH: return new DateTime(value.getYear(), value.getMonth());
            case DAY: return new DateTime(value.getYear(), value.getMonth(), value.getDay());
            case SECOND: return new DateTime(value.getYear(), value.getMonth(), value.getDay(), value.getHour(), value.getMinute(), value.getSecond());
            case MILLI: return new DateTime(value.getYear(), value.getMonth(), value.getDay(), value.getHour(), value.getMinute(), value.getSecond(), value.getMillis());
            default: throw new IllegalArgumentException(String.format("Invalid temporal precision %s", value.getPrecision().toString()));
        }
    }

    protected DateTime toDateTime(DateType value) {
        // TODO: This ought to work, but I'm getting an incorrect month value returned from the Hapi DateType, looks like a Java Calendar problem?
        switch (value.getPrecision()) {
            case YEAR: return new DateTime(value.getYear());
            case MONTH: return new DateTime(value.getYear(), value.getMonth() + 1); // Month is zero based in DateType.
            case DAY: return new DateTime(value.getYear(), value.getMonth() + 1, value.getDay());
            default: throw new IllegalArgumentException(String.format("Invalid temporal precision %s", value.getPrecision().toString()));
        }
    }

    protected Time toTime(TimeType value) {
        throw new RuntimeException("Time values are not supported yet.");
    }

    protected DateTime toDateTime(InstantType value) {
        // TODO: Timezone support
        return new DateTime(value.getYear(), value.getMonth(), value.getDay(), value.getHour(), value.getMinute(), value.getSecond(), value.getMillis());
    }

    protected Object mapPrimitive(Object result, Object source) {
        if (source instanceof DateTimeType) {
            return toDateTime((DateTimeType)source);
        }
        else if (source instanceof DateType) {
            return toDateTime((DateType)source);
        }
        else if (source instanceof TimeType) {
            return toTime((TimeType)source);
        }
        else if (source instanceof InstantType) {
            return toDateTime((InstantType)source);
        }
        else {
            return result;
        }

        // The HAPI primitive types use the same Java types as the CQL Engine with the exception of the date types,
        // where the HAPI classes return Java Dates, the engine expects runtime.DateTime instances

//        if (result instanceof BoundCodeDt) {
//            return ((BoundCodeDt)result).getValue();
//        }
//
//        if (result instanceof BaseDateTimeDt) {
//            return DateTime.fromJavaDate(((BaseDateTimeDt)result).getValue());
//        }
//
//        if (result instanceof BaseDateTimeType) {
//            return DateTime.fromJavaDate(((BaseDateTimeType)result).getValue());
//        }
//
//        if (result instanceof TimeType) {
//            return toTime((TimeType)result);
//        }
//
//        return result;
    }

    protected boolean pathIsChoice(String path) {
        // TODO: Better support for choice types, needs to be more generic, quick fix for now...
        return path.endsWith("DateTime") || path.endsWith("Period");
    }

    protected Object resolveChoiceProperty(Object target, String path, String typeName) {
        String rootPath = path.substring(0, path.indexOf(typeName));
        return resolveProperty(target, rootPath);
    }

    protected Object resolveChoiceProperty(Object target, String path) {
        if (path.endsWith("DateTime")) {
            Object result = resolveChoiceProperty(target, path, "DateTime");
            if (!(result instanceof DateTimeType)) {
                return null;
            }
//            if (result != null && !(result instanceof DateTime)) {
//                throw new IllegalArgumentException(String.format(
//                        "Choice property %s of resource %s was accessed as a DateTime, but is present as a %s.",
//                        path, target.getClass().getSimpleName(), result.getClass().getSimpleName()));
//            }

            return result;
        }

        if (path.endsWith("Period")) {
            Object result = resolveChoiceProperty(target, path, "Period");
            if (!(result instanceof Period)) {
                return null;
            }
//            if (result != null && !(result instanceof Period)) {
//                throw new IllegalArgumentException(String.format(
//                        "Choice property %s of resource %s was accessed as a Period, but is present as a %s.",
//                        path, target.getClass().getSimpleName(), result.getClass().getSimpleName()));
//            }

            return result;
        }

        throw new IllegalArgumentException(String.format("Unknown choice type for choice path %s", path));
    }

    protected Object resolveProperty(Object target, String path) {
        if (target == null) {
            return null;
        }

        if (target instanceof Enumeration && path.equals("value")) {
            return ((Enumeration)target).getValueAsString();
        }

        Class<? extends Object> clazz = target.getClass();
        try {
            String accessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
            String elementAccessorMethodName = String.format("%sElement", accessorMethodName);
            Method accessor = null;
            try {
                accessor = clazz.getMethod(elementAccessorMethodName);
            }
            catch (NoSuchMethodException e) {
                accessor = clazz.getMethod(accessorMethodName);
            }

            Object result = accessor.invoke(target);
            result = mapPrimitive(result, target);
            return result;
        } catch (NoSuchMethodException e) {
            if (pathIsChoice(path)) {
                return resolveChoiceProperty(target, path);
            }
            else {
                throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
            }
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
    }

    @Override
    public Object resolvePath(Object target, String path) {
        String[] identifiers = path.split("\\.");
        for (String identifier : identifiers) {
            // handling indexes: item[0].code
            if (identifier.contains("[")) {
                int j = Character.getNumericValue(identifier.charAt(identifier.indexOf("[") + 1));
                target = resolveProperty(target, identifier.replaceAll("\\[\\d\\]", ""));
                target = ((ArrayList) target).get(j);
            } else
                target = resolveProperty(target, identifier);
        }

        return target;
    }

    @Override
    public Class resolveType(String typeName) {
        try {
            // TODO: Obviously would like to be able to automate this, but there is no programmatic way of which I'm aware
            // For the primitive types, not such a big deal.
            // For the enumerations, the type names are generated from the binding name in the spreadsheet, which doesn't make it to the StructureDefinition,
            // and the schema has no way of indicating whether the enum will be common (i.e. in Enumerations) or per resource
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
                case "uri": typeName = "UriType"; break;
                case "uuid": typeName = "UuidType"; break;
                case "id": typeName = "IdType"; break;
                case "oid": typeName = "OidType"; break;
                case "PlanActionPrecheckBehavior": typeName = "PlanDefinition$PlanActionPrecheckBehavior"; break;
                case "ProvenanceEntityRole": typeName = "Provenance$ProvenanceEntityRole"; break;
                case "UnitsOfTime": typeName = "Timing$UnitsOfTime"; break;
                case "AddressType": typeName = "Address$AddressType"; break;
                case "AllergyIntoleranceCategory": typeName = "AllergyIntolerance$AllergyIntoleranceCategory"; break;
                case "SpecimenStatus": typeName = "Specimen$SpecimenStatus"; break;
                case "RestfulCapabilityMode": typeName = "CapabilityStatement$RestfulCapabilityMode"; break;
                case "DetectedIssueSeverity": typeName = "DetectedIssue$DetectedIssueSeverity"; break;
                case "IssueSeverity": typeName = "OperationOutcome$IssueSeverity"; break;
                case "DataElementStringency": typeName = "DataElement$DataElementStringency"; break;
                case "PlanActionConditionKind": typeName = "PlanDefinition$PlanActionConditionKind"; break;
                case "EncounterStatus": typeName = "Encounter$EncounterStatus"; break;
                case "StructureDefinitionKind": typeName = "StructureDefinition$StructureDefinitionKind"; break;
                case "PublicationStatus": typeName = "Enumerations$PublicationStatus"; break;
                case "ConsentDataMeaning": typeName = "Consent$ConsentDataMeaning"; break;
                case "QuestionnaireResponseStatus": typeName = "QuestionnaireResponse$QuestionnaireResponseStatus"; break;
                case "SearchComparator": typeName = "SearchParameter$SearchComparator"; break;
                case "AllergyIntoleranceType": typeName = "AllergyIntolerance$AllergyIntoleranceType"; break;
                case "DocumentRelationshipType": typeName = "DocumentReference$DocumentRelationshipType"; break;
                case "AllergyIntoleranceClinicalStatus": typeName = "AllergyIntolerance$AllergyIntoleranceClinicalStatus"; break;
                case "CarePlanActivityStatus": typeName = "CarePlan$CarePlanActivityStatus"; break;
                case "ActionList": typeName = "ProcessRequest$ActionList"; break;
                case "ParticipationStatus": typeName = "Appointment$ParticipationStatus"; break;
                case "PlanActionSelectionBehavior": typeName = "PlanDefinition$PlanActionSelectionBehavior"; break;
                case "DocumentMode": typeName = "CapabilityStatement$DocumentMode"; break;
                case "AssertionOperatorType": typeName = "TestScript$AssertionOperatorType"; break;
                case "DaysOfWeek": typeName = "HealthcareService$DaysOfWeek"; break;
                case "IssueType": typeName = "OperationOutcome$IssueType"; break;
                case "ContentType": typeName = "TestScript$ContentType"; break;
                case "StructureMapContextType": typeName = "StructureMap$StructureMapContextType"; break;
                case "FamilyHistoryStatus": typeName = "FamilyMemberHistory$FamilyHistoryStatus"; break;
                case "MedicationStatementCategory": typeName = "MedicationStatement$MedicationStatementCategory"; break;
                case "CommunicationStatus": typeName = "Communication$CommunicationStatus"; break;
                case "ClinicalImpressionStatus": typeName = "ClinicalImpression$ClinicalImpressionStatus"; break;
                case "AssertionResponseTypes": typeName = "TestScript$AssertionResponseTypes"; break;
                case "NarrativeStatus": typeName = "Narrative$NarrativeStatus"; break;
                case "ReferralCategory": typeName = "ReferralRequest$ReferralCategory"; break;
                case "MeasmntPrinciple": typeName = "DeviceComponent$MeasmntPrinciple"; break;
                case "ConsentExceptType": typeName = "Consent$ConsentExceptType"; break;
                case "EndpointStatus": typeName = "Endpoint$EndpointStatus"; break;
                case "GuidePageKind": typeName = "ImplementationGuide$GuidePageKind"; break;
                case "GuideDependencyType": typeName = "ImplementationGuide$GuideDependencyType"; break;
                case "ResourceVersionPolicy": typeName = "CapabilityStatement$ResourceVersionPolicy"; break;
                case "MedicationRequestStatus": typeName = "MedicationRequest$MedicationRequestStatus"; break;
                case "MedicationAdministrationStatus": typeName = "MedicationAdministration$MedicationAdministrationStatus"; break;
                case "NamingSystemIdentifierType": typeName = "NamingSystem$NamingSystemIdentifierType"; break;
                case "AccountStatus": typeName = "Account$AccountStatus"; break;
                case "ProcedureRequestPriority": typeName = "ProcedureRequest$ProcedureRequestPriority"; break;
                case "MedicationDispenseStatus": typeName = "MedicationDispsense$MedicationDispenseStatus"; break;
                case "IdentifierUse": typeName = "Identifier$IdentifierUse"; break;
                case "DigitalMediaType": typeName = "Media$DigitalMediaType"; break;
                case "TestReportParticipantType": typeName = "TestReport$TestReportParticipantType"; break;
                case "BindingStrength": typeName = "Enumerations$BindingStrength"; break;
                case "ConsentStatus": typeName = "Consent$ConsentStatus"; break;
                case "ParticipantRequired": typeName = "Appointment$ParticipantRequired"; break;
                case "XPathUsageType": typeName = "SearchParameter$XPathUsageType"; break;
                case "StructureMapInputMode": typeName = "StructureMap$StructureMapInputMode"; break;
                case "InstanceAvailability": typeName = "ImagingStudy$InstanceAvailability"; break;
                case "LinkageType": typeName = "Linkage$LinkageType"; break;
                case "ReferenceHandlingPolicy": typeName = "CapabilityStatement$ReferenceHandlingPolicy"; break;
                case "FilterOperator": typeName = "CodeSystem$FilterOperator"; break;
                case "NamingSystemType": typeName = "NamingSystem$NamingSystemType"; break;
                case "ResearchStudyStatus": typeName = "ResearchStudy$ResearchStudyStatus"; break;
                case "ExtensionContext": typeName = "StructureDefinition$ExtensionContext"; break;
                case "AuditEventOutcome": typeName = "AuditEvent$AuditEventOutcome"; break;
                case "ConstraintSeverity": typeName = "ElementDefinition$ConstraintSeverity"; break;
                case "EventCapabilityMode": typeName = "CapabilityStatement$EventCapabilityMode"; break;
                case "PlanActionParticipantType": typeName = "PlanDefinition$PlanActionParticipantType"; break;
                case "ProcedureStatus": typeName = "Procedure$ProcedureStatus"; break;
                case "ResearchSubjectStatus": typeName = "ResearchSubject$ResearchSubjectStatus"; break;
                case "PlanActionGroupingBehavior": typeName = "PlanDefinition$PlanActionGroupingBehavior"; break;
                case "CompositeMeasureScoring": typeName = "Measure$CompositeMeasureScoring"; break;
                case "DeviceMetricCategory": typeName = "Device$DeviceMetricCategory"; break;
                case "QuestionnaireStatus": typeName = "Questionnaire$QuestionnaireStatus"; break;
                case "StructureMapTransform": typeName = "StructureMap$StructureMapTransform"; break;
                case "ResponseType": typeName = "MessageHeader$ResponseType"; break;
                case "AggregationMode": typeName = "ElementDefinition$AggregationMode"; break;
                case "CapabilityStatementKind": typeName = "CapabilityStatement$CapabilityStatementKind"; break;
                case "sequenceType": typeName = "Sequence$SequenceType"; break;
                case "AllergyIntoleranceVerificationStatus": typeName = "AllergyIntolerance$AllergyIntoleranceVerificationStatus"; break;
                case "EventTiming": typeName = "Timing$EventTiming"; break;
                case "GoalStatus": typeName = "Goal$GoalStatus"; break;
                case "SearchParamType": typeName = "Enumerations$SearchParamType"; break;
                case "SystemRestfulInteraction": typeName = "CapabilityStatement$SystemRestfulInteraction"; break;
                case "StructureMapModelMode": typeName = "StructureMap$StructureMapModelMode"; break;
                case "TaskStatus": typeName = "Task$TaskStatus"; break;
                case "MeasurePopulationType": typeName = "Measure$MeasurePopulationType"; break;
                case "SubscriptionChannelType": typeName = "Subscription$SubscriptionChannelType"; break;
                case "ProcedureRequestStatus": typeName = "ProcedureRequest$ProcedureRequestStatus"; break;
                case "ReferralStatus": typeName = "ReferralRequest$ReferralStatus"; break;
                case "AssertionDirectionType": typeName = "TestScript$AssertionDirectionType"; break;
                case "SlicingRules": typeName = "ElementDefinition$SlicingRules"; break;
                case "ExplanationOfBenefitStatus": typeName = "ExplanationOfBenefit$ExplanationOfBenefitStatus"; break;
                case "LinkType": typeName = "Patient$LinkType"; break;
                case "AllergyIntoleranceCriticality": typeName = "AllergyIntolerance$AllergyIntoleranceCriticality"; break;
                case "ConceptMapEquivalence": typeName = "ConceptMap$ConceptMapEquivalence"; break;
                case "PropertyRepresentation": typeName = "ElementDefinition$PropertyRepresentation"; break;
                case "AuditEventAction": typeName = "AuditEvent$AuditEventAction"; break;
                case "MeasureDataUsage": typeName = "Measure$MeasureDataUsage"; break;
                case "TriggerType": typeName = "TriggerDefinition$TriggerType"; break;
                case "ActivityDefinitionCategory": typeName = "ActivityDefinition$ActivityDefinitionCategory"; break;
                case "SearchModifierCode": typeName = "SearchParameter$SearchModifierCode"; break;
                case "CompositionStatus": typeName = "Composition$CompositionStatus"; break;
                case "AppointmentStatus": typeName = "Appointment$AppointmentStatus"; break;
                case "MessageSignificanceCategory": typeName = "Conformance$MessageSignificanceCategory"; break;
                case "OperationParameterUse": typeName = "OperationDefinition$OperationParameterUse"; break;
                case "ListMode": typeName = "ListResource$ListMode"; break;
                case "ObservationStatus": typeName = "Observation$ObservationStatus"; break;
                case "qualityType": typeName = "Sequence$QualityType"; break;
                case "AdministrativeGender": typeName = "Enumerations$AdministrativeGender"; break;
                case "MeasureType": typeName = "Measure$MeasureType"; break;
                case "QuestionnaireItemType": typeName = "Questionnaire$QuestionnaireItemType"; break;
                case "StructureMapListMode": typeName = "StructureMap$StructureMapListMode"; break;
                case "DeviceMetricCalibrationType": typeName = "DeviceMetric$DeviceMetricCalibrationType"; break;
                case "SupplyRequestStatus": typeName = "SupplyRequest$SupplyRequestStatus"; break;
                case "EncounterLocationStatus": typeName = "Encounter$EncounterLocationStatus"; break;
                case "SupplyDeliveryStatus": typeName = "SupplyDelivery$SupplyDeliveryStatus"; break;
                case "DiagnosticReportStatus": typeName = "DiagnosticReport$DiagnosticReportStatus"; break;
                case "FlagStatus": typeName = "Flag$FlagStatus"; break;
                case "AllergyIntoleranceCertainty": typeName = "AllergyIntolerance$AllergyIntoleranceCertainty"; break;
                case "CarePlanStatus": typeName = "CarePlan$CarePlanStatus"; break;
                case "ListStatus": typeName = "ListResource$ListStatus"; break;
                case "MeasureScoring": typeName = "Measure$MeasureScoring"; break;
                case "AuditEventAgentNetworkType": typeName = "AuditEvent$AuditEventAgentNetworkType"; break;
                case "AddressUse": typeName = "Address$AddressUse"; break;
                case "ConditionalDeleteStatus": typeName = "CapabilityStatement$ConditionalDeleteStatus"; break;
                case "ContactPointUse": typeName = "ContactPoint$ContactPointUse"; break;
                case "DeviceMetricOperationalStatus": typeName = "DeviceMetric$DeviceMetricOperationalStatus"; break;
                case "NutritionOrderStatus": typeName = "NutritionOrder$NutritionOrderStatus"; break;
                case "ContributorType": typeName = "Contributor$ContributorType"; break;
                case "ReferenceVersionRules": typeName = "ElementDefinition$ReferenceVersionRules"; break;
                case "Use": typeName = "Claim$Use"; break;
                case "IdentityAssuranceLevel": typeName = "Person$IdentityAssuranceLevel"; break;
                case "MeasureReportStatus": typeName = "MeasureReport$MeasureReportStatus"; break;
                case "DeviceMetricColor": typeName = "DeviceMetric$DeviceMetricColor"; break;
                case "SearchEntryMode": typeName = "SearchParameter$SearchEntryMode"; break;
                case "ConditionalReadStatus": typeName = "CapabilityStatement$ConditionalReadStatus"; break;
                case "ConditionVerificationStatus": typeName = "Condition$ConditionVerificationStatus"; break;
                case "AllergyIntoleranceSeverity": typeName = "AllergyIntolerance$AllergyIntoleranceSeverity"; break;
                case "OperationKind": typeName = "OperationDefinition$OperationKind"; break;
                case "ObservationRelationshipType": typeName = "Observation$ObservationRelationshipType"; break;
                case "NameUse": typeName = "HumanName$NameUse"; break;
                case "SubscriptionStatus": typeName = "Subscription$SubscriptionStatus"; break;
                case "DocumentReferenceStatus": typeName = "DocumentReference$DocumentReferenceStatus"; break;
                case "CommunicationRequestStatus": typeName = "CommunicationRequest$CommunicationRequestStatus"; break;
                case "LocationMode": typeName = "Location$LocationMode"; break;
                case "repositoryType": typeName = "Sequence$RepositoryType"; break;
                case "CarePlanRelationship": typeName = "CarePlan$CarePlanRelationship"; break;
                case "LocationStatus": typeName = "Location$LocationStatus"; break;
                case "UnknownContentCode": typeName = "CapabilityStatement$UnknownContentCode"; break;
                case "NoteType": typeName = "Enumerations$NoteType"; break;
                case "TestReportStatus": typeName = "TestReport$TestReportStatus"; break;
                case "HTTPVerb": typeName = "Bundle$HTTPVerb"; break;
                case "CodeSystemContentMode": typeName = "CodeSystem$CodeSystemContentMode"; break;
                case "PlanActionRelationshipType": typeName = "PlanDefinition$PlanActionRelationshipType"; break;
                case "EpisodeOfCareStatus": typeName = "EpisodeOfCare$EpisodeOfCareStatus"; break;
                case "RemittanceOutcome": typeName = "Enumerations$RemittanceOutcome"; break;
                case "ContactPointSystem": typeName = "ContactPoint$ContactPointSystem"; break;
                case "SlotStatus": typeName = "Slot$SlotStatus"; break;
                case "PropertyType": typeName = "CodeSystem$PropertyType"; break;
                case "TypeDerivationRule": typeName = "StructureDefinition$TypeDerivationRule"; break;
                case "MedicationStatementStatus": typeName = "MedicationStatement$MedicationStatementStatus"; break;
                case "GuidanceResponseStatus": typeName = "GuidanceResponse$GuidanceResponseStatus"; break;
                case "QuantityComparator": typeName = "Quantity$QuantityComparator"; break;
                case "RelatedArtifactType": typeName = "RelatedArtifact$RelatedArtifactType"; break;
                case "DeviceStatus": typeName = "Device$DeviceStatus"; break;
                case "TestReportResultCodes": typeName = "TestReport$TestReportResultCodes"; break;
                case "MeasureReportType": typeName = "MeasureReport$MeasureReportType"; break;
                case "SampledDataDataType": typeName = "SampledData$SampledDataDataType"; break;
                case "CompartmentType": typeName = "CompartmentDefinition$CompartmentType"; break;
                case "CompositionAttestationMode": typeName = "Composition$CompositionAttestationMode"; break;
                case "PlanActionRequiredBehavior": typeName = "PlanDefinition$PlanActionRequiredBehavior"; break;
                case "DeviceMetricCalibrationState": typeName = "DeviceMetric$DeviceMetricCalibrationState"; break;
                case "GroupType": typeName = "Group$GroupType"; break;
                case "TypeRestfulInteraction": typeName = "CapabilityStatement$TypeRestfulInteraction"; break;
                case "PlanActionCardinalityBehavior": typeName = "PlanDefinition$PlanActionCardinalityBehavior"; break;
                case "CodeSystemHierarchyMeaning": typeName = "CodeSystem$CodeSystemHierarchyMeaning"; break;
                case "MedicationStatementNotTaken": typeName = "MedicationStatement$MedicationStatementNotTaken"; break;
                case "BundleType": typeName = "Bundle$BundleType"; break;
                case "SystemVersionProcessingMode": typeName = "ExpansionProfile$SystemVersionProcessingMode"; break;
            }
            return Class.forName(String.format("%s.%s", packageName, typeName));
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("Could not resolve type %s.%s.", packageName, typeName));
        }
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        if (target == null) {
            return;
        }

        Class<? extends Object> clazz = target.getClass();
        try {
            String accessorMethodName = String.format("%s%s%s", "set", path.substring(0, 1).toUpperCase(), path.substring(1));
            Method accessor = clazz.getMethod(accessorMethodName);
            accessor.invoke(target, value);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
    }

    protected Field getProperty(Class clazz, String path) {
        try {
            Field field = clazz.getDeclaredField(path);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(String.format("Could not determine field for path %s of type %s", path, clazz.getSimpleName()));
        }
    }

    protected Method getReadAccessor(Class clazz, String path) {
        Field field = getProperty(clazz, path);
        String accessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
        Method accessor = null;
        try {
            accessor = clazz.getMethod(accessorMethodName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
        return accessor;
    }

    protected Method getWriteAccessor(Class clazz, String path) {
        Field field = getProperty(clazz, path);
        String accessorMethodName = String.format("%s%s%s", "set", path.substring(0, 1).toUpperCase(), path.substring(1));
        Method accessor = null;
        try {
            accessor = clazz.getMethod(accessorMethodName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
        return accessor;
    }
}
