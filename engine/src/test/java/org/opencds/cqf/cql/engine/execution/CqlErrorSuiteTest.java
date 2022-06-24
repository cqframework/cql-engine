package org.opencds.cqf.cql.engine.execution;

import org.cqframework.cql.cql2elm.CqlCompilerException;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlErrorSuiteTest {

    private static final Logger logger = LoggerFactory.getLogger(CqlErrorSuiteTest.class);

    private ExpressionDef expression;
    private Context context;

    //TODO: Remove this comment when portable/CqlErrorTestSuite.cql has real tests.
    //@Factory(dataProvider = "dataMethod")
    public CqlErrorSuiteTest(Context context, ExpressionDef expression) {
        this.expression = expression;
        this.context = context;
    }

    @DataProvider
    public static Object[][] dataMethod() throws UcumException, IOException {
        String[] listOfFiles = {
            "portable/CqlErrorTestSuite.cql",
        };

        List<Object[]> testsToRun = new ArrayList<>();
        for (String file: listOfFiles) {
            Library library = translate(file);
            Context context = new Context(library, ZonedDateTime.of(2018, 1, 1, 7, 0, 0, 0, TimeZone.getDefault().toZoneId()));
            if (library.getStatements() != null) {
                for (ExpressionDef expression : library.getStatements().getDef()) {
                    testsToRun.add(new Object[] {
                        context,
                        expression
                    });
                }
            }
        }
        return testsToRun.toArray(new Object[testsToRun.size()][]);
    }

    // This test is for the various CQL operators
    @Test
    public void testErrorSuite() throws IOException, UcumException {
        try {
            expression.evaluate(context);
            logger.error("Test " + expression.getName() + " should result in an error");
            Assert.fail();
        }
        catch (Exception e) {
            // pass
            logger.info(expression.getName() + " TEST PASSED");
        }
    }

    private static Library translate(String file)  throws UcumException, IOException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));

        File cqlFile = new File(URLDecoder.decode(CqlErrorSuiteTest.class.getResource(file).getFile(), "UTF-8"));

        CqlTranslator translator = CqlTranslator.fromFile(cqlFile, modelManager, libraryManager, ucumService);

        if (translator.getErrors().size() > 0) {
            System.err.println("Translation failed due to errors:");
            ArrayList<String> errors = new ArrayList<>();
            for (CqlCompilerException error : translator.getErrors()) {
                TrackBack tb = error.getLocator();
                String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                        tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                System.err.printf("%s %s%n", lines, error.getMessage());
                errors.add(lines + error.getMessage());
            }
            throw new IllegalArgumentException(errors.toString());
        }

        assertThat(translator.getErrors().size(), is(0));

        String json = translator.toJson();

        return JsonCqlLibraryReader.read(new StringReader(json));
    }
}
