package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;
import javax.xml.bind.JAXBException;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlStringOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Combine#evaluate(Context)}
     */
    @Test
    public void testCombine() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("CombineNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("CombineEmptyList").getExpression().evaluate(context);
        assertThat(result, is(""));

        result = context.resolveExpressionRef("CombineABC").getExpression().evaluate(context);
        assertThat(result, is("abc"));

        result = context.resolveExpressionRef("CombineABCSepDash").getExpression().evaluate(context);
        assertThat(result, is("a-b-c"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Concatenate#evaluate(Context)}
     */
    @Test
    public void testConcatenate() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ConcatenateNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateNullB").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConcatenateAB").getExpression().evaluate(context);
        assertThat(result, is("ab"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Indexer#evaluate(Context)}
     */
    @Test
    public void testIndexer() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IndexerNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerANull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerNull1").getExpression().evaluate(context);
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
     * {@link org.opencds.cqf.cql.elm.execution.Length#evaluate(Context)}
     */
    @Test
    public void testLength() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("LengthNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("LengthEmpty").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("LengthA").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("LengthAB").getExpression().evaluate(context);
        assertThat(result, is(2));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Lower#evaluate(Context)}
     */
    @Test
    public void testLower() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("LowerNull").getExpression().evaluate(context);
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
     * {@link org.opencds.cqf.cql.elm.execution.PositionOf#evaluate(Context)}
     */
    @Test
    public void testPositionOf() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("PositionOfNullNull").getExpression().evaluate(context);
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
     * {@link org.opencds.cqf.cql.elm.execution.Split#evaluate(Context)}
     */
    @Test
    public void testSplit() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("SplitNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SplitNullComma").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SplitABNull").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Arrays.asList("a,b"))));

        result = context.resolveExpressionRef("SplitABDash").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Arrays.asList("a,b"))));

        result = context.resolveExpressionRef("SplitABComma").getExpression().evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Arrays.asList("a", "b"))));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Substring#evaluate(Context)}
     */
    @Test
    public void testSubstring() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("SubstringNullNull").getExpression().evaluate(context);
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
     * {@link org.opencds.cqf.cql.elm.execution.Upper#evaluate(Context)}
     */
    @Test
    public void testUpper() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("UpperNull").getExpression().evaluate(context);
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
}
