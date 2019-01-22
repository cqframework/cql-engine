package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.opencds.cqf.cql.runtime.Interval;
import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlClinicalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testAge() throws JAXBException {
        // Tests in the fhir engine
    }

    @Test
    public void testAgeAt() throws JAXBException {
        // Tests in the fhir engine
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CalculateAgeEvaluator#evaluate(Context)}
     */
    @Test
    public void testCalculateAge() throws JAXBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Context context = new Context(library, new DateTime(TemporalHelper.getDefaultOffset(), 2016, 1, 1, 0, 0, 0, 0));

         Object result = context.resolveExpressionRef("CalculateAgeYears").getExpression().evaluate(context);
         assertThat(result, is(6));

        result = context.resolveExpressionRef("CalculateAgeMonths").getExpression().evaluate(context);
        assertThat(result, is(72));

        result = context.resolveExpressionRef("CalculateAgeDays").getExpression().evaluate(context);
        assertThat(result, is(2191));

        result = context.resolveExpressionRef("CalculateAgeHours").getExpression().evaluate(context);
        assertThat(result, is(52583));

        result = context.resolveExpressionRef("CalculateAgeMinutes").getExpression().evaluate(context);
        assertThat(result, is(3155040));

        result = context.resolveExpressionRef("CalculateAgeSeconds").getExpression().evaluate(context);
        assertThat(result, is(189302400));

        result = context.resolveExpressionRef("CalculateAgeUncertain").getExpression().evaluate(context);
        assertThat(result.toString(), is((new Interval(61, true, 72, true)).toString()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CalculateAgeAtEvaluator#evaluate(Context)}
     */
    @Test
    public void testCalculateAgeAt() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CalculateAgeAtYears").getExpression().evaluate(context);
        assertThat(result, is(17));

        result = context.resolveExpressionRef("CalculateAgeAtMonths").getExpression().evaluate(context);
        assertThat(result, is(197));

        result = context.resolveExpressionRef("CalculateAgeAtDays").getExpression().evaluate(context);
        assertThat(result, is(6038));

        result = context.resolveExpressionRef("CalculateAgeAtHours").getExpression().evaluate(context);
        assertThat(result, is(144912));

        result = context.resolveExpressionRef("CalculateAgeAtMinutes").getExpression().evaluate(context);
        assertThat(result, is(8694720));

        result = context.resolveExpressionRef("CalculateAgeAtSeconds").getExpression().evaluate(context);
        assertThat(result, is(521683200));

        result = context.resolveExpressionRef("CalculateAgeAtUncertain").getExpression().evaluate(context);
        Assert.assertTrue(((Interval)result).getStart().equals(187));
        Assert.assertTrue(((Interval)result).getEnd().equals(198));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EqualEvaluator#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("Issue70A").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("Issue70B").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("CodeEqualNullVersion").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConceptEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEqualNullDisplay").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("CodeEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConceptEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EquivalentEvaluator#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CodeEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("CodeEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.InCodeSystemEvaluator#evaluate(Context)}
     */
    @Test
    public void testInCodesystem() throws JAXBException {
        // Tests in the fhir engine
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.InValueSetEvaluator#evaluate(Context)}
     */
    @Test
    public void testInValueset() throws JAXBException {
        // Tests in the fhir engine
    }
}
