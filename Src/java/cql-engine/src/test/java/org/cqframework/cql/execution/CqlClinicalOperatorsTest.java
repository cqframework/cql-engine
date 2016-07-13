package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlClinicalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testAge() throws JAXBException {
        //TODO: Does this have a corresponding elm execution?
    }

    @Test
    public void testAgeAt() throws JAXBException {
        //TODO: Does this have a corresponding elm execution?
    }

    /**
     * {@link org.cqframework.cql.elm.execution.CalculateAge#evaluate(Context)}
     */
    @Test
    public void testCalculateAge() throws JAXBException {
        Context context = new Context(library);
        // Object result = context.resolveExpressionRef(library, "CalculateAgeYears").getExpression().evaluate(context);
        // assertThat(result, is(16));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.CalculateAgeAt#evaluate(Context)}
     */
    @Test
    public void testCalculateAgeAt() throws JAXBException {
        Context context = new Context(library);
        Object result;
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

        // TODO: Fix the equivalent operation for Code and Concept
        // result = context.resolveExpressionRef(library, "CodeEquivalentNull").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef(library, "ConceptEquivalentNull").getExpression().evaluate(context);
        // assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.InCodeSystem#evaluate(Context)}
     */
    @Test
    public void testInCodesystem() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ValueSetDef#evaluate(Context)}
     */
    @Test
    public void testInValueset() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }
}
