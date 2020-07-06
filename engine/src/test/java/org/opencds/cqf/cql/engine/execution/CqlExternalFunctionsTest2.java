package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.opencds.cqf.cql.engine.data.SystemExternalFunctionProvider;
import org.opencds.cqf.cql.engine.execution.external.MyMath2;
import org.testng.annotations.Test;

public class CqlExternalFunctionsTest2 extends CqlExecutionTestBase {

    @Test
    public void testExternalFunctions() throws JAXBException {
        Context context = new Context(library);

        context.registerExternalFunctionProvider(
            library.getIdentifier(),
            new SystemExternalFunctionProvider(Arrays.asList(MyMath2.class.getDeclaredMethods()))
        );

        Object result;

        result = context.resolveExpressionRef("CallMyTimes").getExpression().evaluate(context);
        assertThat(result, is(54));

        result = context.resolveExpressionRef("CallMyDividedBy").getExpression().evaluate(context);
        assertThat(result, is(6));
    }
}
