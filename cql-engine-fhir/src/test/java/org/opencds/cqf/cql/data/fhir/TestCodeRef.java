package org.opencds.cqf.cql.data.fhir;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Christopher on 4/25/2017.
 */
public class TestCodeRef {

    private Library library;
    private FhirTerminologyProvider terminologyProvider =
            new FhirTerminologyProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
    private Path path =
            Paths.get(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/fhir")
                    .toAbsolutePath();

    @BeforeMethod
    public void setup() {
        try {
            translate(new File(path.resolve("TestCodeRef.cql").toString()));
        } catch (JAXBException e) {
            System.out.println("Translation failed: " + e.getMessage());
            return;
        }
    }

    @Test
    public void CodeRefTest1() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef1").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void CodeRefTest2() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef2").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    public void translate(File cql) throws JAXBException {
        try {
            ArrayList<CqlTranslator.Options> options = new ArrayList<>();
            options.add(CqlTranslator.Options.EnableDateRangeOptimization);
            CqlTranslator translator = CqlTranslator.fromFile(cql, new ModelManager(), new LibraryManager(new ModelManager()), options.toArray(new CqlTranslator.Options[options.size()]));
            if (translator.getErrors().size() > 0) {
                System.err.println("Translation failed due to errors:");
                ArrayList<String> errors = new ArrayList<>();
                for (CqlTranslatorException error : translator.getErrors()) {
                    TrackBack tb = error.getLocator();
                    String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                            tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                    errors.add(lines + error.getMessage());
                }
                throw new IllegalArgumentException(errors.toString());
            }

            File xmlFile = new File("response.xml");
            xmlFile.createNewFile();
            PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
            pw.println(translator.toXml());
            pw.println();
            pw.close();
            library = CqlLibraryReader.read(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
