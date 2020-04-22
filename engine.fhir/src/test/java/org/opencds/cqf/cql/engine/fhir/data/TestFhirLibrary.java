package org.opencds.cqf.cql.engine.fhir.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.elm.execution.Library;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.RiskAssessment;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.CqlLibraryReader;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.retrieve.RestFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;

import ca.uhn.fhir.context.FhirContext;

public class TestFhirLibrary {

    //@Test
    public void TestCBP() throws IOException, JAXBException {
        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("library-cbp.elm.xml").getFile(), "UTF-8"));
        Library library = CqlLibraryReader.read(xmlFile);

        Context context = new Context(library);

        FhirContext fhirContext = FhirContext.forDstu3();

		Dstu3FhirModelResolver modelResolver = new Dstu3FhirModelResolver();
		RestFhirRetrieveProvider retrieveProvider = new RestFhirRetrieveProvider(new SearchParameterResolver(fhirContext), fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3"));
		CompositeDataProvider provider = new CompositeDataProvider(modelResolver, retrieveProvider);
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhir3.healthintersections.com.au/open/");
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://wildfhir.aegis.net/fhir");
        context.registerDataProvider("http://hl7.org/fhir", provider);

        Object result = context.resolveExpressionRef("BP: Systolic").evaluate(context);
        assertThat(result, instanceOf(Iterable.class));
        for (Object element : (Iterable<?>)result) {
            assertThat(element, instanceOf(Observation.class));
            Observation observation = (Observation)element;
            assertThat(observation.getCode().getCoding().get(0).getCode(), is("8480-6"));
        }

        result = context.resolveExpressionRef("BP: Diastolic").evaluate(context);
        assertThat(result, instanceOf(Iterable.class));
        for (Object element : (Iterable<?>)result) {
            assertThat(element, instanceOf(Observation.class));
            Observation observation = (Observation)element;
            assertThat(observation.getCode().getCoding().get(0).getCode(), is("8462-4"));
        }
    }

    // TODO: Fix this, it depends on the Convert...
    //@Test
    public void TestCMS9v4_CQM() throws IOException, JAXBException {
        File xmlFile = new File(URLDecoder.decode(TestFhirLibrary.class.getResource("CMS9v4_CQM.xml").getFile(), "UTF-8"));
        Library library = CqlLibraryReader.read(xmlFile);

        Context context = new Context(library);

        FhirContext fhirContext = FhirContext.forDstu3();

		Dstu3FhirModelResolver modelResolver = new Dstu3FhirModelResolver();
		RestFhirRetrieveProvider retrieveProvider = new RestFhirRetrieveProvider(new SearchParameterResolver(fhirContext),  fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3"));
		CompositeDataProvider provider = new CompositeDataProvider(modelResolver, retrieveProvider);
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhir3.healthintersections.com.au/open/");
        //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://wildfhir.aegis.net/fhir");
        context.registerDataProvider("http://hl7.org/fhir", provider);

        Object result = context.resolveExpressionRef("Breastfeeding Intention Assessment").evaluate(context);
        assertThat(result, instanceOf(Iterable.class));
        for (Object element : (Iterable<?>)result) {
            assertThat(element, instanceOf(RiskAssessment.class));
        }
    }
}
