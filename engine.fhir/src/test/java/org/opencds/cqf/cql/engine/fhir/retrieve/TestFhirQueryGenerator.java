package org.opencds.cqf.cql.engine.fhir.retrieve;

import static org.testng.Assert.assertEquals;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DataRequirement;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.cql.engine.fhir.R4FhirTest;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.fhir.terminology.Dstu3FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestFhirQueryGenerator extends R4FhirTest {
    static IGenericClient CLIENT;

    FhirQueryGenerator generator;

    @BeforeClass
    public void setUpBeforeClass() {
        CLIENT= newClient();
    }

    @BeforeMethod
    public void setUp() {
        SearchParameterResolver searchParameterResolver = new SearchParameterResolver(FhirContext.forR4());
        TerminologyProvider terminologyProvider = new R4FhirTerminologyProvider(CLIENT);
        this.generator = new FhirQueryGenerator(searchParameterResolver, terminologyProvider);
    }

    @Test
    void testCodesExceedMaxCodesPerQuery() {
    }

    @Test
    void testGetFhirQueriesObservation() {
        String valueSetUrl = "http://myterm.com/fhir/ValueSet/MyValueSet";
        ValueSet valueSet = new ValueSet();
        valueSet.setId("MyValueSet");
        valueSet.setUrl(valueSetUrl);

        List<ValueSet.ValueSetExpansionContainsComponent> contains = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
        for (int i = 0; i < 3; i++) {
            ValueSet.ValueSetExpansionContainsComponent expansionContainsComponent = new ValueSet.ValueSetExpansionContainsComponent();
            expansionContainsComponent.setSystem("http://myterm.com/fhir/CodeSystem/MyCodeSystem");
            expansionContainsComponent.setCode("code" + i);
            contains.add(expansionContainsComponent);
        }

        ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
        expansion.setContains(contains);
        valueSet.setExpansion(expansion);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

//        FhirContext context = FhirContext.forR4();
//        SearchParameterMap map = new SearchParameterMap();
//        map.toNormalizedQueryString(context);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Observation");
        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
        categoryCodeFilter.setPath("category");
        org.hl7.fhir.r4.model.CanonicalType valueSetReference = new org.hl7.fhir.r4.model.CanonicalType(valueSetUrl);
        categoryCodeFilter.setValueSetElement(valueSetReference);
        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));

//        java.util.List<DataRequirement> dataRequirements = java.util.Arrays.asList(dataRequirement);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointment() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

//        java.util.List<DataRequirement> dataRequirements = java.util.Arrays.asList(dataRequirement);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }
}