package org.cqframework.cql.execution;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.ObjectFactory;
import org.cqframework.cql.elm.execution.ObjectFactoryEx;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import javax.xml.bind.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class CqlExecutionTestBase<T> {
    static Map<String, Library> libraries = new HashMap<String, Library>();
    Library library = null;
    private File xmlFile = null;

    @BeforeMethod
    public void beforeEachTestMethod() throws JAXBException, IOException {
        String fileName = this.getClass().getSimpleName();
        library = libraries.get(fileName);
        if (library == null) {
            LibraryManager libraryManager = new LibraryManager();
            try {
                File cqlFile = new File(URLDecoder.decode(this.getClass().getResource(fileName + ".cql").getFile(), "UTF-8"));

                ArrayList<CqlTranslator.Options> options = new ArrayList<>();
                options.add(CqlTranslator.Options.EnableDateRangeOptimization);
                CqlTranslator translator = CqlTranslator.fromFile(cqlFile, libraryManager, options.toArray(new CqlTranslator.Options[options.size()]));

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

                xmlFile = new File(cqlFile.getParent(), fileName + ".xml");
                xmlFile.createNewFile();

                PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
                pw.println(translator.toXml());
                pw.println();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            library = CqlLibraryReader.read(xmlFile);
            libraries.put(fileName, library);
        }
    }

    @AfterClass
    public void oneTimeTearDown() {
//        if (xmlFile != null) {
//            xmlFile.delete();
//        }
    }
}
