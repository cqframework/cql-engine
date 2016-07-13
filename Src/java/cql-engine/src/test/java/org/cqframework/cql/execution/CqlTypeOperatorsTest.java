package org.cqframework.cql.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.runtime.Concept;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;

import org.joda.time.Partial;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

public class CqlTypeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.cqframework.cql.elm.execution.As#evaluate(Context)}
     */
    @Test
    public void testAs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AsQuantity").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        result = context.resolveExpressionRef(library, "CastAsQuantity").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        result = context.resolveExpressionRef(library, "AsDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Convert#evaluate(Context)}
     */
    @Test
    public void testConvert() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "IntegerToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal(5)));

        result = context.resolveExpressionRef(library, "IntegerToString").getExpression().evaluate(context);
        assertThat(result, is("5"));

        try {
          result = context.resolveExpressionRef(library, "StringToIntegerError").getExpression().evaluate(context);
        } catch (NumberFormatException nfe) {
          assertThat(nfe.getMessage(), is("Unable to convert given string to Integer"));
        }

        result = context.resolveExpressionRef(library, "StringToDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));

        result = context.resolveExpressionRef(library, "StringToTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));

        try {
          result = context.resolveExpressionRef(library, "StringToDateTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"2014/01/01\" is malformed at \"/01/01\""));
        }
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Is#evaluate(Context)}
     */
    @Test
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
    @Test
    public void testToBoolean() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "StringNoToBoolean").getExpression().evaluate(context);
        assertThat(result, is(false));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToConcept#evaluate(Context)}
     */
    @Test
    public void testToConcept() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CodeToConcept1").getExpression().evaluate(context);
        assertThat(result, is(new Concept().withCode(new Code().withCode("8480-6").withSystem("http://loinc.org").withDisplay("Systolic blood pressure"))));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToDateTime#evaluate(Context)}
     */
    @Test
    public void testToDateTime() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "ToDateTime1").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef(library, "ToDateTime2").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2014, 1, 1, 12, 5})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef(library, "ToDateTime3").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef(library, "ToDateTime4").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("1.5")));

        result = context.resolveExpressionRef(library, "ToDateTime5").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-1.25")));

        result = context.resolveExpressionRef(library, "ToDateTime6").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        try {
          result = context.resolveExpressionRef(library, "ToDateTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"2014/01/01T12:05:05.955\" is malformed at \"/01/01T12:05:05.955\""));
        }

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToDecimal#evaluate(Context)}
     */
    @Test
    public void testToDecimal() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "String25D5ToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("25.5")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToInteger#evaluate(Context)}
     */
    @Test
    public void testToInteger() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "StringNeg25ToInteger").getExpression().evaluate(context);
        assertThat(result, is(-25));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToQuantity#evaluate(Context)}
     */
    @Test
    public void testToQuantity() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "String5D5CMToQuantity").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("5.5")).withUnit("cm")));

    }

    /**
     * {@link org.cqframework.cql.elm.execution.ToString#evaluate(Context)}
     */
    @Test
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
    @Test
    public void testToTime() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "ToTime1").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef(library, "ToTime2").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("5.5")));

        result = context.resolveExpressionRef(library, "ToTime3").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-5.75")));

        result = context.resolveExpressionRef(library, "ToTime4").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        try {
          result = context.resolveExpressionRef(library, "ToTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"T14-30-00.0\" is malformed at \"-30-00.0\""));
        }
    }
}
