package org.opencds.cqf.cql.engine.fhir.retrieve;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.cql.engine.fhir.R4FhirTest;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestR4FhirQueryGenerator extends R4FhirTest {
    static IGenericClient CLIENT;

    R4FhirQueryGenerator generator;

    @BeforeClass
    public void setUpBeforeClass() {
        CLIENT= newClient();
    }

    @BeforeMethod
    public void setUp() {
        SearchParameterResolver searchParameterResolver = new SearchParameterResolver(FhirContext.forR4());
        TerminologyProvider terminologyProvider = new R4FhirTerminologyProvider(CLIENT);
        this.generator = new R4FhirQueryGenerator(searchParameterResolver, terminologyProvider);
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

    private DataRequirement getCodeFilteredDataRequirement(String resourceType, String path, ValueSet valueSet) {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType(resourceType);
        DataRequirement.DataRequirementCodeFilterComponent categoryCodeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
        categoryCodeFilter.setPath(path);
        org.hl7.fhir.r4.model.CanonicalType valueSetReference = new org.hl7.fhir.r4.model.CanonicalType(valueSet.getUrl());
        categoryCodeFilter.setValueSetElement(valueSetReference);
        dataRequirement.setCodeFilter(java.util.Arrays.asList(categoryCodeFilter));

        return dataRequirement;
    }

    @Test
    void testCodesExceedMaxCodesPerQuery() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 8);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);
        mockFhirRead("/ValueSet/MyValueSet/$expand", valueSet);

        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);

        this.generator.setMaxCodesPerQuery(4);
        this.generator.setExpandValueSets(true);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String expectedQuery1 = "Observation?category=http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode0,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode1,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode2,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode3&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";
        String expectedQuery2 = "Observation?category=http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode4,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode5,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode6,http%3A%2F%2Fmyterm.com%2Ffhir%2FCodeSystem%2FMyCodeSystem%7Ccode7&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertNotNull(actual);
        assertEquals(actual.size(), 2);
        assertEquals(actual.get(0), expectedQuery1);
        assertEquals(actual.get(1), expectedQuery2);
    }

    @Test
    void testGetFhirQueriesObservation() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 3);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesCodeInVS() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 500);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);

        this.generator.setMaxCodesPerQuery(4);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }

//    @Test
//    void testGetFhirQueriesExpandValueSets() {
//        ValueSet valueSet = getTestValueSet("MyValueSet", 500);
//
//        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
//        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);
//
//        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
//        entry.setResource(valueSet);
//        valueSetBundle.addEntry(entry);
//
//        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);
//        mockFhirRead("/ValueSet/MyValueSet/$expand", valueSet);
//
//        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);
//
//        this.generator.setMaxCodesPerQuery(4);
//        this.generator.setExpandValueSets(true);
//        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);
//
//        String actualQuery = actual.get(0);
//        String expectedQuery = "Observation?category:in=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet&patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";
//
//        assertEquals(actualQuery, expectedQuery);
//    }

    @Test
    void testGetFhirQueriesAppointment() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointmentWithDates() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient%2F%7B%7Bcontext.patientId%7D%7D";

        assertEquals(actualQuery, expectedQuery);
    }
}