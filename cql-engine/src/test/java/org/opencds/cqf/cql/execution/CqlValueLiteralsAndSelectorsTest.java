package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.*;
import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlValueLiteralsAndSelectorsTest extends CqlExecutionTestBase{

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EquivalentEvaluator#evaluate(Context)}
     */
    @Test
    public void testBoolean() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("BooleanFalse").getExpression().evaluate(context);
        Object resultOutput = context.resolveExpressionRef("BooleanFalseOutput").getExpression().evaluate(context);
        assertThat(result, is(false));
        assertThat(resultOutput, is(false));

        result = context.resolveExpressionRef("BooleanTrue").getExpression().evaluate(context);
        assertThat(result, is(true));
    }
}
