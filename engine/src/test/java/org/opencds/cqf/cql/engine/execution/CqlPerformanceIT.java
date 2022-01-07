package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CqlPerformanceIT  extends TranslatingTestBase {

    private static final Integer ITERATIONS = 200;

    private static final Logger logger = LoggerFactory.getLogger(CqlPerformanceIT.class);

    // This test is a basically empty library that tests how long the engine initialization takes.
    @Test
    public void testEngineInit() throws IOException, UcumException {
        Library library = this.toLibrary("library Test");
        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        runPerformanceTest("Engine init", "Test", libraryLoader, 0.2);
    }

    // This test is for the various CQL operators
    @Test
    public void testMainSuite() throws IOException, UcumException {
        Library library = translate("portable/CqlTestSuite.cql");
        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        runPerformanceTest("CqlTestSuite", "CqlTestSuite", libraryLoader, 350.0);
    }

    // This test is for the runtime errors
    // @Test
    // TODO: Ratio type not implemented error
    public void testErrorSuite() throws IOException, UcumException {
        Library library = translate("portable/CqlErrorTestSuite.cql");

        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));

        runPerformanceTest("CqlErrorTestSuite", "CqlErrorTestSuite", libraryLoader, 10.0);
    }

    // This test is to check the validity of the internal representation of the CQL types (OPTIONAL)
    @Test
    public void testInternalTypeRepresentationSuite() throws IOException, UcumException {
        Library library = translate("portable/CqlInternalTypeRepresentationSuite.cql");
        LibraryLoader libraryLoader = new InMemoryLibraryLoader(Collections.singleton(library));
        runPerformanceTest("CqlInternalTypeRepresentationSuite", "CqlInternalTypeRepresentationSuite", libraryLoader, 3.0);
    }

    private Library translate(String file)  throws UcumException, IOException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));

        File cqlFile = new File(URLDecoder.decode(this.getClass().getResource(file).getFile(), "UTF-8"));

        CqlTranslator translator = CqlTranslator.fromFile(cqlFile, modelManager, libraryManager, ucumService);

        if (translator.getErrors().size() > 0) {
            System.err.println("Translation failed due to errors:");
            ArrayList<String> errors = new ArrayList<>();
            for (CqlTranslatorException error : translator.getErrors()) {
                TrackBack tb = error.getLocator();
                String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                        tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                System.err.printf("%s %s%n", lines, error.getMessage());
                errors.add(lines + error.getMessage());
            }
            throw new IllegalArgumentException(errors.toString());
        }

        assertThat(translator.getErrors().size(), is(0));

        String json = translator.toJxson();

        return JsonCqlLibraryReader.read(new StringReader(json));
    }

    private void runPerformanceTest(String testName, String libraryName, LibraryLoader libraryLoader, Double maxPerIterationMs) {
        // A new CqlEngine is created for each loop because it resets and rebuilds the context completely.

        // Warm up the JVM
        for (int i = 0; i < ITERATIONS; i++) {
            CqlEngine engine = new CqlEngine(libraryLoader);
            engine.evaluate(libraryName);
        }

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            CqlEngine engine = new CqlEngine(libraryLoader);
            engine.evaluate(libraryName);
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Double perIteration = (double)timeElapsed / (double)ITERATIONS;

        logger.info("{} performance test took {} millis for {} iterations. Per iteration: {} ms", testName, timeElapsed, ITERATIONS, perIteration);
        assertTrue(perIteration < maxPerIterationMs, String.format("%s took longer per iteration than allowed. max: %3.2f, actual: %3.2f", testName, maxPerIterationMs, perIteration));
    }
}
