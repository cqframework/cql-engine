package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.util.*;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlCompilerException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.FunctionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class CqlMainSuiteTest implements ITest {

    private static final Logger logger = LoggerFactory.getLogger(CqlMainSuiteTest.class);

    private ExpressionDef expression;
    private Context context;

    @Factory(dataProvider = "dataMethod")
    public CqlMainSuiteTest(Context context, ExpressionDef expression) {
        this.expression = expression;
        this.context = context;
    }

    @DataProvider
    public static Object[][] dataMethod() throws UcumException, IOException {
        String[] listOfFiles = {
            "portable/CqlTestSuite.cql",
        };

        List<Object[]> testsToRun = new ArrayList<>();
        for (String file: listOfFiles) {
            Library library = translate(file);
            Context context = new Context(library, ZonedDateTime.of(2018, 1, 1, 7, 0, 0, 0, TimeZone.getDefault().toZoneId()));
            if (library.getStatements() != null) {
                for (ExpressionDef expression : library.getStatements().getDef()) {
                    if (expression instanceof FunctionDef) {
                        continue;
                    }
                    if (expression.getName().startsWith("test")) {
                        testsToRun.add(new Object[] {
                            context,
                            expression
                        });
                    }
                }
            }
        }
        return testsToRun.toArray(new Object[testsToRun.size()][]);
    }

    @Override
    public String getTestName() {
        return expression.getName();
    }

    // This test is for the various CQL operators
    @Test
    public void testMainSuite() throws IOException, UcumException {
        Assert.assertEquals(
            ((String)expression.evaluate(context)),
            getTestName().replaceAll("test_", "") + " TEST PASSED"
        );
    }

    private static Library translate(String file)  throws UcumException, IOException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));

        File cqlFile = new File(URLDecoder.decode(CqlMainSuiteTest.class.getResource(file).getFile(), "UTF-8"));

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
