package org.opencds.cqf.cql.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.junit.Test;

public class CqlEngineTests {
    private Library toLibrary(String text) throws IOException, JAXBException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        CqlTranslator translator = CqlTranslator.fromText(text, modelManager, libraryManager);
        return CqlLibraryReader.read(new ByteArrayInputStream(translator.toXml().getBytes(StandardCharsets.UTF_8)));
    }

    private Map<VersionedIdentifier, Set<String>> toExpressionMap(Library library, String... expressions) {
        Map<VersionedIdentifier, Set<String>> expressionMap = new HashMap<>();
        expressionMap.put(library.getIdentifier(), new HashSet<String>(Arrays.asList(expressions)));
        return expressionMap;
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

    @Test(expected = IllegalArgumentException.class)
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

        EvaluationResult result = engine.evaluate(this.toExpressionMap(library, "Y"));

        assertNotNull(result);
        assertEquals(1, result.libraryResults.size());
        assertEquals(1, result.forLibrary(library.getIdentifier()).expressionResults.size());
        
        Object expResult = result.forLibrary(library.getIdentifier()).forExpression("Y");
        assertThat(expResult, is(4));
    }
}