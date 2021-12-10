package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.R4FhirTest;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TestR4FhirQueryGenerator extends R4FhirTest {
    static IGenericClient CLIENT;

    R4FhirQueryGenerator generator;
    private Context engineContext;

    @BeforeClass
    public void setUpBeforeClass() {
        CLIENT= newClient();
    }

    @BeforeMethod
    public void setUp() {
        SearchParameterResolver searchParameterResolver = new SearchParameterResolver(FhirContext.forR4());
        TerminologyProvider terminologyProvider = new R4FhirTerminologyProvider(CLIENT);
        this.generator = new R4FhirQueryGenerator(searchParameterResolver, terminologyProvider);
        OffsetDateTime evaluationDateTime = OffsetDateTime.of(2018, 11, 19, 9, 0, 0, 000, ZoneOffset.ofHours(-7));
        this.engineContext = new Context(new Library().withIdentifier(new VersionedIdentifier().withId("Test")), evaluationDateTime.toZonedDateTime());
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
    void testGetFhirQueriesObservation() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 3);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http://myterm.com/fhir/ValueSet/MyValueSet&patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesCodeInValueSet() {
        ValueSet valueSet = getTestValueSet("MyValueSet", 500);

        org.hl7.fhir.r4.model.Bundle valueSetBundle = new org.hl7.fhir.r4.model.Bundle();
        valueSetBundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setResource(valueSet);
        valueSetBundle.addEntry(entry);

        mockFhirRead("/ValueSet?url=http%3A%2F%2Fmyterm.com%2Ffhir%2FValueSet%2FMyValueSet", valueSetBundle);

        DataRequirement dataRequirement = getCodeFilteredDataRequirement("Observation", "category", valueSet);

        this.generator.setMaxCodesPerQuery(4);
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Observation?category:in=http://myterm.com/fhir/ValueSet/MyValueSet&patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointment() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

        String actualQuery = actual.get(0);
        String expectedQuery = "Appointment?patient=Patient/{{context.patientId}}";

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesAppointmentWithDate() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Appointment");
        DataRequirement.DataRequirementDateFilterComponent dateFilterComponent = new DataRequirement.DataRequirementDateFilterComponent();
        dateFilterComponent.setSearchParam("date");

        int offsetHours = java.util.TimeZone.getDefault().getRawOffset() / 3600000;
        String offsetSign = offsetHours < 0 ? "-" : "+";
        int offsetAbs = Math.abs(offsetHours);
        String offsetStringPadded = StringUtils.leftPad(String.valueOf(offsetAbs), 2, "0");
        String dateTimeString = String.format("2021-12-01T00:00:00.000%s%s:00", offsetSign, offsetStringPadded);
        dateFilterComponent.setValue(new DateTimeType(dateTimeString));
        dataRequirement.setDateFilter(Collections.singletonList(dateFilterComponent));

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

        String actualQuery = actual.get(0);
        String expectedQuery = String.format("Appointment?date=ge%s&date=le%s&patient=Patient/{{context.patientId}}", dateTimeString, dateTimeString);

        assertEquals(actualQuery, expectedQuery);
    }

    @Test
    void testGetFhirQueriesObservationWithDuration() {
        DataRequirement dataRequirement = new DataRequirement();
        dataRequirement.setType("Observation");
        DataRequirement.DataRequirementDateFilterComponent dateFilterComponent = new DataRequirement.DataRequirementDateFilterComponent();
        dateFilterComponent.setSearchParam("date");
        Duration duration = new Duration();
        duration.setValue(2).setCode("d").setUnit("days");
        dateFilterComponent.setValue(duration);
        dataRequirement.setDateFilter(Collections.singletonList(dateFilterComponent));

        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

        int offsetHours = java.util.TimeZone.getDefault().getRawOffset() / 3600000;
        OffsetDateTime evaluationOffsetDateTime = this.engineContext.getEvaluationOffsetDateTime();
        OffsetDateTime evaluationDateTimeAsLocal = OffsetDateTime.ofInstant(evaluationOffsetDateTime.toInstant(),
            java.util.TimeZone.getDefault().toZoneId());
        OffsetDateTime expectedRangeStartDateTime = evaluationDateTimeAsLocal.minusDays(2);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx");

        String actualQuery = actual.get(0);
        String expectedQuery = String.format("Observation?date=ge%s&date=le%s&patient=Patient/{{context.patientId}}", fmt.format(expectedRangeStartDateTime), fmt.format(evaluationDateTimeAsLocal));

        assertEquals(actualQuery, expectedQuery);
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
        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);

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
//        this.generator.setMaxCodesPerQuery(400);
//        this.generator.setMaxUriLength(20);
//        this.generator.setExpandValueSets(true);
//        java.util.List<String> actual = this.generator.generateFhirQueries(dataRequirement, this.engineContext, null);
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