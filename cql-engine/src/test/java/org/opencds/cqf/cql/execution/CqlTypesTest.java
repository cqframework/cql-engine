package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlTypesTest extends CqlExecutionTestBase {

    @Test
    public void testAny() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("AnyInteger").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("AnyDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("5.0")));

        result = context.resolveExpressionRef("AnyQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("5.0")).withUnit("g")));

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        result = context.resolveExpressionRef("AnyDateTime").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2012, 4, 4)));

        result = context.resolveExpressionRef("AnyTime").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 9, 0, 0, 0)));

        result = context.resolveExpressionRef("AnyInterval").getExpression().evaluate(context);
        Assert.assertTrue(((Interval) result).equal(new Interval(2, true, 7, true)));

        result = context.resolveExpressionRef("AnyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef("AnyTuple").getExpression().evaluate(context);
        assertThat(((Tuple)result).getElements(), is(new HashMap<String, Object>() {{put("id", 5); put("name", "Chris");}}));

        result = context.resolveExpressionRef("AnyString").getExpression().evaluate(context);
        assertThat(result, is("Chris"));
    }

    @Test
    public void testBoolean() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("BooleanTestTrue").getExpression().evaluate(context);
        assertThat(result.getClass().getSimpleName(), is("Boolean"));
        assertThat(result, is(true));

        result = context.resolveExpressionRef("BooleanTestFalse").getExpression().evaluate(context);
        assertThat(result.getClass().getSimpleName(), is("Boolean"));
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CodeEvaluator#evaluate(Context)}
     */
    @Test
    public void testCode() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("CodeLiteral").getExpression().evaluate(context);
        Assert.assertTrue(((Code) result).equal(new Code().withCode("8480-6").withSystem("http://loinc.org").withVersion("1.0").withDisplay("Systolic blood pressure")));

        result = context.resolveExpressionRef("CodeLiteral2").getExpression().evaluate(context);
        Assert.assertTrue(((Code) result).equal(new Code().withCode("1234-5").withSystem("http://example.org").withVersion("1.05").withDisplay("Test Code")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConceptEvaluator#evaluate(Context)}
     */
    @Test
    public void testConcept() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ConceptTest").getExpression().evaluate(context);
        Assert.assertTrue(((Concept) result).equal(new Concept().withCodes(Arrays.asList(new Code().withCode("8480-6").withSystem("http://loinc.org").withVersion("1.0").withDisplay("Systolic blood pressure"), new Code().withCode("1234-5").withSystem("http://example.org").withVersion("1.05").withDisplay("Test Code"))).withDisplay("Type B viral hepatitis")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.DateTimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testDateTime() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        try {
            context.resolveExpressionRef("DateTimeUpperBoundExcept").getExpression().evaluate(context);
            Assert.fail();
        }
        catch (DateTimeParseException e) {
            // pass
        }

        try {
            context.resolveExpressionRef("DateTimeLowerBoundExcept").getExpression().evaluate(context);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            // pass
        }

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        result = context.resolveExpressionRef("DateTimeProper").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2016, 7, 7, 6, 25, 33, 910)));

        result = context.resolveExpressionRef("DateTimeIncomplete").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2015, 2, 10)));

        result = context.resolveExpressionRef("DateTimeUncertain").getExpression().evaluate(context);
        Assert.assertTrue(((Interval)result).getStart().equals(19));
        Assert.assertTrue(((Interval)result).getEnd().equals(49));

        result = context.resolveExpressionRef("DateTimeMin").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 1, 1, 1, 0, 0, 0, 0)));

        result = context.resolveExpressionRef("DateTimeMax").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 9999, 12, 31, 23, 59, 59, 999)));
    }

    @Test
    public void testDecimal() throws JAXBException {
        Context context = new Context(library);
        // NOTE: these should result in compile-time decimal number is too large error, but they do not...
        Object result = context.resolveExpressionRef("DecimalUpperBoundExcept").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("10000000000000000000000000000000000.00000000")));

        result = context.resolveExpressionRef("DecimalLowerBoundExcept").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("-10000000000000000000000000000000000.00000000")));

        // NOTE: This should also return an error as the fractional precision is greater than 8
        result = context.resolveExpressionRef("DecimalFractionalTooBig").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("5.999999999")));

        result = context.resolveExpressionRef("DecimalPi").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.14159265")));
    }

    @Test
    public void testInteger() throws JAXBException {
        Context context = new Context(library);
        // NOTE: These result in compile-time integer number is too large error, which is correct
        // Object result = context.resolveExpressionRef("IntegerUpperBoundExcept").getExpression().evaluate(context);
        // assertThat(result, is(new Integer(2147483649)));
        //
        // result = context.resolveExpressionRef("IntegerLowerBoundExcept").getExpression().evaluate(context);
        // assertThat(result, is(new Integer(-2147483649)));

        Object result = context.resolveExpressionRef("IntegerProper").getExpression().evaluate(context);
        assertThat(result, is(5000));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.QuantityEvaluator#evaluate(Context)}
     */
    @Test
    public void testQuantity() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("QuantityTest").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("150.2")).withUnit("[lb_av]")));

        result = context.resolveExpressionRef("QuantityTest2").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("2.5589")).withUnit("{eskimo kisses}")));

        // NOTE: This should also return an error as the fractional precision is greater than 8
        result = context.resolveExpressionRef("QuantityFractionalTooBig").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("5.99999999")).withUnit("g")));
    }

    @Test
    public void testString() throws JAXBException {
        Context context = new Context(library);
        // NOTE: The escape characters (i.e. the backslashes) remain in the string...
        Object result = context.resolveExpressionRef("StringTestEscapeQuotes").getExpression().evaluate(context);
        assertThat(result, is("\'I start with a single quote and end with a double quote\""));

        // NOTE: This test returns "\u0048\u0069" instead of the string equivalent "Hi"
        // result = context.resolveExpressionRef("StringUnicodeTest").getExpression().evaluate(context);
        // assertThat(result, is(new String("Hi")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.TimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        Object result = context.resolveExpressionRef("TimeProper").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 10, 25, 12, 863)));

        result = context.resolveExpressionRef("TimeAllMax").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 23, 59, 59, 999)));

        result = context.resolveExpressionRef("TimeAllMin").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(offset, 0, 0, 0, 0)));
    }
}
