package org.opencds.cqf.cql.model;

import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Enumerations;
//import org.hl7.fhir.dstu2.model.ResourceType;
import org.testng.annotations.Test;

import ca.uhn.fhir.model.dstu2.resource.Patient;

import static ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum.DOMAINRESOURCE;
import static ca.uhn.fhir.model.dstu2.valueset.StructureDefinitionKindEnum.RESOURCE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//import org.hl7.fhir.instance.model.EnumFactory;
//import org.hl7.fhir.instance.model.Enumeration;
//import org.hl7.fhir.instance.model.Enumerations.*;
import org.opencds.cqf.cql.exception.UnknownType;

public class TestDstu2ModelResolver {

    // Couldn't find a way to automatically get the full list of enums.
    private static List<Class<?>> enums = new ArrayList<Class<?>>() {
        {
            add(Enumerations.AdministrativeGender.class);
            add(Enumerations.AgeUnits.class);
            add(Enumerations.BindingStrength.class);
            add(Enumerations.ConceptMapEquivalence.class);
            add(Enumerations.DataAbsentReason.class);
            add(Enumerations.DataType.class);
            add(Enumerations.DocumentReferenceStatus.class);
            add(Enumerations.FHIRDefinedType.class);
            add(Enumerations.MessageEvent.class);
            add(Enumerations.NoteType.class);
            add(Enumerations.RemittanceOutcome.class);
            add(Enumerations.ResourceType.class);
            add(Enumerations.SearchParamType.class);
            add(Enumerations.SpecialValues.class);
        }
    };

    @Test(expectedExceptions = UnknownType.class)
    public void resolverThrowsExecptionForUnknownType()
    {
        ModelResolver resolver = new Dstu2FhirModelResolver();
        resolver.resolveType("ImpossibleTypeThatDoesntExistAndShouldBlowUp");
    }

    @Test
    public void resolveTypeTests() {
        ModelResolver resolver = new Dstu2FhirModelResolver();

        for (Enumerations.DataType type : Enumerations.DataType.values()) {
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

        for (Enumerations.ResourceType type : Enumerations.ResourceType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case DOMAINRESOURCE:
                case RESOURCE:
                //case :
                    continue;
                default:
            }

            resolver.resolveType(type);
        }

        for (Class<?> enumType : enums) {
            resolver.resolveType(enumType.getSimpleName());
        }
    }

    @Test
    public void createInstanceTests() {
        ModelResolver resolver = new Dstu2FhirModelResolver();

        for (Enumerations.DataType type : Enumerations.DataType.values()) {
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

        for (Enumerations.ResourceType type : Enumerations.ResourceType.values()) {
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

        Object result = resolver.resolvePath(p, "notapath");
        assertNull(result);
    }
}
