package org.opencds.cqf.cql.engine.fhir.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu2.model.Enumerations.AgeUnits;
import org.hl7.fhir.dstu2.model.Enumerations.BindingStrength;
import org.hl7.fhir.dstu2.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.dstu2.model.Enumerations.DataAbsentReason;
import org.hl7.fhir.dstu2.model.Enumerations.DataType;
import org.hl7.fhir.dstu2.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu2.model.Enumerations.FHIRDefinedType;
import org.hl7.fhir.dstu2.model.Enumerations.MessageEvent;
import org.hl7.fhir.dstu2.model.Enumerations.NoteType;
import org.hl7.fhir.dstu2.model.Enumerations.RemittanceOutcome;
import org.hl7.fhir.dstu2.model.Enumerations.ResourceType;
import org.hl7.fhir.dstu2.model.Enumerations.SearchParamType;
import org.hl7.fhir.dstu2.model.Enumerations.SpecialValues;
import org.hl7.fhir.dstu2.model.Patient;
import org.opencds.cqf.cql.engine.fhir.exception.UnknownType;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.testng.annotations.Test;

public class TestDstu2ModelResolver {

    // Couldn't find a way to automatically get the full list of enums.
    @SuppressWarnings("serial")
    private static List<Class<?>> enums = new ArrayList<Class<?>>() {
        {
            add(AdministrativeGender.class);
            add(AgeUnits.class);
            add(BindingStrength.class);
            add(ConceptMapEquivalence.class);
            add(DataAbsentReason.class);
            add(DataType.class);
            add(DocumentReferenceStatus.class);
            add(FHIRDefinedType.class);
            add(MessageEvent.class);
            add(NoteType.class);
            add(RemittanceOutcome.class);
            add(ResourceType.class);
            add(SearchParamType.class);
            add(SpecialValues.class);
        }
    };

    @Test(expectedExceptions = UnknownType.class)
    public void resolverThrowsExceptionForUnknownType()
    {
        ModelResolver resolver = new Dstu2FhirModelResolver();
        resolver.resolveType("ImpossibleTypeThatDoesntExistAndShouldBlowUp");
    }

    @Test
    public void resolveTypeTests() {
        ModelResolver resolver = new Dstu2FhirModelResolver();

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

            resolver.resolveType(type.toCode());
        }

        for (Class<?> enumType : enums) {
            resolver.resolveType(enumType.getSimpleName());
        }
    }

    @Test
    public void createInstanceTests() {
        ModelResolver resolver = new Dstu2FhirModelResolver();

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

            Field enumFactory;
            try {
                enumFactory = instance.getClass().getDeclaredField("myEnumFactory");
                enumFactory.setAccessible(true);
                EnumFactory<?> factory = (EnumFactory<?>)enumFactory.get(instance);

                assertTrue(factory.getClass().getSimpleName().replace("EnumFactory", "").equals(enumType.getSimpleName())); 
            } 
            catch(Exception e){
                throw new AssertionError("error getting factory type. " + e.getMessage());
            }
        }
    }


    @Test
    public void contextPathTests() {
        ModelResolver resolver = new Dstu2FhirModelResolver();

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
        assertTrue(path.equals("patient"));

        path = (String)resolver.getContextPath("Patient", "Appointment");
        assertNotNull(path);
        assertTrue(path.equals("participant.actor"));

        path = (String)resolver.getContextPath("Patient", "Observation");
        assertNotNull(path);
        assertTrue(path.equals("subject"));

        path = (String)resolver.getContextPath("Patient", "Encounter");
        assertNotNull(path);
        assertTrue(path.equals("patient"));
    }

    @Test 
    public void resolveMissingPropertyReturnsNull() {
        ModelResolver resolver = new Dstu2FhirModelResolver();
        
        Patient p = new Patient();

        Object result = resolver.resolvePath(p, "not-a-path");
        assertNull(result);
    }
}
