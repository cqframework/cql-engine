package org.cqframework.cql.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.runtime.Concept;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

public class CqlTypeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.cqframework.cql.elm.execution.As#evaluate(Context)}
     */
    //@Test
    public void testAs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "Int1ToString").getExpression().evaluate(context);
        assertThat(result, is("1"));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Convert#evaluate(Context)}
     */
    //@Test
    public void testConvert() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DecimalToInteger").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "IntegerToString").getExpression().evaluate(context);
        assertThat(result, is("5"));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Is#evaluate(Context)}
     */
    //@Test
    public void testIs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "IntegerIsInteger").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "StringIsInteger").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToBoolean#evaluate(Context)}
     */
    //@Test
    public void testToBoolean() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "StringNoToBoolean").getExpression().evaluate(context);
        assertThat(result, is(false));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToConcept#evaluate(Context)}
     */
    //@Test
    public void testToConcept() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CodeToConcept1").getExpression().evaluate(context);
        assertThat(result, is(new Concept().withCode(new Code().withCode("8480-6").withSystem("http://loinc.org").withDisplay("Systolic blood pressure"))));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToDateTime#evaluate(Context)}
     */
    //@Test
    public void testToDateTime() throws JAXBException {
        Context context = new Context(library);
        Object result;

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToDecimal#evaluate(Context)}
     */
    //@Test
    public void testToDecimal() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "String25D5ToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("25.5")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToInteger#evaluate(Context)}
     */
    //@Test
    public void testToInteger() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "StringNeg25ToInteger").getExpression().evaluate(context);
        assertThat(result, is(-25));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToQuantity#evaluate(Context)}
     */
    //@Test
    public void testToQuantity() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "String5D5CMToQuantity").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("5.5")).withUnit("cm")));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToString#evaluate(Context)}
     */
    //@Test
    public void testToString() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "IntegerNeg5ToString").getExpression().evaluate(context);
        assertThat(result, is("-5"));

        result = context.resolveExpressionRef(library, "Decimal18D55ToString").getExpression().evaluate(context);
        assertThat(result, is("18.55"));

        result = context.resolveExpressionRef(library, "Quantity5D5CMToString").getExpression().evaluate(context);
        assertThat(result, is("5.5cm"));

        result = context.resolveExpressionRef(library, "BooleanTrueToString").getExpression().evaluate(context);
        assertThat(result, is("true"));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToTime#evaluate(Context)}
     */
    //@Test
    public void testToTime() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }
}
