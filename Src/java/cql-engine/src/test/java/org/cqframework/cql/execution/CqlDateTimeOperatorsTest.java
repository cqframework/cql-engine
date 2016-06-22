package org.cqframework.cql.execution;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import org.cqframework.cql.runtime.DateTime;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

public class CqlDateTimeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.cqframework.cql.elm.execution.Add#evaluate(Context)}
     */
    @Test
    public void testAdd() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeAdd5Years").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2010, 10, 10})));

      try {
        result = context.resolveExpressionRef(library, "DateTimeAddInvalidYears").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("The date time addition results in a year greater than the accepted range."));
      }

      result = context.resolveExpressionRef(library, "DateTimeAdd5Months").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2005, 10, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMonthsOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2006, 3, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Days").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 15})));

      result = context.resolveExpressionRef(library, "DateTimeAddDaysOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2016, 7, 1})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Hours").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddHoursOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 11, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Minutes").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMinutesOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Seconds").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddSecondsOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Milliseconds").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMillisecondsOverflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAddLeapYear").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2013, 2, 28})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.After#evaluate(Context)}
     */
    @Test
    public void testAfter() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeAfterYearTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterYearFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterMonthTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterMonthFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterDayTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterDayFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterHourTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterHourFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterMinuteTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterMinuteFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterSecondTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterSecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeAfterMillisecondTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeAfterMillisecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));
  }

    /**
     * {@link org.cqframework.cql.elm.execution.Before#evaluate(Context)}
     */
    @Test
    public void testBefore() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeBeforeYearTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMonthTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeDayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeBeforeMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.DateTime#evaluate(Context)}
     */
    @Test
    public void testDateTime() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeYear").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2003})));

        result = context.resolveExpressionRef(library, "DateTimeMonth").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(2), new int[] {2003, 10})));

        result = context.resolveExpressionRef(library, "DateTimeDay").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2003, 10, 29})));

        result = context.resolveExpressionRef(library, "DateTimeHour").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2003, 10, 29, 20})));

        result = context.resolveExpressionRef(library, "DateTimeMinute").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2003, 10, 29, 20, 50})));

        result = context.resolveExpressionRef(library, "DateTimeSecond").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2003, 10, 29, 20, 50, 33})));

        result = context.resolveExpressionRef(library, "DateTimeMillisecond").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2003, 10, 29, 20, 50, 33, 955})));
    }

    @Test
    public void testDateTimeComponentFrom() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeComponentFromYear").getExpression().evaluate(context);
        assertThat(result, is(2003));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromMonth").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromDay").getExpression().evaluate(context);
        assertThat(result, is(29));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromHour").getExpression().evaluate(context);
        assertThat(result, is(20));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromMinute").getExpression().evaluate(context);
        assertThat(result, is(50));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromSecond").getExpression().evaluate(context);
        assertThat(result, is(33));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromMillisecond").getExpression().evaluate(context);
        assertThat(result, is(955));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromTimezone").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal(1)));

        result = context.resolveExpressionRef(library, "DateTimeComponentFromDate").getExpression().evaluate(context);
        assertThat(result, is(new Partial(DateTime.getFields(7), new int[] {2003, 10, 29, 20, 50, 33, 955})));
    }

    @Test
    public void testDifference() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeDifferenceYear").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceMonth").getExpression().evaluate(context);
        assertThat(result, is(8));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceDay").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceHour").getExpression().evaluate(context);
        assertThat(result, is(8));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceMinute").getExpression().evaluate(context);
        assertThat(result, is(9));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceSecond").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceMillisecond").getExpression().evaluate(context);
        assertThat(result, is(400));

        result = context.resolveExpressionRef(library, "DateTimeDifferenceNegative").getExpression().evaluate(context);
        assertThat(result, is(-18));
    }

    @Test
    public void testDuration() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeDurationBetweenYear").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenYearOffset").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonth").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain3").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Now#evaluate(Context)}
     */
    @Test
    public void testNow() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameAs#evaluate(Context)}
     */
    @Test
    public void testSameAs() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameOrAfter#evaluate(Context)}
     */
    @Test
    public void testSameOrAfter() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameOrBefore#evaluate(Context)}
     */
    @Test
    public void testSameOrBefore() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Subtract#evaluate(Context)}
     */
    @Test
    public void testSubtract() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeSubtract5Years").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2000, 10, 10})));

      try {
        result = context.resolveExpressionRef(library, "DateTimeSubtractInvalidYears").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("The date time addition results in a year less than the accepted range."));
      }

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Months").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2005, 1, 10})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMonthsUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2004, 11, 10})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Days").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractDaysUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(3), new int[] {2016, 5, 30})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Hours").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractHoursUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 9, 23})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Minutes").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMinutesUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 4, 59})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Seconds").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractSecondsUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 4, 59})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Milliseconds").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMillisecondsUnderflow").getExpression().evaluate(context);
      assertThat(result, is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Time#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.TimeOfDay#evaluate(Context)}
     */
    @Test
    public void testTimeOfDay() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Today#evaluate(Context)}
     */
    @Test
    public void testToday() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

}
