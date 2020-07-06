package org.opencds.cqf.cql.engine.fhir.searchparam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.testng.annotations.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;


public class TestSearchParameterResolver {
    @Test
    public void testReturnsNullPathReturnsNull() {
        SearchParameterResolver resolver = new SearchParameterResolver(FhirContext.forDstu3());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", null);
        assertNull(param);
    }

    @Test
    public void testNullDataTypeReturnsNull() {
        SearchParameterResolver resolver = new SearchParameterResolver(FhirContext.forDstu3());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition(null, "code");
        assertNull(param);
    }


    @Test void testDstu3SearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(FhirContext.forDstu3());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", "id");
        assertNotNull(param);
        assertEquals("_id", param.getName());

        
        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication", RestSearchParameterTypeEnum.TOKEN);
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication", RestSearchParameterTypeEnum.REFERENCE);
        assertNotNull(param);
        assertEquals("medication", param.getName());


        param = resolver.getSearchParameterDefinition("Encounter", "period");
        assertNotNull(param);
        assertEquals("date", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "reason");
        assertNotNull(param);
        assertEquals("reason", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "subject");
        assertNotNull(param);
        assertEquals("patient", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "type");
        assertNotNull(param);
        assertEquals("type", param.getName());
    }

    @Test void testR4SearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(FhirContext.forR4());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", "id");
        assertNotNull(param);
        assertEquals("_id", param.getName());

        
        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication", RestSearchParameterTypeEnum.TOKEN);
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication", RestSearchParameterTypeEnum.REFERENCE);
        assertNotNull(param);
        assertEquals("medication", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "period");
        assertNotNull(param);
        assertEquals("date", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "reasonCode");
        assertNotNull(param);
        assertEquals("reason-code", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "subject");
        assertNotNull(param);
        assertEquals("subject", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "type");
        assertNotNull(param);
        assertEquals("type", param.getName());

        param = resolver.getSearchParameterDefinition("Observation", "code");
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("Observation", "subject");
        assertNotNull(param);
        assertEquals("subject", param.getName());
    }
}
