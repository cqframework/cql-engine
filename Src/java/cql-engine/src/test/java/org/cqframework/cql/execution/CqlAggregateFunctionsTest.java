package org.cqframework.cql.execution;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.joda.time.Partial;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;

@Test(groups = {"a"})
public class CqlAggregateFunctionsTest extends CqlExecutionTestBase {

    /**
     * {@link org.cqframework.cql.elm.execution.AllTrue#evaluate(Context)}
     */
    @Test
    public void testAllTrue() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AllTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AllTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AllTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AllTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AllTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AllTrueNullFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AllTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.AnyTrue#evaluate(Context)}
     */
    @Test
    public void testAnyTrue() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AnyTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueAllFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AnyTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueNullFirstThenTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "AnyTrueNullFirstThenFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "AnyTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Avg#evaluate(Context)}
     */
    @Test
    public void testAvg() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AvgTest1").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.0")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Count#evaluate(Context)}
     */
    @Test
    public void testCount() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CountTest1").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef(library, "CountTestDateTime").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef(library, "CountTestTime").getExpression().evaluate(context);
        assertThat(result, is(3));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Max#evaluate(Context)}
     */
    @Test
    public void testMax() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "MaxTestInteger").getExpression().evaluate(context);
        assertThat(result, is(90));

        result = context.resolveExpressionRef(library, "MaxTestString").getExpression().evaluate(context);
        assertThat(result, is("zebra"));

        result = context.resolveExpressionRef(library, "MaxTestDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 10, 6})));

        result = context.resolveExpressionRef(library, "MaxTestTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Median#evaluate(Context)}
     */
    @Test
    public void testMedian() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "MedianTestDecimal").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("3.50000000")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Min#evaluate(Context)}
     */
    @Test
    public void testMin() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "MinTestInteger").getExpression().evaluate(context);
      assertThat(result, is(0));

      result = context.resolveExpressionRef(library, "MinTestString").getExpression().evaluate(context);
      assertThat(result, is("bye"));

      result = context.resolveExpressionRef(library, "MinTestDateTime").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));

      result = context.resolveExpressionRef(library, "MinTestTime").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Mode#evaluate(Context)}
     */
    @Test
    public void testMode() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "ModeTestInteger").getExpression().evaluate(context);
      assertThat(result, is(9));

      result = context.resolveExpressionRef(library, "ModeTestDateTime").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));

      result = context.resolveExpressionRef(library, "ModeTestTime").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.StdDev#evaluate(Context)}
     */
    @Test
    public void testPopulationStdDev() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "PopStdDevTest1").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("1.41421356"))); //23730951454746218587388284504413604736328125
    }

    /**
     * {@link org.cqframework.cql.elm.execution.PopulationVariance#evaluate(Context)}
     */
    @Test
    public void testPopulationVariance() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "PopVarianceTest1").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("2")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.StdDev#evaluate(Context)}
     */
    @Test
    public void testStdDev() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "StdDevTest1").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("1.58113883"))); //00841897613935316257993690669536590576171875
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Sum#evaluate(Context)}
     */
    @Test
    public void testSum() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "SumTest1").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("20.0")));

      result = context.resolveExpressionRef(library, "SumTestNull").getExpression().evaluate(context);
      assertThat(result, is(1));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Variance#evaluate(Context)}
     */
    @Test
    public void testVariance() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "VarianceTest1").getExpression().evaluate(context);
      assertThat(result, is(new BigDecimal("2.5")));
    }
}
