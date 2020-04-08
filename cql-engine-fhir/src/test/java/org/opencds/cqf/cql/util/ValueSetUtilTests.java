package org.opencds.cqf.cql.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.runtime.Code;
import org.testng.annotations.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class ValueSetUtilTests {

    private static final String R4_PATH = "r4/TestValueSet.json";
    private static final String DSTU3_PATH = "dstu3/TestValueSet.json";

    private static final FhirContext DSTU3_CONTEXT  = FhirContext.forDstu3();
    private static final FhirContext R4_CONTEXT = FhirContext.forR4();

    private IBaseResource loadValueSet(FhirContext fhirContext, String path) {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
        IParser parser = path.endsWith("json") ? fhirContext.newJsonParser() : fhirContext.newXmlParser();
        IBaseResource resource = parser.parseResource(stream);

        if (resource == null) {
            throw new IllegalArgumentException(String.format("Unable to read a resource from %s.", path));
        }

        Class<?> valueSetClass = fhirContext.getResourceDefinition("ValueSet").getImplementingClass();
        if (!valueSetClass.equals(resource.getClass())) {
            throw new IllegalArgumentException(String.format("Resource at %s is not FHIR %s ValueSet", path, fhirContext.getVersion().getVersion().getFhirVersionString()));
        }

        return resource;
    }

    private void testGetCompose(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        IBase compose = ValueSetUtil.getCompose(fhirContext, valueSet);
        assertNotNull(compose);
    }

    private void testGetInclude(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        List<IBase> includes = ValueSetUtil.getIncludes(fhirContext, valueSet);
        assertNotNull(includes);
        assertEquals(2, includes.size());
    }

    private void testGetIncludeConcepts(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        List<IBase> concepts = ValueSetUtil.getIncludeConcepts(fhirContext, valueSet);
        assertNotNull(concepts);
        assertEquals(3, concepts.size());
    }

    private void testGetExpansion(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        IBase expansion = ValueSetUtil.getExpansion(fhirContext, valueSet);
        assertNotNull(expansion);
    }

    private void testGetContains(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        List<IBase> contains = ValueSetUtil.getContains(fhirContext, valueSet);
        assertNotNull(contains);
        assertEquals(3, contains.size());
    }

    private void testGetCodesInExpansion(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        Iterable<Code> codes = ValueSetUtil.getCodesInExpansion(fhirContext, valueSet);
        assertNotNull(codes);

        List<Code> codeList = new ArrayList<>();
        for (Code c : codes) {
            codeList.add(c);
        }

        assertEquals(3, codeList.size());

        Code first = codeList.get(0);

        assertEquals("000", first.getCode());
        assertEquals("http://cql.alphora.com/unit-test", first.getSystem());
        assertEquals("000 Code", first.getDisplay());
        assertEquals("2018-03", first.getVersion());
    }

    private void testGetCodesInCompose(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        Iterable<Code> codes = ValueSetUtil.getCodesInCompose(fhirContext, valueSet);
        assertNotNull(codes);

        List<Code> codeList = new ArrayList<>();
        for (Code c : codes) {
            codeList.add(c);
        }

        assertEquals(3, codeList.size());

        Code first = codeList.get(0);

        assertEquals("000", first.getCode());
        assertEquals("http://cql.alphora.com/unit-test", first.getSystem());
        assertEquals("000 Code", first.getDisplay());
        assertEquals("2018-03", first.getVersion());
    }

    private void testGetUrl(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        String url = ValueSetUtil.getUrl(fhirContext, valueSet);
        assertNotNull(url);
    }


    private void testGetId(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        String id = ValueSetUtil.getId(fhirContext, valueSet);
        assertNotNull(id);
    }

    private void testGetResourceType(FhirContext fhirContext, String path) {
        IBaseResource valueSet = this.loadValueSet(fhirContext, path);
        String type = ValueSetUtil.getResourceType(fhirContext, valueSet);
        assertNotNull(type);
    }

    @Test
    public void testGetCompose() {
        this.testGetCompose(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetCompose(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetInclude() {
        this.testGetInclude(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetInclude(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetIncludeConcepts() {
        this.testGetIncludeConcepts(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetIncludeConcepts(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetExpansion() {
        this.testGetExpansion(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetExpansion(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetContains() {
        this.testGetContains(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetContains(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetCodesInExpansion() {
        this.testGetCodesInExpansion(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetCodesInExpansion(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetCodesInCompose() {
        this.testGetCodesInCompose(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetCodesInCompose(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetUrl() {
        this.testGetUrl(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetUrl(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetId() {
        this.testGetId(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetId(R4_CONTEXT, R4_PATH);
    }

    @Test
    public void testGetResourceType() {
        this.testGetResourceType(DSTU3_CONTEXT, DSTU3_PATH);
        this.testGetResourceType(R4_CONTEXT, R4_PATH);
    }


}