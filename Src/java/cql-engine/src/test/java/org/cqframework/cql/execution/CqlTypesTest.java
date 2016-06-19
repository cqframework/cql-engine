package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.runtime.Concept;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlTypesTest extends CqlExecutionTestBase {

    @Test
    public void testAny() throws JAXBException {
      // TODO: Any is missing from elm.execution
        Context context = new Context(library);
        // Object result = context.resolveExpressionRef(library, "AnyTestInteger").getExpression().evaluate(context);
        // assertThat(result, is(5));
    }

    @Test
    public void testBoolean() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Code#evaluate(Context)}
     */
    @Test
    public void testCode() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CodeLiteral").getExpression().evaluate(context);
        assertThat(result, is(new Code().withCode("8480-6").withSystem("http://loinc.org").withDisplay("Systolic blood pressure")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Concept#evaluate(Context)}
     */
    @Test
    public void testConcept() throws JAXBException {
      Context context = new Context(library);
      // Object result = context.resolveExpressionRef(library, "CodeLiteral").getExpression().evaluate(context);
      // assertThat(result, is(new Concept().withCodes(new ArrayList<Code>(Arrays.asList("8480-6", "8462-4"))).withDisplay("Blood pressure codes")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.DateTime#evaluate(Context)}
     */
    @Test
    public void testDateTime() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testDecimal() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testInteger() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Quantity#evaluate(Context)}
     */
    @Test
    public void testQuantity() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testString() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Time#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }
}
