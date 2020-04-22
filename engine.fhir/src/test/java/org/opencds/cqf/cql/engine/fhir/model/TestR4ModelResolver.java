package org.opencds.cqf.cql.engine.fhir.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.elm_modelinfo.r1.ClassInfo;
import org.hl7.elm_modelinfo.r1.TypeInfo;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations.AbstractType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Enumerations.AgeUnits;
import org.hl7.fhir.r4.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Enumerations.DataAbsentReason;
import org.hl7.fhir.r4.model.Enumerations.DataType;
import org.hl7.fhir.r4.model.Enumerations.DefinitionResourceType;
import org.hl7.fhir.r4.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.r4.model.Enumerations.EventResourceType;
import org.hl7.fhir.r4.model.Enumerations.FHIRAllTypes;
import org.hl7.fhir.r4.model.Enumerations.FHIRDefinedType;
import org.hl7.fhir.r4.model.Enumerations.FHIRVersion;
import org.hl7.fhir.r4.model.Enumerations.KnowledgeResourceType;
import org.hl7.fhir.r4.model.Enumerations.MessageEvent;
import org.hl7.fhir.r4.model.Enumerations.NoteType;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Enumerations.RemittanceOutcome;
import org.hl7.fhir.r4.model.Enumerations.RequestResourceType;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;
import org.hl7.fhir.r4.model.Enumerations.SearchParamType;
import org.hl7.fhir.r4.model.Enumerations.SpecialValues;
import org.hl7.fhir.r4.model.Patient;
import org.opencds.cqf.cql.engine.fhir.exception.UnknownType;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.testng.annotations.Test;

public class TestR4ModelResolver {

    // Couldn't find a way to automatically get the full list of enums.
    @SuppressWarnings("serial")
    private static List<Class<?>> enums = new ArrayList<Class<?>>() {
        {
            add(AbstractType.class);
            add(AdministrativeGender.class);
            add(AgeUnits.class);
            add(BindingStrength.class);
            add(ConceptMapEquivalence.class);
            add(DataAbsentReason.class);
            add(DataType.class);
            add(DefinitionResourceType.class);
            add(DocumentReferenceStatus.class);
            add(EventResourceType.class);
            add(FHIRAllTypes.class);
            add(FHIRDefinedType.class);
            add(FHIRVersion.class);
            add(KnowledgeResourceType.class);
            add(MessageEvent.class);
            add(NoteType.class);
            add(PublicationStatus.class);
            add(RemittanceOutcome.class);
            add(RequestResourceType.class);
            add(ResourceType.class);
            add(SearchParamType.class);
            add(SpecialValues.class);
        }
    };

    @Test(expectedExceptions = UnknownType.class)
    public void resolverThrowsExceptionForUnknownType()
    {
        ModelResolver resolver = new R4FhirModelResolver();
        resolver.resolveType("ImpossibleTypeThatDoesntExistAndShouldBlowUp");
    }

    @Test
    public void resolveTypeTests() {
        ModelResolver resolver = new R4FhirModelResolver();

        for (DataType type : DataType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case BACKBONEELEMENT:
                case ELEMENT:
                case NULL:
                    continue;
                default:
            }

            resolver.resolveType(type.toCode());
        }

        for (ResourceType type : ResourceType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case DOMAINRESOURCE:
                case RESOURCE:
                case NULL:
                    continue;
                default:
            }

            resolver.resolveType(type.toCode());;
        }

        for (Class<?> enumType : enums) {
            resolver.resolveType(enumType.getSimpleName());
        }
    }

    @Test 
    public void modelInfoSpecialCaseTests() {
        ModelResolver resolver = new R4FhirModelResolver();
                
        // This tests resolution of inner classes. They aren't registered directly.
        resolver.resolveType("TestScriptRequestMethodCode");
        resolver.resolveType("FHIRDeviceStatus");


        // This tests the special case handling of "Codes".
        resolver.resolveType("ImmunizationStatusCodes");

        // These have different capitalization conventions
        resolver.resolveType("status");
        resolver.resolveType("orientationType");
        resolver.resolveType("strandType");
        resolver.resolveType("sequenceType");


        // These are oddballs requiring manual mapping. They may represent errors in the ModelInfo.
        resolver.resolveType("ConfidentialityClassification");
        resolver.resolveType("ContractResourceStatusCodes");
        resolver.resolveType("EventStatus");
        resolver.resolveType("FinancialResourceStatusCodes");
        resolver.resolveType("SampledDataDataType");
        resolver.resolveType("ClaimProcessingCodes");
        resolver.resolveType("ContractResourcePublicationStatusCodes");
        

        // These are known glitches in the ModelInfo
        resolver.resolveType("vConfidentialityClassification");
    }

    // This tests all the types that are present in the ModelInfo
     @Test
    public void resolveModelInfoTests() {
        ModelResolver resolver = new R4FhirModelResolver();
        ModelManager mm = new ModelManager();
        Model m = mm.resolveModel(new VersionedIdentifier().withId("FHIR").withVersion("4.0.0"));

        List<TypeInfo> typeInfos = m.getModelInfo().getTypeInfo();

        for (TypeInfo ti : typeInfos) {
            ClassInfo ci = (ClassInfo)ti;
            if (ci != null) {
                switch (ci.getBaseType()) {
                    // Abstract classes
                    case "FHIR.BackboneElement":
                    case "FHIR.Element": continue;
                }

                switch (ci.getName()) {
                    // TODO: HAPI Doesn't have a ResourceContainer type
                    case "ResourceContainer": continue;
                }


                // TODO: The cause of failure for this is unknown.
                // Need to figure out if it's a gap in happy,
                // or if a manual mapping is required, or what.
                switch(ci.getName()) {
                    case "ItemInstance" : continue;
                }

                resolver.resolveType(ci.getName());
            }
        }
    }

    @Test
    public void createInstanceTests() {
        ModelResolver resolver = new R4FhirModelResolver();

        for (DataType type : DataType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case BACKBONEELEMENT:
                case ELEMENT:
                case NULL:
                    continue;
                default:
            }

            Object instance = resolver.createInstance(type.toCode());

            assertNotNull(instance);
        }

        for (ResourceType type : ResourceType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case DOMAINRESOURCE:
                case RESOURCE:
                case NULL:
                    continue;
                default:
            }

            Object instance = resolver.createInstance(type.toCode());

            assertNotNull(instance);
        }

        for (Class<?> enumType : enums) {
            // For the enums we actually expect an Enumeration with a factory of the correct type to be created.
            Enumeration<?> instance = (Enumeration<?>)resolver.createInstance(enumType.getSimpleName());
            assertNotNull(instance);

            assertTrue(instance.getEnumFactory().getClass().getSimpleName().replace("EnumFactory", "").equals(enumType.getSimpleName())); 
        }

        // These are some inner classes that don't appear in the enums above
        // This list is not exhaustive. It's meant as a spot check for the resolution code.
        Object instance = resolver.createInstance("TestScriptRequestMethodCode");
        assertNotNull(instance);

        instance = resolver.createInstance("FHIRDeviceStatus");
        assertNotNull(instance);
    }


    @Test
    public void contextPathTests() {
        ModelResolver resolver = new Dstu3FhirModelResolver();

        String path = (String)resolver.getContextPath("Patient", "Patient");
        assertNotNull(path);
        assertTrue(path.equals("id"));

        path = (String)resolver.getContextPath(null, "Encounter");
        assertNull(path);

        // TODO: Consider making this an exception on the resolver because
        // if this happens it means something went wrong in the context.
        path = (String)resolver.getContextPath("Patient", null);
        assertNull(path);

        path = (String)resolver.getContextPath("Patient", "Condition");
        assertNotNull(path);
        assertTrue(path.equals("subject"));

        path = (String)resolver.getContextPath("Patient", "Appointment");
        assertNotNull(path);
        assertTrue(path.equals("participant.actor"));

        path = (String)resolver.getContextPath("Patient", "Account");
        assertNotNull(path);
        assertTrue(path.equals("subject"));

        path = (String)resolver.getContextPath("Patient", "Encounter");
        assertNotNull(path);
        assertTrue(path.equals("subject"));
    }

    @Test 
    public void resolveMissingPropertyReturnsNull() {
        ModelResolver resolver = new R4FhirModelResolver();
        
        Patient p = new Patient();

        Object result = resolver.resolvePath(p, "not-a-path");
        assertNull(result);
    }
}
