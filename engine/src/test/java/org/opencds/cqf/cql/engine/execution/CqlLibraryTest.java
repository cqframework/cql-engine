package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.bind.JAXBException;

import org.opencds.cqf.cql.engine.runtime.Code;
import org.testng.annotations.Test;

public class CqlLibraryTest extends CqlExecutionTestBase {

    @Test
    public void testCode() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("Code").getExpression().evaluate(context);
        assertThat(result, instanceOf(Code.class));

        Code code = (Code)result;
        assertEquals("testcode", code.getCode());
        assertEquals("http://terminology.hl7.org/CodeSystem/dummy-code", code.getSystem());
        assertEquals("TESTCODE", code.getDisplay());
        assertNull(code.getVersion());
    }
}
