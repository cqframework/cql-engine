package org.opencds.cqf.cql.data.fhir;

import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.file.fhir.FileBasedFhirProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;

/**
 * Created by Bryn on 4/16/2016.
 */
public class TestFhirDataProvider {
    @Test
    public void testFhirClient() {
        FhirContext fhirContext = FhirContext.forDstu3();
        IGenericClient fhirClient = fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

        Bundle patients = fhirClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
        assertTrue(patients.getEntry().size() > 0);
    }

    @Test
    public void testPatientRetrieve() {
        FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        Iterable<Object> results = provider.retrieve("Patient", null, "Patient", null, null, null, null, null, null, null, null);
        List<Patient> patients = new ArrayList<>();

        int resultCount = 0;
        for (Object o : results) {
            patients.add((Patient)o);
            resultCount++;
        }

        assertTrue(patients.size() == resultCount);
    }

    @Test
    public void testFileDataProvider() {
      // non-filtering tests
      // Patient context
      FileBasedFhirProvider provider = new FileBasedFhirProvider().withPath(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/data");
      Iterable<Object> results = provider.retrieve("Patient", "123", "Procedure", null, null, null, null, null, null, null, null);
      int size = 0;
      for (Object o : results)
        size++;
      if (size != 1)
        throw new IllegalArgumentException(String.format("%d", size));
      assertTrue(size == 1);

      // Population context
      results = provider.retrieve("Population", null, "Procedure", null, null, null, null, null, null, null, null);
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 2);

      // filtering tests
      // DateTime dtLow = new DateTime().withPartial(new Partial(DateTime.getFields(1), new int[] {2012}));
      // DateTime dtHigh = new DateTime().withPartial(new Partial(DateTime.getFields(1), new int[] {2013}));
      // results = provider.retrieve("Population", null, "Encounter", null, null, null, null, "period.end.value", null, null, new Interval(dtLow, true, dtHigh, true));
      // size = 0;
      // for (Object o : results)
      //   size++;
      // assertTrue(size == 1);
    }
}
