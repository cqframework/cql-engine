package org.opencds.cqf.cql.data.fhir;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.opencds.cqf.cql.data.CompositeDataProvider;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.model.*;
import org.opencds.cqf.cql.retrieve.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu2;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class FhirExecutionTestBase {
	static Map<String, Library> libraries = new HashMap<>();


    protected Dstu2FhirModelResolver dstu2ModelResolver;
    protected RestFhirRetrieveProvider dstu2RetrieveProvider;
    protected CompositeDataProvider dstu2Provider;
    
    protected Dstu3FhirModelResolver dstu3ModelResolver;
    protected RestFhirRetrieveProvider dstu3RetrieveProvider;
    protected CompositeDataProvider dstu3Provider;
    protected HL7FhirModelResolver hl7ModelResolver;
    protected RestFhirRetrieveProvider hl7RetrieveProvider;
    protected CompositeDataProvider hl7Provider;

    Library library = null;
    protected File xmlFile = null;

    @BeforeClass
    public void setup() {
        dstu2ModelResolver = new Dstu2FhirModelResolver();
        dstu2RetrieveProvider = new RestFhirRetrieveProvider(FhirContext.forDstu2(), new SearchParamRegistryDstu2(),
                "http://fhirtest.uhn.ca/baseDstu2");
        dstu2Provider = new CompositeDataProvider(dstu2ModelResolver, dstu2RetrieveProvider);
        
        dstu3ModelResolver = new Dstu3FhirModelResolver();
        dstu3RetrieveProvider = new RestFhirRetrieveProvider(FhirContext.forDstu3(), new SearchParamRegistryDstu3(), "http://measure.eval.kanvix.com/cqf-ruler/baseDstu3");
        dstu3Provider = new CompositeDataProvider(dstu3ModelResolver, dstu3RetrieveProvider);
        
        hl7ModelResolver = new HL7FhirModelResolver();
        hl7RetrieveProvider = new RestFhirRetrieveProvider(FhirContext.forDstu2Hl7Org(), new SearchParamRegistryDstu2(), "http://fhirtest.uhn.ca/baseDstu2");
        hl7Provider = new CompositeDataProvider(hl7ModelResolver, hl7RetrieveProvider);
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
