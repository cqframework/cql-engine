package org.opencds.cqf.cql.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.CqlTranslatorException.ErrorSeverity;
import org.cqframework.cql.cql2elm.LibraryBuilder.SignatureLevel;
import org.cqframework.cql.cql2elm.model.TranslatedLibrary;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.elm.r1.ObjectFactory;
import org.junit.Test;

public class CqlEngineTests {

    private LibraryManager toLibraryManager(Map<org.hl7.elm.r1.VersionedIdentifier, String> libraryText) throws IOException, JAXBException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        libraryManager.getLibrarySourceLoader().registerProvider(new InMemoryLibrarySourceProvider(libraryText));
        return libraryManager;
    }

    private Library toLibrary(String text) throws IOException, JAXBException  {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        return this.toLibrary(text, modelManager, libraryManager);
    }

    private Library toLibrary(String text, ModelManager modelManager, LibraryManager libraryManager) throws IOException, JAXBException {
        CqlTranslator translator = CqlTranslator.fromText(text, modelManager, libraryManager);
        return this.readXml(translator.toXml());
    }

    private Library readXml(String xml) throws IOException, JAXBException {
        return CqlLibraryReader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private Map<VersionedIdentifier, Set<String>> toExpressionMap(VersionedIdentifier libraryIdentifier, String... expressions) {
        Map<VersionedIdentifier, Set<String>> expressionMap = new HashMap<>();
        expressionMap.put(libraryIdentifier, new HashSet<String>(Arrays.asList(expressions)));
        return expressionMap;
    }

    private Map<VersionedIdentifier, Set<String>> mergeExpressionMaps(Map<VersionedIdentifier, Set<String>>... maps) {
        Map<VersionedIdentifier, Set<String>> mergedMaps = new HashMap<VersionedIdentifier, Set<String>>();
        for (Map<VersionedIdentifier, Set<String>> map : maps) {
           mergedMaps.putAll(map);
        }

        return mergedMaps;
    }

    private org.hl7.elm.r1.VersionedIdentifier toElmIdentifier(String name, String version) {
        return new org.hl7.elm.r1.VersionedIdentifier().withId(name).withVersion(version);
    }

    private VersionedIdentifier toExecutionIdentifier(String name, String version) {
        return new VersionedIdentifier().withId(name).withVersion(version);
    }

    private Map<String, String> getLibrariesAsXML(LibraryManager libraryManager) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, TranslatedLibrary> entry : libraryManager.getTranslatedLibraries().entrySet()) {
            result.put(entry.getKey(), toXml(entry.getValue().getLibrary()));
        }
        return result;
    }

    private String toXml(org.hl7.elm.r1.Library library) {
        try {
            return convertToXml(library);
        }
        catch (JAXBException e) {
            throw new IllegalArgumentException("Could not convert library to XML.", e);
        }
    }

    public String convertToXml(org.hl7.elm.r1.Library library) throws JAXBException {
        Marshaller marshaller = getJaxbContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(new ObjectFactory().createLibrary(library), writer);
        return writer.getBuffer().toString();
    }

    public static JAXBContext getJaxbContext() throws JAXBException {
        return JAXBContext.newInstance(org.hl7.elm.r1.Library.class, org.hl7.cql_annotations.r1.Annotation.class);
    }


    
    @Test(expected = IllegalArgumentException.class)
    public void test_nullLibraryLoader_throwsException() {
        CqlEngine engine = new CqlEngine(null);
    }

    @Test
    public void test_simpleLibrary_returnsResult() throws IOException, JAXBException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate(library.getIdentifier());

        assertNotNull(result);
        assertEquals(result.libraryResults.size(), 1);

        Object expResult = result.forLibrary(library.getIdentifier()).forExpression("X");

        assertThat(expResult, is(10));
    }

    //@Test(expected = IllegalArgumentException.class)
    public void test_dataLibrary_noProvider_throwsException() throws IOException, JAXBException {
        Library library = this.toLibrary("library Test version '1.0.0'\nusing FHIR version '3.0.0'\ndefine X:\n5+5");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        engine.evaluate(library.getIdentifier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_terminologyLibrary_noProvider_throwsException() throws IOException, JAXBException {
        Library library = this.toLibrary("library Test version '1.0.0'\nvalueset valueset \"Dummy Value Set\": \"http://localhost\"\ndefine X:\n5+5");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        engine.evaluate(library.getIdentifier());
    }

    @Test
    public void test_twoExpressions_byLibrary_allReturned() throws IOException, JAXBException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate(library.getIdentifier());

        assertNotNull(result);
        assertEquals(1, result.libraryResults.size());
        assertEquals(2, result.forLibrary(library.getIdentifier()).expressionResults.size());

        Object expResult = result.forLibrary(library.getIdentifier()).forExpression("X");
        assertThat(expResult, is(10));

        expResult = result.forLibrary(library.getIdentifier()).forExpression("Y");
        assertThat(expResult, is(4));
    }

    @Test
    public void test_twoExpressions_oneRequested_oneReturned() throws IOException, JAXBException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate(this.toExpressionMap(library.getIdentifier(), "Y"));

        assertNotNull(result);
        assertEquals(1, result.libraryResults.size());
        assertEquals(1, result.forLibrary(library.getIdentifier()).expressionResults.size());
        
        Object expResult = result.forLibrary(library.getIdentifier()).forExpression("Y");
        assertThat(expResult, is(4));
    }

    @Test
    public void test_twoLibraries_expressionsForEach() throws IOException, JAXBException {

        Map<org.hl7.elm.r1.VersionedIdentifier, String> libraries = new HashMap<>();
        libraries.put(this.toElmIdentifier("Common", "1.0.0"), 
            "library Common version '1.0.0'\ndefine Z:\n5+5\n");
        libraries.put(toElmIdentifier("Test", "1.0.0"),
            "library Test version '1.0.0'\ninclude Common version '1.0.0' named \"Common\"\ndefine X:\n5+5\ndefine Y: 2 + 2\ndefine W: \"Common\".Z + 5");


        LibraryManager libraryManager = this.toLibraryManager(libraries);
        List<CqlTranslatorException> errors = new ArrayList<>();
        List<Library> executableLibraries = new ArrayList<>();
        for (org.hl7.elm.r1.VersionedIdentifier id : libraries.keySet()) {
            TranslatedLibrary translated = libraryManager.resolveLibrary(id, ErrorSeverity.Error, SignatureLevel.All, new CqlTranslator.Options[0], errors);
            String xml = this.convertToXml(translated.getLibrary());
            executableLibraries.add(this.readXml(xml));
        }

        Map<VersionedIdentifier, Set<String>> expressions = this.mergeExpressionMaps(
            this.toExpressionMap(toExecutionIdentifier("Common", "1.0.0"), "Z"),
            this.toExpressionMap(toExecutionIdentifier("Test", "1.0.0"), "X", "Y", "W")
        );

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(executableLibraries);

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate(expressions);

        assertNotNull(result);
        assertEquals(2, result.libraryResults.size());
        assertEquals(1, result.forLibrary(executableLibraries.get(0).getIdentifier()).expressionResults.size());
        assertThat(result.forLibrary(executableLibraries.get(0).getIdentifier()).forExpression("Z"), is(10));
        
        assertEquals(3, result.forLibrary(executableLibraries.get(1).getIdentifier()).expressionResults.size());
        assertThat(result.forLibrary(executableLibraries.get(1).getIdentifier()).forExpression("X"), is(10));
        assertThat(result.forLibrary(executableLibraries.get(1).getIdentifier()).forExpression("Y"), is(4));
        assertThat(result.forLibrary(executableLibraries.get(1).getIdentifier()).forExpression("W"), is(15));
    }
}