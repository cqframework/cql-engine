package org.cqframework.cql.execution;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.instanceOf;

import org.joda.time.Partial;

public class CqlArithmeticFunctionsTest extends CqlExecutionTestBase {
    /**
     * {@link org.cqframework.cql.elm.execution.Abs#evaluate(Context)}
     */
    @Test
    public void testAbs() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AbsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Abs0").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "AbsNeg1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "AbsNeg1Dec").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "Abs0Dec").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.0)));

        result = context.resolveExpressionRef(library, "Abs1cm").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("1.0")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Add#evaluate(Context)}
     */
    @Test
    public void testAdd() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "AddNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Add11").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "Add1D1D").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("2.0")));

        result = context.resolveExpressionRef(library, "Add1Q1Q").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("2.0")).withUnit("g/cm3")));

        result = context.resolveExpressionRef(library, "AddIAndD").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.0")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Ceiling#evaluate(Context)}
     */
    @Test
    public void testCeiling() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "CeilingNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Ceiling1D").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Ceiling1D1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "CeilingNegD1").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "CeilingNeg1").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "CeilingNeg1D1").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "Ceiling1I").getExpression().evaluate(context);
        assertThat(result, is(1));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Divide#evaluate(Context)}
     */
    @Test
    public void testDivide() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "DivideNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Divide10").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Divide01").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("0.0")));

        result = context.resolveExpressionRef(library, "Divide11").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("1.0")));

        result = context.resolveExpressionRef(library, "Divide1d1d").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("1.0")));

        result = context.resolveExpressionRef(library, "Divide103").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("3.33333333")));

        result = context.resolveExpressionRef(library, "Divide1Q1").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g/cm3")));

        result = context.resolveExpressionRef(library, "Divide1Q1Q").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g/cm3")));

        result = context.resolveExpressionRef(library, "Divide10I5D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("2.0")));

        result = context.resolveExpressionRef(library, "Divide10I5I").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("2.0")));

        result = context.resolveExpressionRef(library, "Divide10Q5I").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("2.0")).withUnit("g")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Floor#evaluate(Context)}
     */
    @Test
    public void testFloor() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "FloorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Floor1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Floor1D").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Floor1D1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "FloorNegD1").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "FloorNeg1").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "FloorNeg1D1").getExpression().evaluate(context);
        assertThat(result, is(-2));

        result = context.resolveExpressionRef(library, "Floor2I").getExpression().evaluate(context);
        assertThat(result, is(2));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Exp#evaluate(Context)}
     */
    @Test
    public void testExp() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "ExpNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Exp0").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "ExpNeg0").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "Exp1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.exp(1d))));

        result = context.resolveExpressionRef(library, "ExpNeg1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.exp((double) -1))));

        try {
          result = context.resolveExpressionRef(library, "Exp1000").getExpression().evaluate(context);
        } catch (ArithmeticException ae) {
          assertThat(ae.getMessage(), is("Results in positive infinity"));
        }

        try {
          result = context.resolveExpressionRef(library, "Exp1000D").getExpression().evaluate(context);
        } catch (ArithmeticException ae) {
          assertThat(ae.getMessage(), is("Results in positive infinity"));
        }
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Log#evaluate(Context)}
     */
    @Test
    public void testLog() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "LogNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Log1BaseNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Log1Base1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0d)));

        result = context.resolveExpressionRef(library, "Log1Base2").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0d)));

        result = context.resolveExpressionRef(library, "Log1Base100").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0d)));

        result = context.resolveExpressionRef(library, "Log16Base2").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(4d)));

        result = context.resolveExpressionRef(library, "LogD125Base2").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((double)-3)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Ln#evaluate(Context)}
     */
    @Test
    public void testLn() throws JAXBException {
      Context context = new Context(library);
      Object result;

      result = context.resolveExpressionRef(library, "LnNull").getExpression().evaluate(context);
      assertThat(result, is(nullValue()));

      try {
        result = context.resolveExpressionRef(library, "Ln0").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("Results in negative infinity"));
      }

      try {
        result = context.resolveExpressionRef(library, "LnNeg0").getExpression().evaluate(context);
      } catch (ArithmeticException ae) {
        assertThat(ae.getMessage(), is("Results in negative infinity"));
      }

      result = context.resolveExpressionRef(library, "Ln1").getExpression().evaluate(context);
      assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.log(1d))));

      result = context.resolveExpressionRef(library, "LnNeg1").getExpression().evaluate(context);
      assertThat(result, is(nullValue()));

      result = context.resolveExpressionRef(library, "Ln1000").getExpression().evaluate(context);
      assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.log(1000))));

      result = context.resolveExpressionRef(library, "Ln1000D").getExpression().evaluate(context);
      assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.log(1000d))));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Max#evaluate(Context)}
     */
    @Test
    public void testMaximum() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "IntegerMaxValue").getExpression().evaluate(context);
      assertThat(result, is(Integer.MAX_VALUE));

      result = context.resolveExpressionRef(library, "DecimalMaxValue").getExpression().evaluate(context);
      assertThat(result, is(Interval.maxValue(BigDecimal.class)));

      result = context.resolveExpressionRef(library, "QuantityMaxValue").getExpression().evaluate(context);
      assertThat(result, is(Interval.maxValue(Quantity.class)));

      result = context.resolveExpressionRef(library, "DateTimeMaxValue").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {9999, 12, 31, 23, 59, 59, 999})));

      result = context.resolveExpressionRef(library, "TimeMaxValue").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {23, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Min#evaluate(Context)}
     */
    @Test
    public void testMinimum() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef(library, "IntegerMinValue").getExpression().evaluate(context);
      assertThat(result, is(Integer.MIN_VALUE));

      result = context.resolveExpressionRef(library, "DecimalMinValue").getExpression().evaluate(context);
      assertThat(result, is(Interval.minValue(BigDecimal.class)));

      result = context.resolveExpressionRef(library, "QuantityMinValue").getExpression().evaluate(context);
      assertThat(result, is(Interval.minValue(Quantity.class)));

      result = context.resolveExpressionRef(library, "DateTimeMinValue").getExpression().evaluate(context);
      assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {0001, 1, 1, 0, 0, 0, 0})));

      result = context.resolveExpressionRef(library, "TimeMinValue").getExpression().evaluate(context);
      assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {0, 0, 0, 0})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Modulo#evaluate(Context)}
     */
    @Test
    public void testModulo() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "ModuloNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Modulo0By0").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Modulo4By2").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "Modulo4DBy2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0d)));

        result = context.resolveExpressionRef(library, "Modulo10By3").getExpression().evaluate(context);
        assertThat(result, is((1)));

        result = context.resolveExpressionRef(library, "Modulo10DBy3D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "Modulo10IBy3D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "ModuloDResult").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.5)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Multiply#evaluate(Context)}
     */
    @Test
    public void testMultiply() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef(library, "MultiplyNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Multiply1By1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Multiply1DBy2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2.0)));

        result = context.resolveExpressionRef(library, "Multiply1IBy2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2.0)));

        // TODO: should return multiplied units i.e. cm2
        result = context.resolveExpressionRef(library, "Multiply1CMBy2CM").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal(2.0)).withUnit("cm")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Negate#evaluate(Context)}
     */
    @Test
    public void testNegate() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "NegateNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Negate0").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "NegateNeg0").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "Negate1").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "NegateNeg1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Negate0D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-(0d))));

        result = context.resolveExpressionRef(library, "NegateNeg0D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0d)));

        result = context.resolveExpressionRef(library, "Negate1D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((double) -1)));

        result = context.resolveExpressionRef(library, "NegateNeg1D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("1.0")));

        result = context.resolveExpressionRef(library, "Negate1CM").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("-1.0")).withUnit("cm")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Predecessor#evaluate(Context)}
     */
    @Test
    public void testPredecessor() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "PredecessorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "PredecessorOf0").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "PredecessorOf1").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "PredecessorOf1D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo((BigDecimal)Interval.predecessor(new BigDecimal(1.0))));

       result = context.resolveExpressionRef(library, "PredecessorOf101D").getExpression().evaluate(context);
       assertThat(result, is(Interval.predecessor(new BigDecimal("1.01"))));

       result = context.resolveExpressionRef(library, "PredecessorOf1QCM").getExpression().evaluate(context);
       assertThat(result, is(new Quantity().withValue(new BigDecimal("0.99999999")).withUnit("cm")));

       result = context.resolveExpressionRef(library, "PredecessorOfJan12000").getExpression().evaluate(context);
       assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {1999, 12, 31})));

       result = context.resolveExpressionRef(library, "PredecessorOfNoon").getExpression().evaluate(context);
       assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {11, 59, 59, 999})));

       try {
         result = context.resolveExpressionRef(library, "PredecessorUnderflowDt").getExpression().evaluate(context);
       } catch (RuntimeException re) {
         assertThat(re.getMessage(), is("The result of the predecessor operation exceeds the minimum value allowed for type DateTime."));
       }

       try {
         result = context.resolveExpressionRef(library, "PredecessorUnderflowT").getExpression().evaluate(context);
       } catch (RuntimeException re) {
         assertThat(re.getMessage(), is("The result of the predecessor operation exceeds the minimum value allowed for type Time."));
       }
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Power#evaluate(Context)}
     */
    @Test
    public void testPower() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "PowerNullToNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Power0To0").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Power2To2").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef(library, "PowerNeg2To2").getExpression().evaluate(context);
        assertThat(result, is(4));

       result = context.resolveExpressionRef(library, "Power2ToNeg2").getExpression().evaluate(context);
       assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("0.25")));

        result = context.resolveExpressionRef(library, "Power2DTo2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(4d)));

        result = context.resolveExpressionRef(library, "PowerNeg2DTo2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(4d)));

        result = context.resolveExpressionRef(library, "Power2DToNeg2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.25)));

        result = context.resolveExpressionRef(library, "Power2DTo2").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(4d)));

        result = context.resolveExpressionRef(library, "Power2To2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(4d)));

        result = context.resolveExpressionRef(library, "Power2To4").getExpression().evaluate(context);
        assertThat(result, is(16));

        result = context.resolveExpressionRef(library, "Power2DTo4D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("16.0")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Round#evaluate(Context)}
     */
    @Test
    public void testRound() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "RoundNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Round1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "Round0D5").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(1.0)));

        result = context.resolveExpressionRef(library, "Round0D4").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.0)));

       result = context.resolveExpressionRef(library, "Round3D14159").getExpression().evaluate(context);
       assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("3.14")));

        result = context.resolveExpressionRef(library, "RoundNeg0D5").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.0)));

        result = context.resolveExpressionRef(library, "RoundNeg0D4").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.0)));

        result = context.resolveExpressionRef(library, "RoundNeg0D6").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-1.0)));

        result = context.resolveExpressionRef(library, "RoundNeg1D1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-1.0)));

        result = context.resolveExpressionRef(library, "RoundNeg1D5").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-1.0)));

        result = context.resolveExpressionRef(library, "RoundNeg1D6").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-2.0)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Subtract#evaluate(Context)}
     */
    @Test
    public void testSubtract() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "SubtractNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Subtract1And1").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "Subtract1DAnd2D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-1.0)));

        result = context.resolveExpressionRef(library, "Subtract1CMAnd2CM").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal(-1.0)).withUnit("cm")));

        result = context.resolveExpressionRef(library, "Subtract2And11D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("0.9")));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Successor#evaluate(Context)}
     */
    @Test
    public void testSuccessor() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "SuccessorNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SuccessorOf0").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "SuccessorOf1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "SuccessorOf1D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo((BigDecimal)Interval.successor(new BigDecimal(1.0))));

       result = context.resolveExpressionRef(library, "SuccessorOf101D").getExpression().evaluate(context);
       assertThat(result, is(Interval.successor(new BigDecimal("1.01"))));

       result = context.resolveExpressionRef(library, "SuccessorOfJan12000").getExpression().evaluate(context);
       assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2000, 1, 2})));

       result = context.resolveExpressionRef(library, "SuccessorOfNoon").getExpression().evaluate(context);
       assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {12, 0, 0, 1})));

       try {
         result = context.resolveExpressionRef(library, "SuccessorOverflowDt").getExpression().evaluate(context);
       } catch (RuntimeException re) {
         assertThat(re.getMessage(), is("The result of the successor operation exceeds the maximum value allowed for type DateTime."));
       }

       try {
         result = context.resolveExpressionRef(library, "SuccessorOverflowT").getExpression().evaluate(context);
       } catch (RuntimeException re) {
         assertThat(re.getMessage(), is("The result of the successor operation exceeds the maximum value allowed for type Time."));
       }
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Truncate#evaluate(Context)}
     */
    @Test
    public void testTruncate() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "TruncateNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Truncate0").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "Truncate0D0").getExpression().evaluate(context);
        assertThat(result, is((0)));

        result = context.resolveExpressionRef(library, "Truncate0D1").getExpression().evaluate(context);
        assertThat(result, is((0)));

        result = context.resolveExpressionRef(library, "Truncate1").getExpression().evaluate(context);
        assertThat(result, is((1)));

        result = context.resolveExpressionRef(library, "Truncate1D0").getExpression().evaluate(context);
        assertThat(result, is((1)));

        result = context.resolveExpressionRef(library, "Truncate1D1").getExpression().evaluate(context);
        assertThat(result, is((1)));

        result = context.resolveExpressionRef(library, "Truncate1D9").getExpression().evaluate(context);
        assertThat(result, is((1)));

        result = context.resolveExpressionRef(library, "TruncateNeg1").getExpression().evaluate(context);
        assertThat(result, is((-1)));

        result = context.resolveExpressionRef(library, "TruncateNeg1D0").getExpression().evaluate(context);
        assertThat(result, is((-1)));

        result = context.resolveExpressionRef(library, "TruncateNeg1D1").getExpression().evaluate(context);
        assertThat(result, is((-1)));

        result = context.resolveExpressionRef(library, "TruncateNeg1D9").getExpression().evaluate(context);
        assertThat(result, is((-1)));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.TruncatedDivide#evaluate(Context)}
     */
    @Test
    public void testTruncatedDivide() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "TruncatedDivideNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "TruncatedDivide2By1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "TruncatedDivide10By3").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef(library, "TruncatedDivide10d1By3D1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(3.0)));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg2ByNeg1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg10ByNeg3").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg10d1ByNeg3D1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(3.0)));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg2By1").getExpression().evaluate(context);
        assertThat(result, is(-2));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg10By3").getExpression().evaluate(context);
        assertThat(result, is(-3));

        result = context.resolveExpressionRef(library, "TruncatedDivideNeg10d1By3D1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-3.0)));

        result = context.resolveExpressionRef(library, "TruncatedDivide2ByNeg1").getExpression().evaluate(context);
        assertThat(result, is((-2)));

        result = context.resolveExpressionRef(library, "TruncatedDivide10ByNeg3").getExpression().evaluate(context);
        assertThat(result, is(-3));

        result = context.resolveExpressionRef(library, "TruncatedDivide10d1ByNeg3D1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-3.0)));

        result = context.resolveExpressionRef(library, "TruncatedDivide10By5D").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal("2.0")));
    }
}
