package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.runtime.Code;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
