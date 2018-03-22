package org.opencds.cqf.cql.data.fhir;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.exceptions.FHIRException;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Tuple;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Christopher Schuler on 11/5/2016.
 */
public class TestFhirQuery extends FhirExecutionTestBase {

    @Test
    public void testCrossResourceSearch() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("xRefSearch").evaluate(context);

        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
    }

    @Test
    public void testLetClause() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("testLet").evaluate(context);

        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
    }

    @Test
    public void testExpressionSort() throws FHIRException {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);

        context.setEnableTraceLogging(true);

        Object result = context.resolveExpressionRef("testExpressionSortDateTime").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
        Procedure lastPro = null;
        for (Procedure procedure : (List<Procedure>) result) {
            if (lastPro == null && procedure.hasPerformedPeriod()) {
                lastPro = procedure;
            }
            else if (procedure.hasPerformedPeriod()) {
                assertTrue(
                        procedure.getPerformedPeriod().getStart().equals(lastPro.getPerformedPeriod().getStart())
                        || procedure.getPerformedPeriod().getStart().after(lastPro.getPerformedPeriod().getStart())
                );
            }
        }

        result = context.resolveExpressionRef("testExpressionSortEnumString").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
        Tuple lastTuple = null;
        for (Tuple tuple : (List<Tuple>) result) {
            if (lastTuple == null) {
                lastTuple = tuple;
                continue;
            }
            assertTrue(((String) tuple.getElements().get("theKind")).compareTo(((String) lastTuple.getElements().get("theKind"))) <= 0);
        }

        result = context.resolveExpressionRef("testExpressionSortInt").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
        Observation lastObs = null;
        for (Observation observation : (List<Observation>) result) {
            if (lastObs == null && observation.hasIssued()) {
                lastObs = observation;
            }
            else if (observation.hasIssued()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(observation.getIssued());
                int currentObsYear = cal.get(Calendar.YEAR);
                cal.setTime(lastObs.getIssued());
                int lastObsYear = cal.get(Calendar.YEAR);
                assertTrue(currentObsYear <= lastObsYear);
            }
        }
    }

}
