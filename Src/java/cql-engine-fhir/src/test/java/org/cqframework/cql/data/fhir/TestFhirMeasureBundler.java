package org.cqframework.cql.data.fhir;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.execution.CqlLibraryReader;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.Observation;
import org.testng.annotations.Test;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Bryn on 5/7/2016.
 */
public class TestFhirMeasureBundler {
    @Test
    public void TestCBP() throws IOException, JAXBException {
        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("library-cbp.elm.xml").getFile(), "UTF-8"));
        Library library = CqlLibraryReader.read(xmlFile);

        Context context = new Context(library);

        FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open/");
        //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
        context.registerDataProvider("http://hl7.org/fhir", provider);

        FhirMeasureBundler bundler = new FhirMeasureBundler();
        org.hl7.fhir.dstu3.model.Bundle bundle = bundler.Bundle(context, "BP: Systolic", "BP: Diastolic");

        assertThat(bundle.getEntry().size(), greaterThan(0));
    }
}
