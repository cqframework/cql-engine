package org.opencds.cqf.cql.data.fhir;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.DefaultLibrarySourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.opencds.cqf.cql.execution.CqlLibraryReader;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Christopher Schuler on 11/5/2016.
 */
public class TestCrossResourceSearch {

    private File xmlFile;
    private Library library;

    private LibraryManager libraryManager;
    private LibraryManager getLibraryManager() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager();
            DefaultLibrarySourceProvider librarySourceProvider = new DefaultLibrarySourceProvider(new File(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/fhir/").toPath().toAbsolutePath());
            libraryManager.getLibrarySourceLoader().registerProvider(librarySourceProvider);
        }
        return libraryManager;
    }

//    @Test
//    public void test1 () throws JAXBException, IOException {
//        ArrayList<Object> results = new ArrayList<>();
//        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/fhir").toAbsolutePath();
//        JsonFileBasedFhirProvider provider = new JsonFileBasedFhirProvider(System.getProperty("user.dir") +
//                "/src/test/resources/org/opencds/cqf/cql/data/data", "http://nsengdevsfcluster.westus.cloudapp.azure.com:8314/terminology");
//
//        try {
//            translate(new File(path.resolve("SimpleCrossResourceSearch.cql").toString()));
//        } catch (IllegalArgumentException e) {
//            System.out.println("Translation failed: " + e.getMessage());
//            return;
//        }
//
//        Context context = new Context(library);
//        context.registerDataProvider("http://hl7.org/fhir", provider);
//
//        for (ExpressionDef exp : library.getStatements().getDef()) {
//            context.enterContext(exp.getContext());
//            if (context.getCurrentContext().equals("Patient")) {
//                context.setContextValue("Patient", "CrossResourceSearchData");
//            }
//            Object result = exp.getExpression().evaluate(context);
//            results.add(result);
//            System.out.println(result.toString());
//        }
//
//        assertTrue(results.size() > 0);
//    }
//
//    @Test
//    public void test2 () throws JAXBException, IOException {
//        ArrayList<Object> results = new ArrayList<>();
//        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/fhir").toAbsolutePath();
//        JsonFileBasedFhirProvider provider = new JsonFileBasedFhirProvider(System.getProperty("user.dir") +
//                "/src/test/resources/org/opencds/cqf/cql/data/data", "http://nsengdevsfcluster.westus.cloudapp.azure.com:8314/terminology");
//
//        try {
//            translate(new File(path.resolve("ComplexCrossResourceSearch.cql").toString()));
//        } catch (IllegalArgumentException e) {
//            System.out.println("Translation failed: " + e.getMessage());
//            return;
//        }
//
//        Context context = new Context(library);
//        context.registerDataProvider("http://hl7.org/fhir", provider);
//
//        for (ExpressionDef exp : library.getStatements().getDef()) {
//            context.enterContext(exp.getContext());
//            if (context.getCurrentContext().equals("Patient")) {
//                context.setContextValue("Patient", "CrossResourceSearchData");
//            }
//            Object result = exp.getExpression().evaluate(context);
//            results.add(result);
//            System.out.println(result.toString());
//        }
//
//        assertTrue(results.size() > 0);
//    }

    public void translate(File cql) throws JAXBException, IOException {
        try {
            ArrayList<CqlTranslator.Options> options = new ArrayList<>();
            options.add(CqlTranslator.Options.EnableDateRangeOptimization);
            CqlTranslator translator = CqlTranslator.fromFile(cql, new LibraryManager(), options.toArray(new CqlTranslator.Options[options.size()]));
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

            // output translated library for review
            xmlFile = new File("response.xml");
            xmlFile.createNewFile();
            PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
            pw.println(translator.toXml());
            pw.println();
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        library = CqlLibraryReader.read(xmlFile);
    }
}
