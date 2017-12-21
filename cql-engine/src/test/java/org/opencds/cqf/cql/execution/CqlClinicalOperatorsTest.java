package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.DateFromEvaluator;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.opencds.cqf.cql.elm.execution.CalculateAgeAtEvaluator;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Uncertainty;
import org.joda.time.Partial;
import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlClinicalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testAge() throws JAXBException {
        Context context = new Context(library);

        // Object result = context.resolveExpressionRef("AgeYears").getExpression().evaluate(context);
        // assertThat(result, is(2));
    }

    @Test
    public void testAgeAt() throws JAXBException {
        Context context = new Context(library);

        Object result;
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CalculateAgeEvaluator#evaluate(Context)}
     */
    @Test
    public void testCalculateAge() throws JAXBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Context context = new Context(library);
        DateTime now = context.getEvaluationDateTime();
        // TODO: fix this -- translation error
        // Object result = context.resolveExpressionRef("CalculateAgeYears").getExpression().evaluate(context);
        // assertThat(result, is(16));

        Object result = context.resolveExpressionRef("CalculateAgeMonths").getExpression().evaluate(context);
        assertThat(result, is((CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(3), new int[] {2000, 1, 1})), now, "month"))));

        result = context.resolveExpressionRef("CalculateAgeDays").getExpression().evaluate(context);
        assertThat(result, is(CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(3), new int[] {2000, 1, 1})), now, "day")));

        result = context.resolveExpressionRef("CalculateAgeHours").getExpression().evaluate(context);
        assertThat(result, is(CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(4), new int[] {2000, 1, 1, 0})), now, "hour")));

        result = context.resolveExpressionRef("CalculateAgeMinutes").getExpression().evaluate(context);
        assertThat(result, is(CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(5), new int[] {2000, 1, 1, 0, 0})), now, "minute")));

        result = context.resolveExpressionRef("CalculateAgeSeconds").getExpression().evaluate(context);
        assertThat(result, is(CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(6), new int[] {2000, 1, 1, 0, 0, 0})), now, "second")));

        result = context.resolveExpressionRef("CalculateAgeUncertain").getExpression().evaluate(context);
        Integer low = (Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(2), new int[] {2000, 12})), now, "month");
        Integer high = (Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime(new Partial(DateTime.getFields(2), new int[] {2000, 1})), now, "month");
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(low, true, high, true)));
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
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(187, true, 198, true)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EqualEvaluator#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CodeEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

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

        // TODO: Fix these -- figure out if Codes are allowed null components
        // result = context.resolveExpressionRef("CodeEquivalentNullTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("CodeEquivalentNullFalse").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef("ConceptEquivalentNullTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("ConceptEquivalentNullFalse").getExpression().evaluate(context);
        // assertThat(result, is(false));
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
