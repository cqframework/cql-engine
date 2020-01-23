package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

import org.opencds.cqf.cql.runtime.Code;

public class IncludedCodeRefTest extends CqlExecutionTestBase {
    @Test
    public void testCodeRef() {
        Context context = new Context(library);
        context.registerLibraryLoader(new TestLibraryLoader(getLibraryManager()));

        Object result = context.resolveExpressionRef("IncludedCode").getExpression().evaluate(context);
        assertNotNull(result);
        assertThat(result, is(instanceOf(Code.class)));
    }
}