package org.opencds.cqf.cql.engine.fhir.searchparam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

public class SearchParameterResolverTests {

    FhirContext r4FhirContext;
    FhirContext dstu3FhirContext;
    CapabilityStatementIndexer capabilityStatementIndexer;
    IParser jsonParser;

    @BeforeClass
    public void setup() {
        this.r4FhirContext = FhirContext.forR4();
        this.dstu3FhirContext = FhirContext.forDstu3();
        this.jsonParser = this.r4FhirContext.newJsonParser();
        this.capabilityStatementIndexer = new CapabilityStatementIndexer(r4FhirContext);

    }

    protected CapabilityStatementIndex getIndex(String resourceName) {
        CapabilityStatement capabilityStatement = this.jsonParser.parseResource(CapabilityStatement.class,
                CapabilityStatementIndexerTests.class.getResourceAsStream(resourceName));

        return this.capabilityStatementIndexer.index(capabilityStatement);
    }


    @Test
    public void testReturnsNullPathReturnsNull() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.dstu3FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", null);
        assertNull(param);
    }

    @Test
    public void testNullDataTypeReturnsNull() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.dstu3FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition(null, "code");
        assertNull(param);
    }

    @Test
    void testDstu3SearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.dstu3FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", "id");
        assertNotNull(param);
        assertEquals("_id", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication",
                RestSearchParameterTypeEnum.TOKEN);
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication",
                RestSearchParameterTypeEnum.REFERENCE);
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

    @Test
    void testDstu3DateSearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.dstu3FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("ProcedureRequest", "authoredOn",
                RestSearchParameterTypeEnum.DATE);
        assertNotNull(param);
        assertEquals("authored", param.getName());
    }

    @Test
    void testR4SearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.r4FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", "id");
        assertNotNull(param);
        assertEquals("_id", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication",
                RestSearchParameterTypeEnum.TOKEN);
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("MedicationAdministration", "medication",
                RestSearchParameterTypeEnum.REFERENCE);
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
        assertEquals("patient", param.getName());

        param = resolver.getSearchParameterDefinition("Encounter", "type");
        assertNotNull(param);
        assertEquals("type", param.getName());

        param = resolver.getSearchParameterDefinition("Observation", "code");
        assertNotNull(param);
        assertEquals("code", param.getName());

        param = resolver.getSearchParameterDefinition("Observation", "subject");
        assertNotNull(param);
        assertEquals("patient", param.getName());
    }

    @Test
    void testR4DateSearchParams() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.r4FhirContext);

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("ServiceRequest", "authoredOn",
                RestSearchParameterTypeEnum.DATE);
        assertNotNull(param);
        assertEquals("authored", param.getName());
    }

    @Test
    void testR4ReferenceParameter() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.r4FhirContext);
        Pair<String, IQueryParameterType> actual = resolver.createSearchParameter("Patient", "Observation", "subject",
                "123");

        assertEquals("Patient/123", actual.getRight().getValueAsQueryToken(this.r4FhirContext));
    }

    @Test
    void testR4TokenParameter() {
        SearchParameterResolver resolver = new SearchParameterResolver(this.r4FhirContext);
        Pair<String, IQueryParameterType> actual = resolver.createSearchParameter("Patient", "Observation", "code",
                "123");

        assertEquals("123", actual.getRight().getValueAsQueryToken(this.r4FhirContext));
    }


    @Test
    void testCapabilityIndexParameterOverride() {

        SearchParameterResolver resolver = new SearchParameterResolver(this.r4FhirContext);
        RuntimeSearchParam param = resolver.getSearchParameterDefinition("MedicationRequest", "subject");
        assertNotNull(param);
        assertEquals(param.getName(), "patient");

        CapabilityStatementIndex index = this.getIndex("sample-dummy-capability-statement.json");
        param = resolver.getSearchParameterDefinition("MedicationRequest", "subject", index);
        assertNotNull(param);
        assertEquals(param.getName(), "subject");
    }
}
