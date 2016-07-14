package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlComparisonOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testBetween() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "BetweenIntTrue").getExpression().evaluate(context);
      assertThat(result, is(true));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equal#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "SimpleEqTrueTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleEqTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleEqFalseFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleEqFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleEqNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        // result = context.resolveExpressionRef(library, "SimpleEqTrueNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleEqNullTrue").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleEqInt1Int1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleEqInt1Int2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleEqStringAStringA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleEqStringAStringB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleEqFloat1Float1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleEqFloat1Float2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleEqFloat1Int1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleEqFloat1Int2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "QuantityEqCM1CM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        // TODO: Quantity unit comparison is not implemented yet
        // result = context.resolveExpressionRef(library, "QuantityEqCM1M01").getExpression().evaluate(context);
        // assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleEqJohnJohn").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleEqJohnJane").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TupleEqJohn1John2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TupleEqDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleEqDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TupleEqDateTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "TupleEqTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleEqTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ListEqEmptyEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ListEqABCABC").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ListEqABCAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ListEqABC123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "ListEq123ABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "ListEq123String123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntervalEq1To101To10").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntervalEq1To101To5").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeEqTodayToday").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeEqJanJan").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeEqJanJuly").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeEqMissingArg").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeEqNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeEqNull2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeUTC").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeEqTodayYesterday").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeEq10A10A").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeEq10A10P").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Greater#evaluate(Context)}
     */
    @Test
    public void testGreater() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "GreaterZZ").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterZ1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterZNeg1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterDecZZ").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterDecZ1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterDecZNeg1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterCM0CM0").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterCM0CM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterCM0NegCM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterM1CM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterM1CM10").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterAA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterBA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterAThanAA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterAAThanA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterJackJill").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeGreaterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeGreaterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeGreaterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeGreaterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.GreaterOrEqual#evaluate(Context)}
     */
    @Test
    public void testGreaterOrEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "GreaterOrEqualZZ").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualZ1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualZNeg1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualDecZZ").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualDecZ1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualDecZNeg1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualCM0CM0").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualCM0CM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualCM0NegCM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        // TODO: Quantity unit comparison is not implemented yet
        // result = context.resolveExpressionRef(library, "GreaterOrEqualM1CM1").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef(library, "GreaterOrEqualM1CM10").getExpression().evaluate(context);
        // assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualAA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualBA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualAThanAA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "GreaterOrEqualAAThanA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "GreaterOrEqualJackJill").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeGreaterEqTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeGreaterEqTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeGreaterEqFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeGreaterEqTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeGreaterEqTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeGreaterEqFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Less#evaluate(Context)}
     */
    @Test
    public void testLess() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "LessZZ").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessZ1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessZNeg1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessDecZZ").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessDecZ1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessDecZNeg1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessCM0CM0").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessCM0CM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessCM0NegCM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        // TODO: Quantity unit comparison is not implemented yet
        // result = context.resolveExpressionRef(library, "LessM1CM1").getExpression().evaluate(context);
        // assertThat(result, is(false));

        // result = context.resolveExpressionRef(library, "LessM1CM10").getExpression().evaluate(context);
        // assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessAA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessAB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessBA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessAThanAA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessAAThanA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessJackJill").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeLessTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeLessFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeLessTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeLessFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.LessOrEqual#evaluate(Context)}
     */
    @Test
    public void testLessOrEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "LessOrEqualZZ").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualZ1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualZNeg1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessOrEqualDecZZ").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualDecZ1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualDecZNeg1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessOrEqualCM0CM0").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualCM0CM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualCM0NegCM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        // TODO: uncomment once quantity unit comparison is implemented
        // result = context.resolveExpressionRef(library, "LessOrEqualM1CM1").getExpression().evaluate(context);
        // assertThat(result, is(false));
        //
        // result = context.resolveExpressionRef(library, "LessOrEqualM1CM10").getExpression().evaluate(context);
        // assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessOrEqualAA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualAB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualBA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessOrEqualAThanAA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "LessOrEqualAAThanA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "LessOrEqualJackJill").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeLessEqTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeLessEqTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeLessEqFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeLessEqTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeLessEqTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeLessEqFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equivalent#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "EquivTrueTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivFalseFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivNullNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        // result = context.resolveExpressionRef(library, "EquivTrueNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivNullTrue").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivInt1Int1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivInt1Int2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivStringAStringA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivStringAStringB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivFloat1Float1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivFloat1Float2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivFloat1Int1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivFloat1Int2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivEqCM1CM1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivEqCM1M01").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivTupleJohnJohn").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivTupleJohnJane").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivTupleJohn1John2").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivListEmptyEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivListABCABC").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivListABCAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivListABC123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivList123ABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivList123String123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivInterval1To101To10").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivInterval1To101To5").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivDateTimeTodayToday").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivDateTimeTodayYesterday").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivTime10A10A").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivTime10A10P").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.NotEqual#evaluate(Context)}
     */
    @Test
    public void testNotEqual() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "SimpleNotEqTrueTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleNotEqTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleNotEqFalseFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleNotEqFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleNotEqNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        // TODO: This results in a translation error - don't know why, but null often causes errors
        // result = context.resolveExpressionRef(library, "SimpleNotEqTrueNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleNotEqNullTrue").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleNotEqInt1Int1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleNotEqInt1Int2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleNotEqStringAStringA").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleNotEqStringAStringB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleNotEqFloat1Float1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "SimpleNotEqFloat1Float2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "SimpleNotEqFloat1Int1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SimpleNotEqFloat1Int2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "QuantityNotEqCM1CM1").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityNotEqCM1M01").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleNotEqJohnJohn").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TupleNotEqJohnJane").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TupleNotEqJohn1John2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ListNotEqEmptyEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ListNotEqABCABC").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ListNotEqABCAB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ListNotEqABC123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "ListNotEq123ABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "ListNotEq123String123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntervalNotEq1To101To10").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IntervalNotEq1To101To5").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeNotEqTodayToday").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeNotEqTodayYesterday").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeNotEq10A10A").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeNotEq10A10P").getExpression().evaluate(context);
        assertThat(result, is(true));
    }
}
