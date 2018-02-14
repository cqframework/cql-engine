package org.opencds.cqf.cql.execution;

import java.util.Arrays;
import javax.xml.bind.JAXBException;

import org.opencds.cqf.cql.data.SystemExternalFunctionProvider;
import org.opencds.cqf.cql.execution.external.*;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlExternalFunctionsTest extends CqlExecutionTestBase {

    @Test
    public void testExternalFunctions() throws JAXBException {
        Context context = new Context(library);

        context.registerExternalFunctionProvider(
            library.getIdentifier(),
            new SystemExternalFunctionProvider(Arrays.asList(MyMath.class.getDeclaredMethods()))
        );

        Object result;

        result = context.resolveExpressionRef("CallMyPlus").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef("CallMyMinus").getExpression().evaluate(context);
        assertThat(result, is(-2));
    }
}
