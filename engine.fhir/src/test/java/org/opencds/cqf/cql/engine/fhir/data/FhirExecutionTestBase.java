package org.opencds.cqf.cql.engine.fhir.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.execution.CqlLibraryReader;
import org.opencds.cqf.cql.engine.fhir.model.Dstu2FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.retrieve.RestFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import ca.uhn.fhir.context.FhirContext;

public abstract class FhirExecutionTestBase {
	static Map<String, Library> libraries = new HashMap<>();


    protected Dstu2FhirModelResolver dstu2ModelResolver;
    protected RestFhirRetrieveProvider dstu2RetrieveProvider;
    protected CompositeDataProvider dstu2Provider;
    
    protected Dstu3FhirModelResolver dstu3ModelResolver;
    protected RestFhirRetrieveProvider dstu3RetrieveProvider;
    protected CompositeDataProvider dstu3Provider;

    Library library = null;
    protected File xmlFile = null;

    @BeforeClass
    public void setup() {
        dstu2ModelResolver = new Dstu2FhirModelResolver();
        FhirContext dstu2Context = FhirContext.forDstu2();
        dstu2RetrieveProvider = new RestFhirRetrieveProvider(new SearchParameterResolver(dstu2Context),
                dstu2Context.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2"));
        dstu2Provider = new CompositeDataProvider(dstu2ModelResolver, dstu2RetrieveProvider);
        
        dstu3ModelResolver = new Dstu3FhirModelResolver();
        FhirContext dstu3Context = FhirContext.forDstu3();
        dstu3RetrieveProvider = new RestFhirRetrieveProvider(new SearchParameterResolver(dstu3Context), 
        dstu3Context.newRestfulGenericClient("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3"));
        dstu3Provider = new CompositeDataProvider(dstu3ModelResolver, dstu3RetrieveProvider);

    }

    @BeforeMethod
    public void beforeEachTestMethod() throws JAXBException, IOException, UcumException {
        String fileName = this.getClass().getSimpleName();
        library = libraries.get(fileName);
        if (library == null) {
            ModelManager modelManager = new ModelManager();
            LibraryManager libraryManager = new LibraryManager(modelManager);
            UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));
            try {
                File cqlFile = new File(URLDecoder.decode(this.getClass().getResource(fileName + ".cql").getFile(), "UTF-8"));

                ArrayList<CqlTranslator.Options> options = new ArrayList<>();
                options.add(CqlTranslator.Options.EnableDateRangeOptimization);
                CqlTranslator translator = CqlTranslator.fromFile(cqlFile, modelManager, libraryManager, ucumService, options.toArray(new CqlTranslator.Options[options.size()]));

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
}
