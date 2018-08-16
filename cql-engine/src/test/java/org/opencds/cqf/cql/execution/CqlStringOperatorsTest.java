package org.opencds.cqf.cql.execution;

import org.testng.Assert;
import org.testng.annotations.Test;
import javax.xml.bind.JAXBException;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlStringOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CombineEvaluator#evaluate(Context)}
     */
    @Test
    public void testCombine() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("CombineNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("CombineEmptyList").getExpression().evaluate(context);
        assertThat(result, is(""));

        result = context.resolveExpressionRef("CombineABC").getExpression().evaluate(context);
        assertThat(result, is("abc"));

        result = context.resolveExpressionRef("CombineABCSepDash").getExpression().evaluate(context);
        assertThat(result, is("a-b-c"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ConcatenateEvaluator#evaluate(Context)}
     */
    @Test
    public void testConcatenate() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ConcatenateNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateNullB").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateAB").getExpression().evaluate(context);
        assertThat(result, is("ab"));

        result = context.resolveExpressionRef("ConcatenateABWithAdd").getExpression().evaluate(context);
        assertThat(result, is("ab"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EndsWithEvaluator#evaluate(Context)}
     */
    @Test
    public void testEndsWith() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("EndsWithNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EndsWithTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EndsWithFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IndexerEvaluator#evaluate(Context)}
     */
    @Test
    public void testIndexer() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IndexerNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerNull1String").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerAB0").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("IndexerAB1").getExpression().evaluate(context);
        assertThat(result, is("b"));

        result = context.resolveExpressionRef("IndexerAB2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerABNeg1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.MatchesEvaluator#evaluate(Context)}
     */
    @Test
    public void testMatches() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("MatchesNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("MatchesNumberFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("MatchesNumberTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("MatchesAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("MatchesWordsAndSpacesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("MatchesWordsAndSpacesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("MatchesNotWords").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("MatchesWhiteSpace").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.LastPositionOfEvaluator#evaluate(Context)}
     */
    @Test
    public void testLastPositionOf() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("LastPositionOfNull").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("LastPositionOfNull1").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("LastPositionOfNull2").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("LastPositionOf1").getExpression().evaluate(context);
        Assert.assertTrue((Integer) result == 1);

        result = context.resolveExpressionRef("LastPositionOf2").getExpression().evaluate(context);
        Assert.assertTrue((Integer) result == 11);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.LengthEvaluator#evaluate(Context)}
     */
    @Test
    public void testLength() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("LengthNullString").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("LengthEmptyString").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("LengthA").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("LengthAB").getExpression().evaluate(context);
        assertThat(result, is(2));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.LowerEvaluator#evaluate(Context)}
     */
    @Test
    public void testLower() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("LowerNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("LowerEmpty").getExpression().evaluate(context);
        assertThat(result, is(""));

        result = context.resolveExpressionRef("LowerA").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("LowerB").getExpression().evaluate(context);
        assertThat(result, is("b"));

        result = context.resolveExpressionRef("LowerAB").getExpression().evaluate(context);
        assertThat(result, is("ab"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.PositionOfEvaluator#evaluate(Context)}
     */
    @Test
    public void testPositionOf() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("PositionOfNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("PositionOfANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("PositionOfNullA").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("PositionOfAInAB").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("PositionOfBInAB").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("PositionOfCInAB").getExpression().evaluate(context);
        assertThat(result, is(-1));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ReplaceMatchesEvaluator#evaluate(Context)}
     */
    @Test
    public void testReplaceMatches() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ReplaceMatchesNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ReplaceMatchesAll").getExpression().evaluate(context);
        assertThat(result, is("But still waters run deep"));

        result = context.resolveExpressionRef("ReplaceMatchesMany").getExpression().evaluate(context);
        assertThat(result, is("Who put the bang in the bang she bang she bang?"));

        result = context.resolveExpressionRef("ReplaceMatchesSpaces").getExpression().evaluate(context);
        assertThat(result, is("All$that$glitters$is$not$gold"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SplitEvaluator#evaluate(Context)}
     */
    @Test
    public void testSplit() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("SplitNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SplitNullComma").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SplitABNull").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Collections.singletonList("a,b"))));

        result = context.resolveExpressionRef("SplitABDash").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Collections.singletonList("a,b"))));

        result = context.resolveExpressionRef("SplitABComma").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Arrays.asList("a", "b"))));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.StartsWithEvaluator#evaluate(Context)}
     */
    @Test
    public void testStartsWith() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("StartsWithNull").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("StartsWithNull1").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("StartsWithNull2").getExpression().evaluate(context);
        Assert.assertTrue(result == null);

        result = context.resolveExpressionRef("StartsWithTrue1").getExpression().evaluate(context);
        Assert.assertTrue((Boolean) result);

        result = context.resolveExpressionRef("StartsWithFalse1").getExpression().evaluate(context);
        Assert.assertTrue(!(Boolean) result);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SubstringEvaluator#evaluate(Context)}
     */
    @Test
    public void testSubstring() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("SubstringNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SubstringANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SubstringNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SubstringAB0").getExpression().evaluate(context);
        assertThat(result, is("ab"));

        result = context.resolveExpressionRef("SubstringAB1").getExpression().evaluate(context);
        assertThat(result, is("b"));

        result = context.resolveExpressionRef("SubstringAB2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SubstringABNeg1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SubstringAB0To1").getExpression().evaluate(context);
        assertThat(result, is("a"));

        result = context.resolveExpressionRef("SubstringABC1To1").getExpression().evaluate(context);
        assertThat(result, is("b"));

        result = context.resolveExpressionRef("SubstringAB0To3").getExpression().evaluate(context);
        assertThat(result, is("ab"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.UpperEvaluator#evaluate(Context)}
     */
    @Test
    public void testUpper() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("UpperNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("UpperEmpty").getExpression().evaluate(context);
        assertThat(result, is(""));

        result = context.resolveExpressionRef("UpperA").getExpression().evaluate(context);
        assertThat(result, is("A"));

        result = context.resolveExpressionRef("UpperB").getExpression().evaluate(context);
        assertThat(result, is("B"));

        result = context.resolveExpressionRef("UpperAB").getExpression().evaluate(context);
        assertThat(result, is("AB"));
    }

    // TODO: QuantityToString
    // TODO: DateTimeToString1
    // TODO: DateTimeToString2
    // TODO: DateTimeToString3
    // TODO: TimeToString1
    // TODO: TupleToString
    // TODO: IntervalToString
    // TODO: UncertaintyToString
    // TODO: CodeToString
    // TODO: ConceptToString
}
