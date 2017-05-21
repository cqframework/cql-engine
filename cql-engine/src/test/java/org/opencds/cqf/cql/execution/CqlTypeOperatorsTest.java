package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;
import org.joda.time.Partial;
import org.testng.Assert;
import org.testng.annotations.Test;
import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlTypeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.As#evaluate(Context)}
     */
    @Test
    public void testAs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("AsQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        result = context.resolveExpressionRef("CastAsQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        result = context.resolveExpressionRef("AsDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Convert#evaluate(Context)}
     */
    @Test
    public void testConvert() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("IntegerToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal(5)));

        result = context.resolveExpressionRef("IntegerToString").getExpression().evaluate(context);
        assertThat(result, is("5"));

        try {
          result = context.resolveExpressionRef("StringToIntegerError").getExpression().evaluate(context);
        } catch (NumberFormatException nfe) {
          assertThat(nfe.getMessage(), is("Unable to convert given string to Integer"));
        }

        result = context.resolveExpressionRef("StringToDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));

        result = context.resolveExpressionRef("StringToTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));

        try {
          result = context.resolveExpressionRef("StringToDateTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"2014/01/01\" is malformed at \"/01/01\""));
        }
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Is#evaluate(Context)}
     */
    @Test
    public void testIs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("IntegerIsInteger").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("StringIsInteger").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToBoolean#evaluate(Context)}
     */
    @Test
    public void testToBoolean() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("StringNoToBoolean").getExpression().evaluate(context);
        assertThat(result, is(false));

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToConcept#evaluate(Context)}
     */
    @Test
    public void testToConcept() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CodeToConcept1").getExpression().evaluate(context);
        Assert.assertTrue(((Concept) result).equal(new Concept().withCode(new Code().withCode("8480-6").withSystem("http://loinc.org").withDisplay("Systolic blood pressure"))));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToDateTime#evaluate(Context)}
     */
    @Test
    public void testToDateTime() throws JAXBException {
      // TODO: Fix timezone tests
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("ToDateTime1").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 1, 1})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime2").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2014, 1, 1, 12, 5})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime3").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime4").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("1.5")));

        result = context.resolveExpressionRef("ToDateTime5").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-1.25")));

        result = context.resolveExpressionRef("ToDateTime6").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2014, 1, 1, 12, 5, 5, 955})));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        try {
          result = context.resolveExpressionRef("ToDateTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"2014/01/01T12:05:05.955\" is malformed at \"/01/01T12:05:05.955\""));
        }

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToDecimal#evaluate(Context)}
     */
    @Test
    public void testToDecimal() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("String25D5ToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("25.5")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToInteger#evaluate(Context)}
     */
    @Test
    public void testToInteger() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("StringNeg25ToInteger").getExpression().evaluate(context);
        assertThat(result, is(-25));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToQuantity#evaluate(Context)}
     */
    @Test
    public void testToQuantity() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("String5D5CMToQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("5.5")).withUnit("cm")));

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToString#evaluate(Context)}
     */
    @Test
    public void testToString() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("IntegerNeg5ToString").getExpression().evaluate(context);
        assertThat(result, is("-5"));

        result = context.resolveExpressionRef("Decimal18D55ToString").getExpression().evaluate(context);
        assertThat(result, is("18.55"));

        result = context.resolveExpressionRef("Quantity5D5CMToString").getExpression().evaluate(context);
        assertThat(result, is("5.5cm"));

        result = context.resolveExpressionRef("BooleanTrueToString").getExpression().evaluate(context);
        assertThat(result, is("true"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToTime#evaluate(Context)}
     */
    @Test
    public void testToTime() throws JAXBException {
      // TODO: fix timezone tests
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("ToTime1").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        // assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToTime2").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        // assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("5.5")));

        result = context.resolveExpressionRef("ToTime3").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        // assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-5.75")));

        result = context.resolveExpressionRef("ToTime4").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {14, 30, 0, 0})));
        // assertThat(((Time)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        try {
          result = context.resolveExpressionRef("ToTimeMalformed").getExpression().evaluate(context);
        } catch (IllegalArgumentException iae) {
          assertThat(iae.getMessage(), is("Invalid format: \"T14-30-00.0\" is malformed at \"-30-00.0\""));
        }
    }
}
