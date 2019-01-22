package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.runtime.*;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Christopher Schuler on 6/19/2017.
 */
public class FhirDataProviderHL7 extends FhirDataProviderStu3 {

    // Although this is Dstu2, it is closer in package structure to the STU3 provider...

    public FhirDataProviderHL7() {
        setPackageName("org.hl7.fhir.instance.model");
        setFhirContext(FhirContext.forDstu2Hl7Org());
    }

    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange) {

        // Apply filtering based on
        //  profile (templateId)
        //  codes
        //  dateRange
        IQuery<IBaseBundle> search = null; //fhirClient.search().forResource(dataType);

        // TODO: Would like to be able to use the criteria builders, but it looks like they don't have one for :in with a valueset?
        // So..... I'll just construct a search URL
        //        if (templateId != null && !templateId.equals("")) {
        //            search = search.withProfile(templateId);
        //        }
        //
        //        if (codePath != null && !codePath.equals("")) {
        //            search.where(Patient.ACTIVE.)
        //        }

        // TODO: It's unclear from the FHIR documentation whether we need to use a URLEncoder.encode call on the embedded system and valueset uris here...
        StringBuilder params = new StringBuilder();

        if (templateId != null && !templateId.equals("")) {
            params.append(String.format("_profile=%s", templateId));
        }

        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context != null && context.equals("Patient") && contextValue != null) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(String.format("%s=%s", getPatientSearchParam(dataType), URLEncode((String)contextValue)));
        }

        if (codePath != null && !codePath.equals("")) {
            if (params.length() > 0) {
                params.append("&");
            }
            if (valueSet != null && !valueSet.equals("")) {
                if (terminologyProvider != null && expandValueSets) {
                    ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                    codes = terminologyProvider.expand(valueSetInfo);
                }
                else {
                    params.append(String.format("%s:in=%s", convertPathToSearchParam(dataType, codePath), URLEncode(valueSet)));
                }
            }

            if (codes != null) {
                StringBuilder codeList = new StringBuilder();
                for (Code code : codes) {
                    if (codeList.length() > 0) {
                        codeList.append(",");
                    }

                    if (code.getSystem() != null) {
                        codeList.append(URLEncode(code.getSystem()));
                        codeList.append("|");
                    }

                    codeList.append(URLEncode(code.getCode()));
                }
                params.append(String.format("%s=%s", convertPathToSearchParam(dataType, codePath), codeList.toString()));
            }
        }

        if (dateRange != null) {
            if (dateRange.getLow() != null) {
                String lowDatePath = convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath);
                if (lowDatePath == null || lowDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or low date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        lowDatePath,
                        dateRange.getLowClosed() ? "ge" : "gt",
                        dateRange.getLow().toString()));
            }

            if (dateRange.getHigh() != null) {
                String highDatePath = convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath);
                if (highDatePath == null || highDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or high date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        highDatePath,
                        dateRange.getHighClosed() ? "le" : "lt",
                        dateRange.getHigh().toString()));
            }
        }

        // TODO: Use compartment search for patient context?
        if (params.length() > 0) {
            search = fhirClient.search().byUrl(String.format("%s?%s", dataType, params.toString()));
        }
        else {
            search = fhirClient.search().byUrl(String.format("%s", dataType));
        }

        org.hl7.fhir.instance.model.Bundle results = cleanEntry(search.returnBundle(org.hl7.fhir.instance.model.Bundle.class).execute(), dataType);

        return new FhirBundleCursorHL7(fhirClient, results);
    }

    public org.hl7.fhir.instance.model.Bundle cleanEntry(org.hl7.fhir.instance.model.Bundle bundle, String dataType) {
        org.hl7.fhir.instance.model.Bundle cleanBundle = new org.hl7.fhir.instance.model.Bundle();
        for (org.hl7.fhir.instance.model.Bundle.BundleEntryComponent comp : bundle.getEntry()){
            if (comp.getResource().getResourceType().name().equals(dataType)) {
                cleanBundle.addEntry(comp);
            }
        }

        return cleanBundle;
    }

    protected DateTime toDateTime(BaseDateTimeType value, TemporalPrecisionEnum precision) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value.getValue());
        switch (precision) {
            case YEAR: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR)
            );
            case MONTH: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
            );
            case DAY: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            );
            case MINUTE: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)
            );
            case SECOND: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)
            );
            case MILLI: return new DateTime(
                    TemporalHelper.zoneToOffset(ZoneOffset.of(value.getTimeZone().getID())),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)
            );
            default: throw new IllegalArgumentException(String.format("Invalid temporal precision %s", value.getPrecision().toString()));
        }
    }

    protected DateTime toDateTime(DateTimeType value) {
        return toDateTime(value, value.getPrecision());
    }

    protected DateTime toDateTime(DateType value) {
        return toDateTime(value, value.getPrecision());
    }

    protected Time toTime(TimeType value) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_TIME;
        return new Time(OffsetTime.from(formatter.parse(value.getValue())), Precision.MILLISECOND);
    }

    protected DateTime toDateTime(InstantType value) {
        return toDateTime(value, value.getPrecision());
    }

    @Override
    protected Object fromJavaPrimitive(Object value, Object target) {
        if (target instanceof DateTimeType || target instanceof DateType) {
            DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
            return Date.from(Instant.from(dtf.parse(((DateTime) value).getDateTime().toString())));
        }
        else if (target instanceof TimeType && value instanceof Time) {
            return ((Time) value).getTime().toString();
        }
        else {
            return value;
        }
    }

    @Override
    protected Object toJavaPrimitive(Object result, Object source) {
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
    }

    @Override
    public Object createInstance(String typeName) {
        String className = resolveClassName(typeName);
        if (className.indexOf('$') >= 0) {
            className += "EnumFactory";
            return new Enumeration((EnumFactory)createInstance(resolveClass(className)));
        }

        return createInstance(resolveClass(className));
    }

    @Override
    public Class resolveType(Object value) {
        if (value == null) {
            return Object.class;
        }

        if (value instanceof Enumeration) {
            String className = ((Enumeration)value).getValue().getClass().getName();
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Could not resolve type %s", className));
            }
        }

        return value.getClass();
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

        return super.resolveProperty(target, path);
    }

    @Override
    protected String resolveClassName(String typeName) {
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
