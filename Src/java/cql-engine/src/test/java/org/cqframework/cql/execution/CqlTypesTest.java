package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.runtime.Concept;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.cqframework.cql.runtime.Uncertainty;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Tuple;
import java.math.BigDecimal;

import org.joda.time.Partial;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.comparesEqualTo;

public class CqlTypesTest extends CqlExecutionTestBase {

    @Test
    public void testAny() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AnyInteger").getExpression().evaluate(context);
        assertThat(result, is(new Integer(5)));

        result = context.resolveExpressionRef(library, "AnyDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("5.0")));

        result = context.resolveExpressionRef(library, "AnyQuantity").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("5.0")).withUnit("g")));

        result = context.resolveExpressionRef(library, "AnyDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 4, 4})));

        result = context.resolveExpressionRef(library, "AnyTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {9, 0, 0, 0})));

        result = context.resolveExpressionRef(library, "AnyInterval").getExpression().evaluate(context);
        assertThat(result, is(new Interval(2, true, 7, true)));

        result = context.resolveExpressionRef(library, "AnyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef(library, "AnyTuple").getExpression().evaluate(context);
        assertThat(((Tuple)result).getElements(), is(new HashMap<String, Object>() {{put("id", 5); put("name", "Chris");}}));

        result = context.resolveExpressionRef(library, "AnyString").getExpression().evaluate(context);
        assertThat(result, is("Chris"));
    }

    @Test
    public void testBoolean() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "BooleanTestTrue").getExpression().evaluate(context);
        assertThat(result.getClass().getSimpleName(), is("Boolean"));
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "BooleanTestFalse").getExpression().evaluate(context);
        assertThat(result.getClass().getSimpleName(), is("Boolean"));
        assertThat(result, is(false));
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
      Object result = context.resolveExpressionRef(library, "ConceptTest").getExpression().evaluate(context);
      assertThat(result, is(new Concept().withCodes(Arrays.asList(new Code().withCode("66071002").withSystem("http://loinc.org"), new Code().withCode("B18.1").withSystem("http://loinc.org"))).withDisplay("Type B viral hepatitis")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.DateTime#evaluate(Context)}
     */
    @Test
    public void testDateTime() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeNull").getExpression().evaluate(context);
      assertThat(result, is(nullValue()));

      try {
        result = context.resolveExpressionRef(library, "DateTimeUpperBoundExcept").getExpression().evaluate(context);
      } catch (IllegalArgumentException e) {
        assertThat(e.getMessage(), is("The year: 10000 falls above the accepted bounds of 0001-9999."));
      }

      try {
        result = context.resolveExpressionRef(library, "DateTimeLowerBoundExcept").getExpression().evaluate(context);
      } catch (IllegalArgumentException e) {
        assertThat(e.getMessage(), is("The year: 0 falls below the accepted bounds of 0001-9999."));
      }

      result = context.resolveExpressionRef(library, "DateTimeProper").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 7, 7, 6, 25, 33, 910})));

      result = context.resolveExpressionRef(library, "DateTimeIncomplete").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2015, 2, 10})));

      result = context.resolveExpressionRef(library, "DateTimeUncertain").getExpression().evaluate(context);
      assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(19, true, 49, true)));

      result = context.resolveExpressionRef(library, "DateTimeMin").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {0001, 1, 1, 0, 0, 0, 0})));

      result = context.resolveExpressionRef(library, "DateTimeMax").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {9999, 12, 31, 23, 59, 59, 999})));
    }

    @Test
    public void testDecimal() throws JAXBException {
        Context context = new Context(library);
        // NOTE: these should result in compile-time decimal number is too large error, but they do not...
        Object result = context.resolveExpressionRef(library, "DecimalUpperBoundExcept").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("10000000000000000000000000000000000.00000000")));

        result = context.resolveExpressionRef(library, "DecimalLowerBoundExcept").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("-10000000000000000000000000000000000.00000000")));

        // NOTE: This should also return an error as the fractional precision is greater than 8
        result = context.resolveExpressionRef(library, "DecimalFractionalTooBig").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("5.999999999")));

        result = context.resolveExpressionRef(library, "DecimalPi").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.14159265")));
    }

    @Test
    public void testInteger() throws JAXBException {
        Context context = new Context(library);
        // NOTE: These result in compile-time integer number is too large error, which is correct
        // Object result = context.resolveExpressionRef(library, "IntegerUpperBoundExcept").getExpression().evaluate(context);
        // assertThat(result, is(new Integer(2147483649)));
        //
        // result = context.resolveExpressionRef(library, "IntegerLowerBoundExcept").getExpression().evaluate(context);
        // assertThat(result, is(new Integer(-2147483649)));

        Object result = context.resolveExpressionRef(library, "IntegerProper").getExpression().evaluate(context);
        assertThat(result, is(new Integer(5000)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Quantity#evaluate(Context)}
     */
    @Test
    public void testQuantity() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "QuantityTest").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("150.2")).withUnit("lbs")));

        result = context.resolveExpressionRef(library, "QuantityTest2").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("2.5589")).withUnit("eskimo kisses")));

        // NOTE: This should also return an error as the fractional precision is greater than 8
        result = context.resolveExpressionRef(library, "QuantityFractionalTooBig").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("5.999999999")).withUnit("g")));
    }

    @Test
    public void testString() throws JAXBException {
        Context context = new Context(library);
        // NOTE: The escape characters (i.e. the backslashes) remain in the string...
        Object result = context.resolveExpressionRef(library, "StringTestEscapeQuotes").getExpression().evaluate(context);
        assertThat(result, is("\\'I start with a single quote and end with a double quote\\\""));

        // NOTE: This test returns "\u0048\u0069" instead of the string equivalent "Hi"
        // result = context.resolveExpressionRef(library, "StringUnicodeTest").getExpression().evaluate(context);
        // assertThat(result, is(new String("Hi")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Time#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "TimeProper").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 25, 12, 863})));

        result = context.resolveExpressionRef(library, "TimeAllMax").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {23, 59, 59, 999})));

        result = context.resolveExpressionRef(library, "TimeAllMin").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {0, 0, 0, 0})));
    }
}
