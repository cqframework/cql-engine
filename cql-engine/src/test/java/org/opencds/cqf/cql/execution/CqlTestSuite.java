package org.opencds.cqf.cql.execution;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.FunctionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlTestSuite {

    final static Logger logger = LoggerFactory.getLogger(CqlTestSuite.class);

    // TODO - test value suites

    @Test
    public void testMainSuite() throws IOException, JAXBException, UcumException {
        Library library = translate("portable/CqlTestSuite.cql");
        Context context = new Context(library, new DateTime(new Partial(DateTime.getFields(7), new int[] {2018, 1, 1, 7, 0, 0, 0})));
        if (library.getStatements() != null) {
            for (ExpressionDef expression : library.getStatements().getDef()) {
                if (expression instanceof FunctionDef) {
                    continue;
                }
                if (expression.getName().startsWith("test")) {
                    logger.info((String) expression.evaluate(context));
                }
            }
        }
    }

    private Library translate(String file)  throws UcumException, JAXBException, IOException {
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

        String xml = translator.toXml();

        return CqlLibraryReader.read(new StringReader(xml));
    }
}
