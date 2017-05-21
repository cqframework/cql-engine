package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.execution.Context;
import org.testng.annotations.Test;

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
    public void testExpressionSort() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);

        Object result = context.resolveExpressionRef("testExpressionSortDateTime").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);

        result = context.resolveExpressionRef("testExpressionSortEnumString").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);

        result = context.resolveExpressionRef("testExpressionSortInt").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);

        result = context.resolveExpressionRef("testExpressionSortQuantity").evaluate(context);
        assertTrue(result instanceof Iterable && ((List)result).size() > 0);
    }

}
