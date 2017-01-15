package org.opencds.cqf.cql.data.fhir;

import org.apache.commons.io.FileUtils;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhirpath.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.execution.LibraryLoader;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// import javax.xml.bind.JAXB;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.greaterThan;
// import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by Bryn on 5/7/2016.
 */
public class TestFhirMeasureEvaluator {
    //@Test
    public void TestCBP() throws IOException, JAXBException {
        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("library-col.elm.xml").getFile(), "UTF-8"));
        Library library = CqlLibraryReader.read(xmlFile);

        Context context = new Context(library);

        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
        FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://open-api2.hspconsortium.org/payerextract/data");

        FhirTerminologyProvider terminologyProvider = new FhirTerminologyProvider().withBasicAuth("brhodes", "apelon123!").withEndpoint("http://fhir.ext.apelon.com/dtsserverws/fhir");
        provider.setTerminologyProvider(terminologyProvider);
        provider.setExpandValueSets(true);

        context.registerDataProvider("http://hl7.org/fhir", provider);

        xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("measure-col.xml").getFile(), "UTF-8"));
        Measure measure = provider.getFhirClient().getFhirContext().newXmlParser().parseResource(Measure.class, new FileReader(xmlFile));

        String patientId = "Patient-12214";
        Patient patient = provider.getFhirClient().read().resource(Patient.class).withId(patientId).execute();
        // TODO: Couldn't figure out what matcher to use here, gave up.
        if (patient == null) {
            throw new RuntimeException("Patient is null");
        }

        context.setContextValue("Patient", patientId);

        FhirMeasureEvaluator evaluator = new FhirMeasureEvaluator();

        // Java's date support is _so_ bad.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2014, 1, 1, 0, 0, 0);
        Date periodStart = cal.getTime();
        cal.set(2014, 12, 31, 11, 59, 59);
        Date periodEnd = cal.getTime();

        org.hl7.fhir.dstu3.model.MeasureReport report = evaluator.evaluate(provider.getFhirClient(), context, measure, patient, periodStart, periodEnd);

        if (report == null) {
            throw new RuntimeException("MeasureReport is null");
        }

        if (report.getEvaluatedResources() == null) {
            throw new RuntimeException("EvaluatedResources is null");
        }

        System.out.println(String.format("Bundle url: %s", report.getEvaluatedResources().getReference()));
    }

    private ModelManager modelManager;
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = new ModelManager();
        }

        return modelManager;
    }

    private LibraryManager libraryManager;
    private LibraryManager getLibraryManager() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager(getModelManager());
            libraryManager.getLibrarySourceLoader().clearProviders();
            libraryManager.getLibrarySourceLoader().registerProvider(new TestLibrarySourceProvider());
        }
        return libraryManager;
    }

    private LibraryLoader libraryLoader;
    private LibraryLoader getLibraryLoader() {
        if (libraryLoader == null) {
            libraryLoader = new TestLibraryLoader(libraryManager);
        }
        return libraryLoader;
    }

    private Library translate(String cql) {
//        try {
        ArrayList<CqlTranslator.Options> options = new ArrayList<>();
        options.add(CqlTranslator.Options.EnableDateRangeOptimization);
        CqlTranslator translator = CqlTranslator.fromText(cql, getModelManager(), getLibraryManager(), options.toArray(new CqlTranslator.Options[options.size()]));
        if (translator.getErrors().size() > 0) {
            ArrayList<String> errors = new ArrayList<>();
            for (CqlTranslatorException error : translator.getErrors()) {
                TrackBack tb = error.getLocator();
                String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                        tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                errors.add(lines + error.getMessage());
            }
            throw new IllegalArgumentException(errors.toString());
        }

//            // output translated library for review
//            xmlFile = new File("response.xml");
//            xmlFile.createNewFile();
//            PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
//            pw.println(translator.toXml());
//            pw.println();
//            pw.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        Library library = null;
        try {
            library = CqlLibraryReader.read(new StringReader(translator.toXml()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return library;
    }

    @Test
    public void TestMeasure() throws IOException, JAXBException {
        File cqlFile = new File(URLDecoder.decode(TestFhirMeasureEvaluator.class.getResource("library-test.cql").getFile(), "UTF-8"));
        String cql = FileUtils.readFileToString(cqlFile, StandardCharsets.UTF_8);
        Library library = translate(cql);

        Context context = new Context(library);

        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
        FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://measure.eval.kanvix.com/cql-measure-processor/baseDstu3");

        FhirTerminologyProvider terminologyProvider = new FhirTerminologyProvider().withBasicAuth("brhodes", "apelon123!").withEndpoint("http://fhir.ext.apelon.com/dtsserverws/fhir");
        provider.setTerminologyProvider(terminologyProvider);
        provider.setExpandValueSets(false);

        context.registerDataProvider("http://hl7.org/fhir", provider);

        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("measure-test.xml").getFile(), "UTF-8"));
        Measure measure = provider.getFhirClient().getFhirContext().newXmlParser().parseResource(Measure.class, new FileReader(xmlFile));

        String patientId = "PAT-00001";
        Patient patient = provider.getFhirClient().read().resource(Patient.class).withId(patientId).execute();
        // TODO: Couldn't figure out what matcher to use here, gave up.
        if (patient == null) {
            throw new RuntimeException("Patient is null");
        }

        context.setContextValue("Patient", patientId);

        FhirMeasureEvaluator evaluator = new FhirMeasureEvaluator();

        // Java's date support is _so_ bad.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2014, 1, 1, 0, 0, 0);
        Date periodStart = cal.getTime();
        cal.set(2014, 12, 31, 11, 59, 59);
        Date periodEnd = cal.getTime();

        org.hl7.fhir.dstu3.model.MeasureReport report = evaluator.evaluate(provider.getFhirClient(), context, measure, patient, periodStart, periodEnd);

        if (report == null) {
            throw new RuntimeException("MeasureReport is null");
        }

        if (report.getEvaluatedResources() == null) {
            throw new RuntimeException("EvaluatedResources is null");
        }

        System.out.println(String.format("Bundle url: %s", report.getEvaluatedResources().getReference()));
    }
}
