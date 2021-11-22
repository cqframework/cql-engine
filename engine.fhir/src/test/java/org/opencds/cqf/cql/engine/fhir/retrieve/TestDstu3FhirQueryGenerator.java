package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DataRequirement;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.opencds.cqf.cql.engine.fhir.Dstu3FhirTest;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.fhir.terminology.Dstu3FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TestDstu3FhirQueryGenerator extends Dstu3FhirTest {
    static IGenericClient CLIENT;

    Dstu3FhirQueryGenerator generator;

    @BeforeClass
    public void setUpBeforeClass() {
        CLIENT= newClient();
    }

    @BeforeMethod
    public void setUp() {
        SearchParameterResolver searchParameterResolver = new SearchParameterResolver(FhirContext.forDstu3());
        TerminologyProvider terminologyProvider = new Dstu3FhirTerminologyProvider(CLIENT);
        this.generator = new Dstu3FhirQueryGenerator(searchParameterResolver, terminologyProvider);
    }

    private ValueSet getTestValueSet(String id, int numberOfCodesToInclude) {
        String valueSetUrl = String.format("http://myterm.com/fhir/ValueSet/%s", id);
        ValueSet valueSet = new ValueSet();
        valueSet.setId("MyValueSet");
        valueSet.setUrl(valueSetUrl);

        List<ValueSet.ValueSetExpansionContainsComponent> contains = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
        for (int i = 0; i < numberOfCodesToInclude; i++) {
            ValueSet.ValueSetExpansionContainsComponent expansionContainsComponent = new ValueSet.ValueSetExpansionContainsComponent();
            expansionContainsComponent.setSystem(String.format("http://myterm.com/fhir/CodeSystem/%s", id));
            expansionContainsComponent.setCode("code" + i);
            contains.add(expansionContainsComponent);
        }

        ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
        expansion.setContains(contains);
        valueSet.setExpansion(expansion);

        return valueSet;
    }

    private DataRequirement getCodeFilteredViaValueSetDataRequirement(String resourceType, String path, ValueSet valueSet) {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType(resourceType);
        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
        categoryCodeFilter.setPath(path);
        org.hl7.fhir.dstu3.model.Reference valueSetReference = new org.hl7.fhir.dstu3.model.Reference(valueSet.getUrl());
        categoryCodeFilter.setValueSet(valueSetReference);
        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));

        return dataRequirement;
    }

//    private DataRequirement getCodeFilteredViaCodeableConceptDataRequirement(String resourceType, String path, ValueSet valueSet) {
//        DataRequirement dataRequirement = new DataRequirement();
//        dataRequirement.setType(resourceType);
//        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
//        categoryCodeFilter.setPath(path);
//        org.hl7.fhir.dstu3.model.Reference valueSetReference = new org.hl7.fhir.dstu3.model.Reference(valueSet.getUrl());
//        categoryCodeFilter.setValueSet(valueSetReference);
//        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));
//
//        return dataRequirement;
//    }
//
//    private DataRequirement getCodeFilteredViaCodingDataRequirement(String resourceType, String path, ValueSet valueSet) {
//        DataRequirement dataRequirement = new DataRequirement();
//        dataRequirement.setType(resourceType);
//        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
//        categoryCodeFilter.setPath(path);
//        org.hl7.fhir.dstu3.model.Reference valueSetReference = new org.hl7.fhir.dstu3.model.Reference(valueSet.getUrl());
//        categoryCodeFilter.setValueSet(valueSetReference);
//        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));
//
//        return dataRequirement;
//    }
//
//    private DataRequirement getCodeFilteredViaCodeDataRequirement(String resourceType, String path, ValueSet valueSet) {
//        DataRequirement dataRequirement = new DataRequirement();
//        dataRequirement.setType(resourceType);
//        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
//        categoryCodeFilter.setPath(path);
//        org.hl7.fhir.dstu3.model.Reference valueSetReference = new org.hl7.fhir.dstu3.model.Reference(valueSet.getUrl());
//        categoryCodeFilter.setValueSet(valueSetReference);
//        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));
//
//        return dataRequirement;
//    }

    @Test
    void testGetFhirQueriesObservation() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 3);

        org.hl7.fhir.dstu3.model.Bundle valueSetBundle = new org.hl7.fhir.dstu3.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredViaValueSetDataRequirement("Observation", "category", valueSet);

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http://myterm.com/fhir/ValueSet/MyValueSet&patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesCodeInValueSet() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 500);

        org.hl7.fhir.dstu3.model.Bundle valueSetBundle = new org.hl7.fhir.dstu3.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredViaValueSetDataRequirement("Observation", "category", valueSet);

        this.generator.setMaxCodesPerQuery(4);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http://myterm.com/fhir/ValueSet/MyValueSet&patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointment() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointmentWithDates() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testCodesExceedMaxCodesPerQuery() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 8);

        org.hl7.fhir.dstu3.model.Bundle valueSetBundle = new org.hl7.fhir.dstu3.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);
        mockFhirRead("/ValueSet/MyValueSet/$expand", valueSet);

        DataRequirement dataRequirement = getCodeFilteredViaValueSetDataRequirement("Observation", "category", valueSet);

        this.generator.setMaxCodesPerQuery(4);
        this.generator.setExpandValueSets(true);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String expectedQuery1 = "Observation?category=http://myterm.com/fhir/CodeSystem/MyValueSet|code0,http://myterm.com/fhir/CodeSystem/MyValueSet|code1,http://myterm.com/fhir/CodeSystem/MyValueSet|code2,http://myterm.com/fhir/CodeSystem/MyValueSet|code3&patient=Patient/{{context.patientId}}";
        String expectedQuery2 = "Observation?category=http://myterm.com/fhir/CodeSystem/MyValueSet|code4,http://myterm.com/fhir/CodeSystem/MyValueSet|code5,http://myterm.com/fhir/CodeSystem/MyValueSet|code6,http://myterm.com/fhir/CodeSystem/MyValueSet|code7&patient=Patient/{{context.patientId}}";

        assertNotNull(actual);
        assertEquals(actual.size(), 2);
        assertEquals(actual.get(0), expectedQuery1);
        assertEquals(actual.get(1), expectedQuery2);
    }

//    @Test
//    void testCodesExceedMaxUriLength() {
//        ValueSet valueSet = getTestValueSet("MyValueSet", 200);
//
//        org.hl7.fhir.dstu3.model.Bundle valueSetBundle = new org.hl7.fhir.dstu3.model.Bundle();
//        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);
//
//        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
//        entry.setResource(valueSet);
//        valueSetBundle.addEntry(entry);
//
//        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);
//        mockFhirRead("/ValueSet/MyValueSet/$expand", valueSet);
//
//        DataRequirement dataRequirement = getCodeFilteredViaValueSetDataRequirement("Observation", "category", valueSet);
//
//        this.generator.setMaxCodesPerQuery(400);
//        this.generator.setMaxUriLength(20);
//        this.generator.setExpandValueSets(true);
//        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);
//
//        String expectedQuery1 = "Observation?category=http://myterm.com/fhir/CodeSystem/MyValueSet|code0,http://myterm.com/fhir/CodeSystem/MyValueSet|code1,http://myterm.com/fhir/CodeSystem/MyValueSet|code2,http://myterm.com/fhir/CodeSystem/MyValueSet|code3&patient=Patient/{{context.patientId}}";
//        String expectedQuery2 = "Observation?category=http://myterm.com/fhir/CodeSystem/MyValueSet|code4,http://myterm.com/fhir/CodeSystem/MyValueSet|code5,http://myterm.com/fhir/CodeSystem/MyValueSet|code6,http://myterm.com/fhir/CodeSystem/MyValueSet|code7&patient=Patient/{{context.patientId}}";
//
//        assertNotNull(actual);
//        assertEquals(actual.size(), 2);
//        assertEquals(actual.get(0), expectedQuery1);
//        assertEquals(actual.get(1), expectedQuery2);
//    }
}