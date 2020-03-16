package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.AsEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.exception.InvalidCast;
import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlTypeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AsEvaluator#evaluate(Context)}
     */
    @Test
    public void testAs() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("AsQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        result = context.resolveExpressionRef("CastAsQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("45.5")).withUnit("g")));

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        result = context.resolveExpressionRef("AsDateTime").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1, 1)));

        try {
            AsEvaluator evaluator = new AsEvaluator();
            evaluator.setStrict(true);
            result = evaluator.as(1, Tuple.class);
            Assert.fail();
        }
        catch (InvalidCast e) {
            // pass
        }
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConvertEvaluator#evaluate(Context)}
     */
    @Test
    public void testConvert() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IntegerToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal(5)));

        result = context.resolveExpressionRef("IntegerToString").getExpression().evaluate(context);
        assertThat(result, is("5"));

        try {
            context.resolveExpressionRef("StringToIntegerError").getExpression().evaluate(context);
        } catch (NumberFormatException nfe) {
            assertThat(nfe.getMessage(), is("Unable to convert given string to Integer"));
        }

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        result = context.resolveExpressionRef("StringToDateTime").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1, 1)));

        result = context.resolveExpressionRef("StringToTime").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(14, 30, 0, 0)));

        try {
            context.resolveExpressionRef("StringToDateTimeMalformed").getExpression().evaluate(context);
        } catch (DateTimeParseException iae) {

        }
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConvertsToBooleanEvaluator#evaluate(Context)}
     */
    @Test
    public void testConvertsToBoolean() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ConvertsToBooleanTrue").getExpression().evaluate(context);
        Assert.assertTrue((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToBooleanFalse").getExpression().evaluate(context);
        Assert.assertFalse((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToBooleanNull").getExpression().evaluate(context);
        Assert.assertNull(result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConvertsToDateEvaluator#evaluate(Context)}
     */
    @Test
    public void testConvertsToDate() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ConvertsToDateTrue").getExpression().evaluate(context);
        Assert.assertTrue((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToDateFalse").getExpression().evaluate(context);
        Assert.assertFalse((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToDateNull").getExpression().evaluate(context);
        Assert.assertNull(result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConvertsToDateTimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testConvertsToDateTime() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ConvertsToDateTimeTrue").getExpression().evaluate(context);
        Assert.assertTrue((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToDateTimeFalse").getExpression().evaluate(context);
        Assert.assertFalse((Boolean) result);

        result = context.resolveExpressionRef("ConvertsToDateTimeNull").getExpression().evaluate(context);
        Assert.assertNull(result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IsEvaluator#evaluate(Context)}
     */
    @Test
    public void testIs() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IntegerIsInteger").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("StringIsInteger").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToBooleanEvaluator#evaluate(Context)}
     */
    @Test
    public void testToBoolean() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("StringNoToBoolean").getExpression().evaluate(context);
        assertThat(result, is(false));

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToConceptEvaluator#evaluate(Context)}
     */
    @Test
    public void testToConcept() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("CodeToConcept1").getExpression().evaluate(context);
        Assert.assertTrue(((Concept) result).equivalent(new Concept().withCode(new Code().withCode("8480-6"))));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToDateTimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testToDateTime() {
        // TODO: Fix timezone tests
        Context context = new Context(library);

        BigDecimal offset = TemporalHelper.getDefaultOffset();
        Object result = context.resolveExpressionRef("ToDateTime0").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1)));

        result = context.resolveExpressionRef("ToDateTime1").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1, 1)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime2").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1, 1, 12, 5)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime3").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2014, 1, 1, 12, 5, 5, 955)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTime4").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(new BigDecimal("1.5"), 2014, 1, 1, 12, 5, 5, 955)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("1.5")));

        result = context.resolveExpressionRef("ToDateTime5").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(new BigDecimal("-1.25"), 2014, 1, 1, 12, 5, 5, 955)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-1.25")));

        result = context.resolveExpressionRef("ToDateTime6").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(new BigDecimal(0), 2014, 1, 1, 12, 5, 5, 955)));
        // assertThat(((DateTime)result).getTimezoneOffset(), is(new BigDecimal("-7")));

        result = context.resolveExpressionRef("ToDateTimeMalformed").getExpression().evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToDecimalEvaluator#evaluate(Context)}
     */
    @Test
    public void testToDecimal() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("String25D5ToDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("25.5")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToIntegerEvaluator#evaluate(Context)}
     */
    @Test
    public void testToInteger() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("StringNeg25ToInteger").getExpression().evaluate(context);
        assertThat(result, is(-25));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToQuantityEvaluator#evaluate(Context)}
     */
    @Test
    public void testToQuantity() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("String5D5CMToQuantity").getExpression().evaluate(context);
        Assert.assertTrue(((Quantity) result).equal(new Quantity().withValue(new BigDecimal("5.5")).withUnit("cm")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToRatioEvaluator#evaluate(Context)}
     */
    @Test
    public void testToRatio() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ToRatioIsValid").getExpression().evaluate(context);
        Assert.assertTrue(((Ratio) result).getNumerator().equal(new Quantity().withValue(new BigDecimal("1.0")).withUnit("mg")));
        Assert.assertTrue(((Ratio) result).getDenominator().equal(new Quantity().withValue(new BigDecimal("2.0")).withUnit("mg")));

        result = context.resolveExpressionRef("ToRatioIsNull").getExpression().evaluate(context);
        Assert.assertNull(result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToStringEvaluator#evaluate(Context)}
     */
    @Test
    public void testToString() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IntegerNeg5ToString").getExpression().evaluate(context);
        assertThat(result, is("-5"));

        result = context.resolveExpressionRef("Decimal18D55ToString").getExpression().evaluate(context);
        assertThat(result, is("18.55"));

        result = context.resolveExpressionRef("Quantity5D5CMToString").getExpression().evaluate(context);
        assertThat(result, is("5.5 'cm'"));

        result = context.resolveExpressionRef("BooleanTrueToString").getExpression().evaluate(context);
        assertThat(result, is("true"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ToTimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testToTime() {
        Context context = new Context(library);
        
        Object result = context.resolveExpressionRef("ToTime1").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(result, new Time(14, 30, 0, 0)));

        result = context.resolveExpressionRef("ToTimeMalformed").getExpression().evaluate(context);
        Assert.assertNull(result);
    }
}
