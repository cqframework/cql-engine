package org.opencds.cqf.cql.data.fhir;

import org.cqframework.cql.elm.execution.Library;
import org.joda.time.DateTime;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Patient;
import org.testng.annotations.Test;
// import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.greaterThan;
// import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by Bryn on 5/7/2016.
 */
public class TestFhirMeasureEvaluator {
    @Test
    public void TestCBP() throws IOException, JAXBException {
        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("library-col.elm.xml").getFile(), "UTF-8"));
        Library library = CqlLibraryReader.read(xmlFile);

        Context context = new Context(library);

        FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open/");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
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
}
