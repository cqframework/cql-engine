package org.opencds.cqf.cql.execution;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.testng.annotations.Test;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import org.joda.time.Partial;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Arrays;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CqlListOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testSort() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("simpleSortAsc").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 1, 2, 4, 5, 6)));

        result = context.resolveExpressionRef("simpleSortDesc").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(6, 5, 4, 2, 1, 1)));

//        result = context.resolveExpressionRef("simpleSortStringAsc").getExpression().evaluate(context);
//        assertThat(result, is(Arrays.asList(1, 1, 2, 4, 5, 6)));
//
//        result = context.resolveExpressionRef("simpleSortStringDesc").getExpression().evaluate(context);
//        assertThat(result, is(Arrays.asList(6, 5, 4, 2, 1, 1)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Contains#evaluate(Context)}
     */
    @Test
    public void testContains() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ContainsABNullHasNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ContainsNullFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ContainsABCHasA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ContainsJan2012True").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ContainsJan2012False").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ContainsTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ContainsTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Distinct#evaluate(Context)}
     */
    @Test
    public void testDistinct() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("DistinctEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        // result = context.resolveExpressionRef("DistinctNullNullNull").getExpression().evaluate(context);
        // assertThat(result, is(new ArrayList<Object>() {{
        //     add(null);
        // }}));
        //
        // result = context.resolveExpressionRef("DistinctANullANull").getExpression().evaluate(context);
        // assertThat(result, is(Arrays.asList("a", null)));

        result = context.resolveExpressionRef("Distinct112233").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef("Distinct123123").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef("DistinctAABBCC").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList("a", "b", "c")));

        result = context.resolveExpressionRef("DistinctABCABC").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList("a", "b", "c")));

        result = context.resolveExpressionRef("DistinctDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 10, 5})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 1, 1})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef("DistinctTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Equal#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("EqualNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EqualEmptyListNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EqualNullEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EqualEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Equal12And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("Equal123And12").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("Equal123And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EqualDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EqualDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("EqualTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EqualTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Except#evaluate(Context)}
     */
    @Test
    public void testExcept() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ExceptEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef("Except1234And23").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 4)));

        result = context.resolveExpressionRef("Except23And1234").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef("ExceptDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(1));

        result = context.resolveExpressionRef("ExceptTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(1));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Exists#evaluate(Context)}
     */
    @Test
    public void testExists() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ExistsEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ExistsListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Exists1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Exists12").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ExistsDateTime").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ExistsTime").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Flatten#evaluate(Context)}
     */
    @Test
    public void testFlatten() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("FlattenEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef("FlattenListNullAndNull").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(null, null)));

        result = context.resolveExpressionRef("FlattenList12And34").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 4)));

        result = context.resolveExpressionRef("FlattenDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef("FlattenTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.First#evaluate(Context)}
     */
    @Test
    public void testFirst() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("FirstEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("FirstNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("First1Null").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("First12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("FirstDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef("FirstTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.In#evaluate(Context)}
     */
    @Test
    public void testIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("InNullEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("InNullAnd1Null").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("In1Null").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("In1And12").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("In3And12").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("InDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("InDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("InTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("InTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Includes#evaluate(Context)}
     */
    @Test
    public void testIncludes() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IncludesEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludesListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Includes123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Includes123And2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Includes123And4").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IncludesDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludesDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IncludesTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludesTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.IncludedIn#evaluate(Context)}
     */
    @Test
    public void testIncludedIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IncludedInEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedInListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedInEmptyAnd123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedIn2And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedIn4And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IncludedInDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedInDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("IncludedInTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("IncludedInTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Indexer#evaluate(Context)}
     */
    @Test
    public void testIndexer() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IndexerNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("Indexer0Of12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("Indexer1Of12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("Indexer2Of12").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerNeg1Of12").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexerDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef("IndexerTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }


    /**
     * {@link org.opencds.cqf.cql.elm.execution.IndexOf#evaluate(Context)}
     */
    @Test
    public void testIndexOf() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IndexOfEmptyNull").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef("IndexOfNullEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("IndexOfNullIn1Null").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("IndexOf1In12").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("IndexOf2In12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("IndexOf3In12").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef("IndexOfDateTime").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("IndexOfTime").getExpression().evaluate(context);
        assertThat(result, is(1));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Intersect#evaluate(Context)}
     */
    @Test
    public void testIntersect() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("IntersectEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef("Intersect1234And23").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(2, 3)));

        result = context.resolveExpressionRef("Intersect23And1234").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(2, 3)));

        result = context.resolveExpressionRef("IntersectDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef("IntersectTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Last#evaluate(Context)}
     */
    @Test
    public void testLast() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("LastEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("LastNull1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("Last1Null").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("Last12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("LastDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));

        result = context.resolveExpressionRef("LastTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Length#evaluate(Context)}
     */
    @Test
    public void testLength() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("LengthEmpty").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("LengthNull1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("Length1Null").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("Length12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("LengthDateTime").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef("LengthTime").getExpression().evaluate(context);
        assertThat(result, is(6));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Equivalent#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("EquivalentEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EquivalentABCAndABC").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EquivalentABCAndAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("EquivalentABCAnd123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("Equivalent123AndABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("Equivalent123AndString123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EquivalentDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EquivalentDateTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EquivalentDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("EquivalentTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("EquivalentTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("EquivalentTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.NotEqual#evaluate(Context)}
     */
    @Test
    public void testNotEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("NotEqualEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NotEqualABCAndABC").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NotEqualABCAndAB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("NotEqualABCAnd123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NotEqual123AndABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NotEqual123AndString123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("NotEqualDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("NotEqualDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("NotEqualTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("NotEqualTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ProperIncludes#evaluate(Context)}
     */
    @Test
    public void testProperlyIncludes() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ProperIncludesEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludesListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludes123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludes123And2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludes123And4").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludesDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludesDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludesTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludesTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ProperIncludedIn#evaluate(Context)}
     */
    @Test
    public void testProperlyIncludedIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("ProperIncludedInEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludedInListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludedInEmptyAnd123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludedIn2And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludedIn4And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ProperIncludedInDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ProperIncludedInDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SingletonFrom#evaluate(Context)}
     */
    @Test
    public void testSingletonFrom() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("SingletonFromEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SingletonFromListNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SingletonFrom1").getExpression().evaluate(context);
        assertThat(result, is(1));

        try {
            result = context.resolveExpressionRef("SingletonFrom12").getExpression().evaluate(context);
            Assert.fail("List with more than one element should throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat(ex, isA(IllegalArgumentException.class));
        }

        result = context.resolveExpressionRef("SingletonFromDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef("SingletonFromTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Union#evaluate(Context)}
     */
    @Test
    public void testUnion() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef("UnionEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef("UnionListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(null, null)));

        result = context.resolveExpressionRef("Union123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef("Union123And2").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 2)));

        result = context.resolveExpressionRef("Union123And4").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 4)));

        result = context.resolveExpressionRef("UnionDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2001, 9, 11})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(2)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(3));

        result = context.resolveExpressionRef("UnionTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(2)).getPartial(), is(new Partial(Time.getFields(4), new int[] {12, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(3)).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(4));
    }
}
