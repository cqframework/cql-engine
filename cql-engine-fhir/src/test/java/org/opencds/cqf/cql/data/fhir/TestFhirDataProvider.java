package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.joda.time.Partial;
import org.opencds.cqf.cql.file.fhir.FileBasedFhirProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Created by Bryn on 4/16/2016.
 */
public class TestFhirDataProvider {

    /* TODO: Fix this test --
        java.lang.IllegalArgumentException: Unknown TypeRestfulInteraction code 'patch'
            at org.hl7.fhir.dstu3.model.CapabilityStatement$TypeRestfulInteractionEnumFactory.fromCode(CapabilityStatement.java:507) */
    //@Test
    public void testFhirClient() {
        FhirContext fhirContext = FhirContext.forDstu3();
        IGenericClient fhirClient = fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

        Bundle patients = fhirClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
        assertTrue(patients.getEntry().size() > 0);
    }

    /* TODO: Fix this test --
        java.lang.IllegalArgumentException: Unknown TypeRestfulInteraction code 'patch'
            at org.hl7.fhir.dstu3.model.CapabilityStatement$TypeRestfulInteractionEnumFactory.fromCode(CapabilityStatement.java:507) */
    //@Test
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

    // TODO: Fix this test
    //@Test
    public void testFileDataProvider() {
      // non-filtering tests
      // Patient context
      FileBasedFhirProvider provider = new FileBasedFhirProvider(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/data", null);
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
      DateTime dtLow = new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2012, 1, 1}));
      DateTime dtHigh = new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2013, 12, 31}));
      // datePath test
      results = provider.retrieve("Population", null, "Encounter", null, null, null, null, "period.end.value", null, null, new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // dateHighPath test
      results = provider.retrieve("Population", null, "Encounter", null, null, null, null, null, null, "period.end.value", new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // dateLowPath test
      results = provider.retrieve("Population", null, "Encounter", null, null, null, null, null, "period.start.value", null, new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // dateLowPath & dateHighPath test
      results = provider.retrieve("Population", null, "Encounter", null, null, null, null, null, "period.start.value", "period.end.value", new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // valueset test
      results = provider.retrieve("Population", null, "Condition", null, "code", null, "procedure-outcome", null, null, null, null);
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // valueset test with datePath
      // false
      results = provider.retrieve("Population", null, "Condition", null, "code", null, "procedure-outcome", "onset.value", null, null, new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 0);

      // true
      DateTime dtLow2 = new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2010, 1, 1}));
      DateTime dtHigh2 = new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2011, 12, 31}));
      results = provider.retrieve("Population", null, "Condition", null, "code", null, "procedure-outcome", "onset.value", null, null, new Interval(dtLow2, true, dtHigh2, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // codes test
      List<Code> listOfCodes = new ArrayList<>();
      listOfCodes.add(new Code().withCode("183807002").withSystem("http://snomed.info/sct"));
      results = provider.retrieve("Population", null, "Encounter", null, "type", listOfCodes, null, null, null, null, null);
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // codes with datePath
      // true
      results = provider.retrieve("Population", null, "Encounter", null, "type", listOfCodes, null, null, "period.start.value", null, new Interval(dtLow, true, dtHigh, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 1);

      // false - bad date
      results = provider.retrieve("Population", null, "Encounter", null, "type", listOfCodes, null, "period.start.value", null, null, new Interval(dtLow2, true, dtHigh2, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 0);

      // false - bad code
      List<Code> listOfCodes2 = new ArrayList<>();
      listOfCodes2.add(new Code().withCode("183807007").withSystem("http://snomed.info/sct"));
      results = provider.retrieve("Population", null, "Encounter", null, "type", listOfCodes2, null, "period.start.value", null, null, new Interval(dtLow2, true, dtHigh2, true));
      size = 0;
      for (Object o : results)
        size++;
      assertTrue(size == 0);
    }


}
