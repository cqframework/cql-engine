package org.cqframework.cql.execution;

import org.joda.time.Partial;
import org.joda.time.DateTimeFieldType;

import org.cqframework.cql.runtime.DateTime;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        Object result;
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
    public void testDateTimeComponentFrom() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testDifference() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testDuration() throws JAXBException {
        Context context = new Context(library);
        Object result;
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
