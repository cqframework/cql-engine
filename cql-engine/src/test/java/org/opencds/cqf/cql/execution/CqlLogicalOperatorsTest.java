package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;
import javax.xml.bind.JAXBException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlLogicalOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AndEvaluator#evaluate(Context)}
     */
    @Test
    public void testAnd() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("TrueAndTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TrueAndFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TrueAndNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("FalseAndTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("FalseAndFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("FalseAndNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NullAndTrue").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullAndFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NullAndNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ImpliesEvaluator#evaluate(Context)}
     */
    @Test
    public void testImplies() throws JAXBException {
        Context context = new Context(library);
        // TODO: uncomment this and cql once working
        // Object result = context.resolveExpressionRef("TrueImpliesTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("TrueImpliesFalse").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef("TrueImpliesNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));
        //
        // result = context.resolveExpressionRef("FalseImpliesTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("FalseImpliesFalse").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("FalseImpliesNull").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("NullImpliesTrue").getExpression().evaluate(context);
        // assertThat(result, is(true));
        //
        // result = context.resolveExpressionRef("NullImpliesFalse").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));
        //
        // result = context.resolveExpressionRef("NullImpliesNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.NotEqualEvaluator#evaluate(Context)}
     */
    @Test
    public void testNot() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("NotTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NotFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("NotNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.OrEvaluator#evaluate(Context)}
     */
    @Test
    public void testOr() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("TrueOrTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TrueOrFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TrueOrNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("FalseOrTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("FalseOrFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("FalseOrNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullOrTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("NullOrFalse").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullOrNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.XorEvaluator#evaluate(Context)}
     */
    @Test
    public void testXOr() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("TrueXorTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TrueXorFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TrueXorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("FalseXorTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("FalseXorFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("FalseXorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullXorTrue").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullXorFalse").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NullXorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }
}
