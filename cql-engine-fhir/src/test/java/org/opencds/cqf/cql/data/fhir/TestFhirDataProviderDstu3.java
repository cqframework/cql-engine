package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.file.fhir.FileBasedFhirProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Created by Bryn on 4/16/2016.
 */
public class TestFhirDataProviderDstu3 extends FhirExecutionTestBase {

    @Test
    public void testFhirClient() {
        FhirContext fhirContext = FhirContext.forDstu3();
        IGenericClient fhirClient = fhirContext.newRestfulGenericClient("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3");

        Bundle patients = fhirClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
        assertTrue(patients.getEntry().size() > 0);
    }

    @Test
    public void testDataProviderRetrieve() {
        BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3");
        FhirBundleCursorStu3 results = (FhirBundleCursorStu3) provider.retrieve("Patient", null, "Patient", null, null, null, null, null, null, null, null);

        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testPatientRetrieve() {
        BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3");
        Iterable<Object> results = provider.retrieve("Patient", "Patient-12214", "Patient", null, null, null, null, null, null, null, null);
        List<Patient> patients = new ArrayList<>();

        int resultCount = 0;
        for (Object o : results) {
            patients.add((Patient)o);
            resultCount++;
        }

        assertTrue(patients.size() == resultCount);
    }

    @Test
    public void testChoiceTypes() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);

        Object result = context.resolveExpressionRef("testChoiceTypes").getExpression().evaluate(context);
        Assert.assertTrue(result != null);
    }

    // @Test
    public void testDateType() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        context.setContextValue("Patient", "Patient-12214");

        Object result = context.resolveExpressionRef("testDateType").getExpression().evaluate(context);
        Assert.assertTrue(result != null);
    }

//    TODO - fix
//    @Test
    public void testPostSearch() {
        Context context = new Context(library);

        String patientId = "post-search-example";
        Patient patient = new Patient();

        dstu3Provider.fhirClient.update().resource(patient).withId(patientId).execute();

        MedicationRequest request = new MedicationRequest();
        request.setIntent(MedicationRequest.MedicationRequestIntent.ORDER)
                .setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE)
                .setMedication(new CodeableConcept().addCoding(new Coding().setCode("1049502").setSystem("http://www.nlm.nih.gov/research/umls/rxnorm")))
                .setSubject(new Reference("Patient/" + patientId))
                .setAuthoredOn(new Date());

        dstu3Provider.fhirClient.update().resource(request).withId(patientId).execute();

        dstu3Provider.setSearchUsingPOST(true);
        dstu3Provider.setTerminologyProvider(new FhirTerminologyProvider().setEndpoint("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3", false));
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        context.enterContext("Patient");
        context.setContextValue("Patient", patientId);

        Object result = context.resolveExpressionRef("Active Ambulatory Opioid Rx").getExpression().evaluate(context);
        Assert.assertTrue(result instanceof List && ((List) result).size() == 1);
    }

    @Test
    public void testList() {
        BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhir.hl7.de:8080/baseDstu3");
        FhirBundleCursorStu3 results = (FhirBundleCursorStu3) provider.retrieve("Patient", null, "List", null, null, null, null, null, null, null, null);
        List<ListResource> lists = new ArrayList<>();
        int resultCount = 0;
        for (Object o : results) {
            lists.add((ListResource)o);
            resultCount++;
        }

        assertTrue(lists.size() == resultCount);
    }

    // @Test
    public void testFileDataProvider() {
        // non-filtering tests
        // Patient context
        FileBasedFhirProvider provider = new FileBasedFhirProvider(System.getProperty("user.dir") + "/src/test/resources/org/opencds/cqf/cql/data/data", null);
        Iterable<Object> results = provider.retrieve("Patient", "123", "Procedure", null, null, null, null, null, null, null, null);
        int size = 0;
        for (Object o : results)
            size++;
        assertTrue(size == 1);

        results = provider.retrieve("Patient", "123", "Condition", null, "code", null, "end-of-life-conditions", null, null, null, null);
        size = 0;
        for (Object o : results)
            size++;
        assertTrue(size == 1);

        results = provider.retrieve("Patient", "123", "Condition", null, "code", null, "http://hl7.org/fhir/ig/opioid-cds/ValueSet/end-of-life-conditions", null, null, null, null);
        size = 0;
        for (Object o : results)
            size++;
        assertTrue(size == 1);

        List<Code> codes = new ArrayList<>();
        Code code = new Code().withSystem("http://snomed.info/sct").withCode("94398002");
        codes.add(code);
        results = provider.retrieve("Patient", "123", "Condition", null, "code", codes, null, null, null, null, null);
        size = 0;
        for (Object o : results)
            size++;
        assertTrue(size == 1);

        /*
            Commented out by Darren D  per Chris S' instruction.
            Test failure on DD machine not reproduced by CS.
            Gradle suite > Gradle test > org.opencds.cqf.cql.data.fhir.TestFhirDataProviderDstu3.testFileDataProvider FAILED
                java.lang.NullPointerException at TestFhirDataProviderDstu3.java:89
            Issue not fixed but considered unimportant to do so due to rare use of test.
            TODO: Fix it.

        // Population context
        results = provider.retrieve("Population", null, "Procedure", null, null, null, null, null, null, null, null);
        size = 0;
        for (Object o : results)
            size++;
        assertTrue(size == 2);

        // filtering tests
        DateTime dtLow = new DateTime(new Partial(DateTime.getFields(3), new int[] {2012, 1, 1}));
        DateTime dtHigh = new DateTime(new Partial(DateTime.getFields(3), new int[] {2013, 12, 31}));
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

        // datePath with an Interval
        results = provider.retrieve("Population", null, "Encounter", null, null, null, null, "period", null, null, new Interval(dtLow, true, dtHigh, true));
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
        DateTime dtLow2 = new DateTime(new Partial(DateTime.getFields(3), new int[] {2010, 1, 1}));
        DateTime dtHigh2 = new DateTime(new Partial(DateTime.getFields(3), new int[] {2011, 12, 31}));
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

        */
    }


}
