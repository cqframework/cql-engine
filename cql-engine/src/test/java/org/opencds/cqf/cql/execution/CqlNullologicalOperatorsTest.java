package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.runtime.Time;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlNullologicalOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CoalesceEvaluator#evaluate(Context)}
     */
    @Test
    public void testCoalesce() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("CoalesceANull").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("CoalesceNullA").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("CoalesceEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("CoalesceListFirstA").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("CoalesceListLastA").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("CoalesceFirstList").getExpression().evaluate(context);
        assertThat(result, is(Collections.singletonList("a")));

        result = context.resolveExpressionRef("CoalesceLastList").getExpression().evaluate(context);
        assertThat(result, is(Collections.singletonList("a")));

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        result = context.resolveExpressionRef("DateTimeCoalesce").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2012, 5, 18)));

        result = context.resolveExpressionRef("DateTimeListCoalesce").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2012, 5, 18)));

        result = context.resolveExpressionRef("TimeCoalesce").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 5, 15, 33, 556)));

        result = context.resolveExpressionRef("TimeListCoalesce").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 5, 15, 33, 556)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IsNullEvaluator#evaluate(Context)}
     */
    @Test
    public void testIsNull() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IsNullTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IsNullFalseEmptyString").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IsNullAlsoFalseAbcString").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IsNullAlsoFalseNumber1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IsNullAlsoFalseNumberZero").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IsFalseEvaluator#evaluate(Context)}
     */
    @Test
    public void testIsFalse() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IsFalseFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IsFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IsFalseNull").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IsTrueEvaluator#evaluate(Context)}
     */
    @Test
    public void testIsTrue() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IsTrueTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IsTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IsTrueNull").getExpression().evaluate(context);
        assertThat(result, is(false));
    }
}
