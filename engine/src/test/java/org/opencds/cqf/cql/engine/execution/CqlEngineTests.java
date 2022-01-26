package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.model.TranslatedLibrary;
import org.cqframework.cql.elm.execution.Library;
import org.testng.annotations.Test;

public class CqlEngineTests extends TranslatingTestBase {


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_nullLibraryLoader_throwsException() {
        new CqlEngine(null);
    }

    @Test
    public void test_simpleLibrary_returnsResult() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate("Test");


        Object expResult = result.forExpression("X");

        assertThat(expResult, is(10));
    }

    @Test
    public void test_simpleLibraryWithParam_returnsParamValue() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\nparameter IntValue Integer\ndefine X:\nIntValue");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("IntValue", 10);

        EvaluationResult result = engine.evaluate("Test", parameters);


        Object expResult = result.forExpression("X");

        assertThat(expResult, is(10));
    }


    //@Test(expected = IllegalArgumentException.class)
    public void test_dataLibrary_noProvider_throwsException() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\nusing FHIR version '3.0.0'\ndefine X:\n5+5");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        engine.evaluate("Test");
    }

    @Test
    public void test_twoExpressions_byLibrary_allReturned() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate("Test");

        assertNotNull(result);

        Object expResult = result.forExpression("X");
        assertThat(expResult, is(10));

        expResult = result.forExpression("Y");
        assertThat(expResult, is(4));
    }

    @Test
    public void test_twoExpressions_oneRequested_oneReturned() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate("Test", new HashSet<>(Arrays.asList("Y")));

        assertNotNull(result);

        Object expResult = result.forExpression("Y");
        assertThat(expResult, is(4));
    }

    @Test
    public void test_twoLibraries_expressionsForEach() throws IOException {

        Map<org.hl7.elm.r1.VersionedIdentifier, String> libraries = new HashMap<>();
        libraries.put(this.toElmIdentifier("Common", "1.0.0"),
            "library Common version '1.0.0'\ndefine Z:\n5+5\n");
        libraries.put(toElmIdentifier("Test", "1.0.0"),
            "library Test version '1.0.0'\ninclude Common version '1.0.0' named \"Common\"\ndefine X:\n5+5\ndefine Y: 2 + 2\ndefine W: \"Common\".Z + 5");


        LibraryManager libraryManager = this.toLibraryManager(libraries);
        List<CqlTranslatorException> errors = new ArrayList<>();
        List<Library> executableLibraries = new ArrayList<>();
        for (org.hl7.elm.r1.VersionedIdentifier id : libraries.keySet()) {
            TranslatedLibrary translated = libraryManager.resolveLibrary(id, CqlTranslatorOptions.defaultOptions(), errors);
            String json = this.convertToJson(translated.getLibrary());
            executableLibraries.add(this.readJson(json));
        }

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(executableLibraries);

        CqlEngine engine = new CqlEngine(libraryLoader);

        EvaluationResult result = engine.evaluate("Test", new HashSet<>(Arrays.asList("X", "Y", "W")));

        assertNotNull(result);
        assertEquals(3, result.expressionResults.size());
        assertThat(result.forExpression("X"), is(10));
        assertThat(result.forExpression("Y"), is(4));
        assertThat(result.forExpression("W"), is(15));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void validationEnabled_validatesTerminology() throws IOException  {
        Library library = this.toLibrary("library Test version '1.0.0'\ncodesystem \"X\" : 'http://example.com'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader, EnumSet.of(CqlEngine.Options.EnableValidation));
        engine.evaluate("Test");
    }

    @Test
    public void validationDisabled_doesNotValidateTerminology() throws IOException {
        Library library = this.toLibrary("library Test version '1.0.0'\ncodesystem \"X\" : 'http://example.com'\ndefine X:\n5+5\ndefine Y: 2 + 2");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        CqlEngine engine = new CqlEngine(libraryLoader);
        engine.evaluate("Test");
    }
}
