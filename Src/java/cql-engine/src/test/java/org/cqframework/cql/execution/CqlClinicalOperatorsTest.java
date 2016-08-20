package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import org.cqframework.cql.elm.execution.CalculateAgeAtEvaluator;

import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Uncertainty;

import org.joda.time.Partial;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlClinicalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testAge() throws JAXBException {
      Context context = new Context(library);
      // Object result = context.resolveExpressionRef(library, "AgeYears").getExpression().evaluate(context);
      // assertThat(result, is(2));
    }

    @Test
    public void testAgeAt() throws JAXBException {
      Context context = new Context(library);
      Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.CalculateAge#evaluate(Context)}
     */
    @Test
    public void testCalculateAge() throws JAXBException {
        Context context = new Context(library);
        // TODO: fix this -- translation error
        // Object result = context.resolveExpressionRef(library, "CalculateAgeYears").getExpression().evaluate(context);
        // assertThat(result, is(16));

        Object result = context.resolveExpressionRef(library, "CalculateAgeMonths").getExpression().evaluate(context);
        assertThat(result, is((Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2000, 1, 1})), DateTime.getToday(), "month")));

        result = context.resolveExpressionRef(library, "CalculateAgeDays").getExpression().evaluate(context);
        assertThat(result, is((Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {2000, 1, 1})), DateTime.getToday(), "day")));

        result = context.resolveExpressionRef(library, "CalculateAgeHours").getExpression().evaluate(context);
        assertThat(result, is((Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(4), new int[] {2000, 1, 1, 0})), DateTime.getToday(), "hour")));

        result = context.resolveExpressionRef(library, "CalculateAgeMinutes").getExpression().evaluate(context);
        assertThat(result, is((Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(5), new int[] {2000, 1, 1, 0, 0})), DateTime.getToday(), "minute")));

        result = context.resolveExpressionRef(library, "CalculateAgeSeconds").getExpression().evaluate(context);
        assertThat(result, is((Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(6), new int[] {2000, 1, 1, 0, 0, 0})), DateTime.getToday(), "second")));

        result = context.resolveExpressionRef(library, "CalculateAgeUncertain").getExpression().evaluate(context);
        Integer low = (Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(2), new int[] {2000, 12})), DateTime.getToday(), "month");
        Integer high = (Integer)CalculateAgeAtEvaluator.calculateAgeAt(new DateTime().withPartial(new Partial(DateTime.getFields(2), new int[] {2000, 1})), DateTime.getToday(), "month");
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(low, true, high, true)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.CalculateAgeAt#evaluate(Context)}
     */
    @Test
    public void testCalculateAgeAt() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CalculateAgeAtYears").getExpression().evaluate(context);
        assertThat(result, is(17));

        result = context.resolveExpressionRef(library, "CalculateAgeAtMonths").getExpression().evaluate(context);
        assertThat(result, is(197));

        result = context.resolveExpressionRef(library, "CalculateAgeAtDays").getExpression().evaluate(context);
        assertThat(result, is(6038));

        result = context.resolveExpressionRef(library, "CalculateAgeAtHours").getExpression().evaluate(context);
        assertThat(result, is(144912));

        result = context.resolveExpressionRef(library, "CalculateAgeAtMinutes").getExpression().evaluate(context);
        assertThat(result, is(8694720));

        result = context.resolveExpressionRef(library, "CalculateAgeAtSeconds").getExpression().evaluate(context);
        assertThat(result, is(521683200));

        result = context.resolveExpressionRef(library, "CalculateAgeAtUncertain").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(187, true, 198, true)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equal#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CodeEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "CodeEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ConceptEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ConceptEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "CodeEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "ConceptEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equivalent#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CodeEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "CodeEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ConceptEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ConceptEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "CodeEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ConceptEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        // TODO: Fix these -- figure out if Codes are allowed null components
        // result = context.resolveExpressionRef(library, "CodeEquivalentNullTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef(library, "CodeEquivalentNullFalse").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef(library, "ConceptEquivalentNullTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef(library, "ConceptEquivalentNullFalse").getExpression().evaluate(context);
        // assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.InCodeSystem#evaluate(Context)}
     */
    @Test
    public void testInCodesystem() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "InCodeSystemStringNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InCodeSystemCodeNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InCodeSystemConceptNull").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ValueSetDef#evaluate(Context)}
     */
    @Test
    public void testInValueset() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "InValueSetFalseString").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InValueSetFalseCode").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InValueSetFalseConcept").getExpression().evaluate(context);
        assertThat(result, is(false));
    }
}
