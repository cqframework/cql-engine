package org.cqframework.cql.execution;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Uncertainty;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.comparesEqualTo;

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
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2010, 10, 10})));

      try {
        result = context.resolveExpressionRef(library, "DateTimeAddInvalidYears").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("The date time addition results in a year greater than the accepted range."));
      }

      result = context.resolveExpressionRef(library, "DateTimeAdd5Months").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 10, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMonthsOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2006, 3, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Days").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 15})));

      result = context.resolveExpressionRef(library, "DateTimeAddDaysOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2016, 7, 1})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Hours").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddHoursOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 11, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Minutes").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMinutesOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Seconds").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddSecondsOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAdd5Milliseconds").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 10})));

      result = context.resolveExpressionRef(library, "DateTimeAddMillisecondsOverflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 6, 0})));

      result = context.resolveExpressionRef(library, "DateTimeAddLeapYear").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2013, 2, 28})));

      result = context.resolveExpressionRef(library, "DateTimeAdd2YearsByMonths").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));

      result = context.resolveExpressionRef(library, "DateTimeAdd2YearsByDays").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));

      result = context.resolveExpressionRef(library, "DateTimeAdd2YearsByDaysRem5Days").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));

      result = context.resolveExpressionRef(library, "TimeAdd5Hours").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeAdd1Minute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeAdd1Second").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 0, 999})));

      result = context.resolveExpressionRef(library, "TimeAdd1Millisecond").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 0, 0})));

      result = context.resolveExpressionRef(library, "TimeAdd5Hours1Minute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {21, 0, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeAdd5hoursByMinute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
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

      result = context.resolveExpressionRef(library, "DateTimeAfterUncertain").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeAfterHourTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeAfterHourFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeAfterMinuteTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeAfterMinuteFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeAfterSecondTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeAfterSecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeAfterMillisecondTrue").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeAfterMillisecondFalse").getExpression().evaluate(context);
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

        result = context.resolveExpressionRef(library, "TimeBeforeHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeBeforeMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeBeforeSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeBeforeMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeBeforeMillisecondFalse").getExpression().evaluate(context);
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
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2003, 10, 29, 20, 50, 33, 955})));

        result = context.resolveExpressionRef(library, "TimeComponentFromHour").getExpression().evaluate(context);
        assertThat(result, is(23));

        result = context.resolveExpressionRef(library, "TimeComponentFromMinute").getExpression().evaluate(context);
        assertThat(result, is(20));

        result = context.resolveExpressionRef(library, "TimeComponentFromSecond").getExpression().evaluate(context);
        assertThat(result, is(15));

        result = context.resolveExpressionRef(library, "TimeComponentFromMilli").getExpression().evaluate(context);
        assertThat(result, is(555));
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

        result = context.resolveExpressionRef(library, "DateTimeDifferenceUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeDifferenceHour").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef(library, "TimeDifferenceMinute").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "TimeDifferenceSecond").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef(library, "TimeDifferenceMillis").getExpression().evaluate(context);
        assertThat(result, is(-5));
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

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenDaysDiffYears").getExpression().evaluate(context);
        assertThat(result, is(-788));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainInterval").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(17, true, 44, true)));

        // result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainInterval2").getExpression().evaluate(context);
        // assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(5, true, 17, true)));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainAdd").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(34, true, 88, true)));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainSubtract").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(12, true, 28, true)));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainMultiply").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(289, true, 1936, true)));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenUncertainDiv").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(3, true, 2, true)));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain3").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain4").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain5").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain6").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeDurationBetweenMonthUncertain7").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeDurationBetweenHour").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "TimeDurationBetweenMinute").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef(library, "TimeDurationBetweenSecond").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef(library, "TimeDurationBetweenMillis").getExpression().evaluate(context);
        assertThat(result, is(5));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Now#evaluate(Context)}
     */
    @Test
    public void testNow() throws JAXBException {
        Context context = new Context(library);

        // TODO: the result is inconsistent -- sometimes true, sometimes not -- fix evaluator
        // Object result = context.resolveExpressionRef(library, "DateTimeNow").getExpression().evaluate(context);
        // assertThat(result, is(true));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameAs#evaluate(Context)}
     */
    @Test
    public void testSameAs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeSameAsYearTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMonthTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsDayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameAsMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameAsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "TimeSameAsHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeSameAsHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeSameAsMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeSameAsMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeSameAsSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeSameAsSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "TimeSameAsMillisTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "TimeSameAsMillisFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameOrAfter#evaluate(Context)}
     */
    @Test
    public void testSameOrAfter() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeSameOrAfterYearTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterYearTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterYearFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMonthTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMonthTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMonthFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterDayTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterDayTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterDayFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterHourTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterHourTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterHourFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMinuteTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMinuteTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMinuteFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterSecondTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterSecondTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterSecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMillisecondTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMillisecondTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterMillisecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "DateTimeSameOrAfterNull1").getExpression().evaluate(context);
      assertThat(result, is(nullValue()));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterHourTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterHourTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterHourFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMinuteTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMinuteTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMinuteFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterSecondTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterSecondTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterSecondFalse").getExpression().evaluate(context);
      assertThat(result, is(false));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMillisTrue1").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMillisTrue2").getExpression().evaluate(context);
      assertThat(result, is(true));

      result = context.resolveExpressionRef(library, "TimeSameOrAfterMillisFalse").getExpression().evaluate(context);
      assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SameOrBefore#evaluate(Context)}
     */
    @Test
    public void testSameOrBefore() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMonthTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMonthTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeDayTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeDayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeHourTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeHourTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMinuteTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMinuteTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeSecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeSecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMillisecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMillisecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Subtract#evaluate(Context)}
     */
    @Test
    public void testSubtract() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "DateTimeSubtract5Years").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2000, 10, 10})));

      try {
        result = context.resolveExpressionRef(library, "DateTimeSubtractInvalidYears").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("The date time addition results in a year less than the accepted range."));
      }

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Months").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 1, 10})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMonthsUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2004, 11, 10})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Days").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractDaysUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2016, 5, 30})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Hours").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractHoursUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 9, 23})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Minutes").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMinutesUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 4, 59})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Seconds").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractSecondsUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 4, 59})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract5Milliseconds").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 5})));

      result = context.resolveExpressionRef(library, "DateTimeSubtractMillisecondsUnderflow").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract2YearsAsMonths").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2012})));

      result = context.resolveExpressionRef(library, "DateTimeSubtract2YearsAsMonthsRem1").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2011})));

      result = context.resolveExpressionRef(library, "TimeSubtract5Hours").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeSubtract1Minute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 58, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeSubtract1Second").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 58, 999})));

      result = context.resolveExpressionRef(library, "TimeSubtract1Millisecond").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 58, 999})));

      result = context.resolveExpressionRef(library, "TimeSubtract5Hours1Minute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 58, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeSubtract5hoursByMinute").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Time#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "TimeTest2").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {23, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.TimeOfDay#evaluate(Context)}
     */
    @Test
    public void testTimeOfDay() throws JAXBException {
        Context context = new Context(library);
        // TODO: uncomment once Time(x,x,x,x,x) format is fixed
        // Object result = context.resolveExpressionRef(library, "TimeOfDayTest").getExpression().evaluate(context);
        // assertThat(((Time)result).getPartial().getValue(0), is(10));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Today#evaluate(Context)}
     */
    @Test
    public void testToday() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeTodayTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeTodayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DateTimeSameOrBeforeTodayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DateTimeAddTodayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

}
