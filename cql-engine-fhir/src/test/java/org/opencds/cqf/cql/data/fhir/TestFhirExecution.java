package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.execution.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by Christopher on 5/11/2017.
 */
public class TestFhirExecution extends FhirExecutionTestBase {

    // TODO: fix this... I think it requires a resource to be loaded - put in init bundle
    //@Test
    public void testCoalesce() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("testCoalesce").getExpression().evaluate(context);
        Assert.assertTrue((Integer)((List) result).get(0) == 72);
    }

    @Test
    public void testMonthFrom() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        context.setParameter(null, "MAXYEAR", 2014);
        Object result = context.resolveExpressionRef("testMonthFrom").getExpression().evaluate(context);
        Assert.assertTrue(result != null);
    }

    @Test
    public void testMultisourceQueryCreatingDatePeriod() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("Immunizations in range").getExpression().evaluate(context);
        Assert.assertTrue(result != null);
    }

    @Test
    public void testIdResolution() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("Resource Id").getExpression().evaluate(context);
        Assert.assertTrue(result != null);
    }
}
