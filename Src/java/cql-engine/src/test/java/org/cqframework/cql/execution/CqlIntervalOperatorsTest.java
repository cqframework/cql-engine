package org.cqframework.cql.execution;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by Bryn on 5/1/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class CqlIntervalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void TestIntervalOperators() {
        Context context = new Context(library);

        /*
        After:
        define TestAfterNull: null after IntegerIntervalTest
        define IntegerIntervalAfterTrue: IntegerIntervalTest2 after IntegerIntervalTest
        define IntegerIntervalAfterFalse: IntegerIntervalTest after IntegerIntervalTest2
        define IntegerIntervalPointAfterTrue: 12 after IntegerIntervalTest
        define IntegerIntervalPointAfterFalse: 9 after IntegerIntervalTest
        define IntegerIntervalAfterPointTrue: IntegerIntervalTest2 after 5
        define IntegerIntervalAfterPointFalse: IntegerIntervalTest2 after 12
        define DecimalIntervalAfterTrue: DecimalIntervalTest2 after DecimalIntervalTest
        define DecimalIntervalAfterFalse: DecimalIntervalTest after DecimalIntervalTest2
        define DecimalIntervalPointAfterTrue: 12.0 after DecimalIntervalTest
        define DecimalIntervalPointAfterFalse: 9.0 after DecimalIntervalTest
        define DecimalIntervalAfterPointTrue: DecimalIntervalTest2 after 5.0
        define DecimalIntervalAfterPointFalse: DecimalIntervalTest2 after 12.0
        define QuantityIntervalAfterTrue: QuantityIntervalTest2 after QuantityIntervalTest
        define QuantityIntervalAfterFalse: QuantityIntervalTest after QuantityIntervalTest2
        define QuantityIntervalPointAfterTrue: 12.0'g' after QuantityIntervalTest
        define QuantityIntervalPointAfterFalse: 9.0'g' after QuantityIntervalTest
        define QuantityIntervalAfterPointTrue: QuantityIntervalTest2 after 5.0'g'
        define QuantityIntervalAfterPointFalse: QuantityIntervalTest2 after 12.0'g'
        */
        Object result = context.resolveExpressionRef(library, "TestAfterNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IntegerIntervalPointAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalPointAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IntegerIntervalAfterPointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalAfterPointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalPointAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalPointAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalAfterPointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalAfterPointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalPointAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalPointAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalAfterPointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalAfterPointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Before:
        define TestBeforeNull : null before IntegerIntervalTest
        define IntegerIntervalBeforeFalse: IntegerIntervalTest2 before IntegerIntervalTest
        define IntegerIntervalBeforeTrue: IntegerIntervalTest before IntegerIntervalTest2
        define IntegerIntervalPointBeforeTrue: 9 before IntegerIntervalTest2
        define IntegerIntervalPointBeforeFalse: 9 before IntegerIntervalTest
        define IntegerIntervalBeforePointTrue: IntegerIntervalTest before 11
        define IntegerIntervalBeforePointFalse: IntegerIntervalTest before 8
        define DecimalIntervalBeforeFalse: DecimalIntervalTest2 before DecimalIntervalTest
        define DecimalIntervalBeforeTrue: DecimalIntervalTest before DecimalIntervalTest2
        define DecimalIntervalPointBeforeTrue: 9.0 before DecimalIntervalTest2
        define DecimalIntervalPointBeforeFalse: 9.0 before DecimalIntervalTest
        define DecimalIntervalBeforePointTrue: DecimalIntervalTest before 11.0
        define DecimalIntervalBeforePointFalse: DecimalIntervalTest before 8.0
        define QuantityIntervalBeforeTrue: QuantityIntervalTest before QuantityIntervalTest2
        define QuantityIntervalBeforeFalse: QuantityIntervalTest2 before QuantityIntervalTest
        define QuantityIntervalPointBeforeTrue: QuantityIntervalTest before 12.0'g'
        define QuantityIntervalPointBeforeFalse: QuantityIntervalTest before 9.0'g'
        define QuantityIntervalBeforePointTrue:  5.0'g' before QuantityIntervalTest2
        define QuantityIntervalBeforePointFalse: 12.0'g' before QuantityIntervalTest2
        */
        result = context.resolveExpressionRef(library, "TestBeforeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IntegerIntervalBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalPointBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalPointBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IntegerIntervalBeforePointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalBeforePointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalPointBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalPointBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalBeforePointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalBeforePointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalPointBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalPointBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalBeforePointTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalBeforePointFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Collapse:
        define TestCollapseNull: collapse {Interval(null, null)}
        define IntegerIntervalCollapse: collapse { Interval[1,5], Interval[3,7], Interval[12,19], Interval[7,10] }
        define IntegerIntervalCollapse2: collapse { Interval[1,2], Interval[3,7], Interval[10,19], Interval[7,10] }
        define DecimalIntervalCollapse: collapse { Interval[1.0,5.0], Interval[3.0,7.0], Interval[12.0,19.0], Interval[7.0,10.0] }
        define QuantityIntervalCollapse: collapse { Interval[1.0 'g',5.0 'g'], Interval[3.0 'g',7.0 'g'], Interval[12.0 'g',19.0 'g'], Interval[7.0 'g',10.0 'g'] }
        */
        result = context.resolveExpressionRef(library, "TestCollapseNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalCollapse").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(new Interval(1, true, 10, true), new Interval(12, true, 19, true))));

        result = context.resolveExpressionRef(library, "IntegerIntervalCollapse2").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(new Interval(1, true, 2, true), new Interval(3, true, 19, true))));

        result = context.resolveExpressionRef(library, "DecimalIntervalCollapse").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(new Interval(new BigDecimal("1.0"), true, new BigDecimal("10.0"), true), new Interval(new BigDecimal("12.0"), true, new BigDecimal("19.0"), true))));

        result = context.resolveExpressionRef(library, "QuantityIntervalCollapse").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(new Interval(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("10.0")).withUnit("g"), true), new Interval(new Quantity().withValue(new BigDecimal("12.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("19.0")).withUnit("g"), true))));

        /*
        Contains:
        define TestContainsNull: IntegerIntervalTest contains null
        define IntegerIntervalContainsTrue: IntegerIntervalTest contains 5
        define IntegerIntervalContainsFalse: IntegerIntervalTest contains 25
        define DecimalIntervalContainsTrue: DecimalIntervalTest contains 8.0
        define DecimalIntervalContainsFalse: DecimalIntervalTest contains 255.0
        define QuantityIntervalContainsTrue: QuantityIntervalTest contains 2.0 'g'
        define QuantityIntervalContainsFalse: QuantityIntervalTest contains 100.0 'g'
        */
        result = context.resolveExpressionRef(library, "TestContainsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalContainsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalContainsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalContainsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalContainsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalContainsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalContainsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Ends
        define TestEndsNull: IntegerIntervalTest ends Interval(null, null)
        define IntegerIntervalEndsTrue: IntegerIntervalTest4 ends IntegerIntervalTest
        define IntegerIntervalEndsFalse: IntegerIntervalTest3 ends IntegerIntervalTest
        define DecimalIntervalEndsTrue: DecimalIntervalTest3 ends DecimalIntervalTest
        define DecimalIntervalEndsFalse: DecimalIntervalTest2 ends DecimalIntervalTest
        define QuantityIntervalEndsTrue: QuantityIntervalTest3 ends QuantityIntervalTest
        define QuantityIntervalEndsFalse: QuantityIntervalTest2 ends QuantityIntervalTest
        */
        result = context.resolveExpressionRef(library, "TestEndsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalEndsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalEndsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalEndsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalEndsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalEndsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalEndsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Equal
        define TestEqualNull: IntegerIntervalTest = Interval(null, null)
        define IntegerIntervalEqualTrue: IntegerIntervalTest = IntegerIntervalTest
        define IntegerIntervalEqualFalse: IntegerIntervalTest = IntegerIntervalTest2
        define DecimalIntervalEqualTrue: DecimalIntervalTest = DecimalIntervalTest
        define DecimalIntervalEqualFalse: DecimalIntervalTest = DecimalIntervalTest2
        define QuantityIntervalEqualTrue: QuantityIntervalTest = QuantityIntervalTest
        define QuantityIntervalEqualFalse: QuantityIntervalTest = QuantityIntervalTest2
        */
        result = context.resolveExpressionRef(library, "TestEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Except:
        define TestExceptNull: NullInterval except NullInterval
        define IntegerIntervalExcept1to3: IntegerIntervalTest except IntegerIntervalTest4
        define IntegerIntervalExceptNull: Interval[1, 10] except Interval[3,7]
        define DecimalIntervalExcept1to3: DecimalIntervalTest except DecimalIntervalTest3
        define DecimalIntervalExceptNull: Interval[1.0, 10.0] except Interval[3.0,7.0]
        define QuantityIntervalExcept1to4: QuantityIntervalTest except QuantityIntervalTest3
        */
        result = context.resolveExpressionRef(library, "TestExceptNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalExcept1to3").getExpression().evaluate(context);
        assertThat((Interval)result, is(new Interval(1, true, 3, true)));

        result = context.resolveExpressionRef(library, "IntegerIntervalExceptNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DecimalIntervalExcept1to3").getExpression().evaluate(context);
        assertThat((Interval)result, is(new Interval(new BigDecimal("1.0"), true, new BigDecimal("3.0"), true)));

        result = context.resolveExpressionRef(library, "DecimalIntervalExceptNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "QuantityIntervalExcept1to4").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("4.0")).withUnit("g"), true)));

        /*
        In
        define TestInNull: 5 in NullInterval
        define IntegerIntervalInTrue: 5 in IntegerIntervalTest;
        define IntegerIntervalInFalse: 500 in IntegerIntervalTest;
        define DecimalIntervalInTrue: 9.0 in DecimalIntervalTest;
        define DecimalIntervalInFalse: -2.0 in DecimalIntervalTest;
        define QuantityIntervalInTrue: 1.0 'g' in QuantityIntervalTest;
        define QuantityIntervalInFalse: 55.0 'g' in QuantityIntervalTest;
        */
        result = context.resolveExpressionRef(library, "TestInNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Includes
        define TestIncludesNull: IntegerIntervalTest includes null
        define IntegerIntervalIncludesTrue: IntegerIntervalTest includes IntegerIntervalTest4
        define IntegerIntervalIncludesFalse: IntegerIntervalTest includes IntegerIntervalTest3
        define DecimalIntervalIncludesTrue: DecimalIntervalTest includes DecimalIntervalTest3
        define DecimalIntervalIncludesFalse: DecimalIntervalTest includes DecimalIntervalTest2
        define QuantityIntervalIncludesTrue: QuantityIntervalTest includes QuantityIntervalTest3
        define QuantityIntervalIncludesFalse: QuantityIntervalTest includes QuantityIntervalTest2
        */
        result = context.resolveExpressionRef(library, "TestIncludesNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Included In
        define TestIncludedInNull: null included in IntegerIntervalTest
        define IntegerIntervalIncludedInTrue: IntegerIntervalTest4 included in IntegerIntervalTest
        define IntegerIntervalIncludedInFalse: IntegerIntervalTest3 included in IntegerIntervalTest
        define DecimalIntervalIncludedInTrue: DecimalIntervalTest3 included in DecimalIntervalTest
        define DecimalIntervalIncludedInFalse: DecimalIntervalTest2 included in DecimalIntervalTest
        define QuantityIntervalIncludedInTrue: QuantityIntervalTest3 included in QuantityIntervalTest
        define QuantityIntervalIncludedInFalse: QuantityIntervalTest2 included in QuantityIntervalTest
        */
        result = context.resolveExpressionRef(library, "TestIncludedInNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Intersect
        define TestIntersectNull: IntegerIntervalTest intersect null
        define IntegerIntervalIntersectTest4to10: IntegerIntervalTest intersect IntegerIntervalTest4
        define IntegerIntervalIntersectTestNull: IntegerIntervalTest intersect IntegerIntervalTest2
        define DecimalIntervalIntersectTest4to10: DecimalIntervalTest intersect DecimalIntervalTest3
        define DecimalIntervalIntersectTestNull: DecimalIntervalTest intersect DecimalIntervalTest2
        define QuantityIntervalIntersectTest5to10: QuantityIntervalTest intersect QuantityIntervalTest3
        define QuantityIntervalIntersectTestNull: QuantityIntervalTest intersect QuantityIntervalTest2
        */
        // result = context.resolveExpressionRef(library, "TestIntersectNull").getExpression().evaluate(context);
        // assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalIntersectTest4to10").getExpression().evaluate(context);
        assertThat(result, is(new Interval(4, true, 10, true)));

        result = context.resolveExpressionRef(library, "IntegerIntervalIntersectTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DecimalIntervalIntersectTest4to10").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new BigDecimal("4.0"), true, new BigDecimal("10.0"), true)));

        result = context.resolveExpressionRef(library, "IntegerIntervalIntersectTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "QuantityIntervalIntersectTest5to10").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new Quantity().withValue(new BigDecimal("5.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("10.0")).withUnit("g"), true)));

        result = context.resolveExpressionRef(library, "QuantityIntervalIntersectTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        /*
        Equivalent
        define IntegerIntervalEquivalentTrue: IntegerIntervalTest ~ IntegerIntervalTest
        define IntegerIntervalEquivalentFalse: IntegerIntervalTest3 ~ IntegerIntervalTest
        define DecimalIntervalEquivalentTrue: DecimalIntervalTest ~ DecimalIntervalTest
        define DecimalIntervalEquivalentFalse: DecimalIntervalTest2 ~ DecimalIntervalTest
        define QuantityIntervalEquivalentTrue: QuantityIntervalTest ~ QuantityIntervalTest
        define QuantityIntervalEquivalentFalse: QuantityIntervalTest2 ~ QuantityIntervalTest
        */

        result = context.resolveExpressionRef(library, "IntegerIntervalEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Meets
        define TestMeetsNull: Interval(null, 5] meets Interval[11, null)
        define IntegerIntervalMeetsTrue: IntegerIntervalTest meets IntegerIntervalTest2
        define IntegerIntervalMeetsFalse: IntegerIntervalTest meets IntegerIntervalTest3
        define DecimalIntervalMeetsTrue: Interval[3.01, 5.00000001] meets Interval[5.00000002, 8.50]
        define DecimalIntervalMeetsFalse: Interval[3.01, 5.00000001] meets Interval[5.5, 8.50]
        define QuantityIntervalMeetsTrue: Interval[3.01 'g', 5.00000001 'g'] meets Interval[5.00000002 'g', 8.50 'g']
        define QuantityIntervalMeetsFalse: Interval[3.01 'g', 5.00000001 'g'] meets Interval[5.5 'g', 8.50 'g']
        */
        result = context.resolveExpressionRef(library, "TestMeetsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        MeetsBefore
        define TestMeetsBeforeNull: Interval(null, 5] meets before Interval[11, null)
        define IntegerIntervalMeetsBeforeTrue: IntegerIntervalTest meets before IntegerIntervalTest2
        define IntegerIntervalMeetsBeforeFalse: IntegerIntervalTest meets before IntegerIntervalTest3
        define DecimalIntervalMeetsBeforeTrue: Interval[3.50000001, 5.00000011] meets before Interval[5.00000012, 8.50]
        define DecimalIntervalMeetsBeforeFalse: Interval[8.01, 15.00000001] meets before Interval[15.00000000, 18.50]
        define QuantityIntervalMeetsBeforeTrue: Interval[3.50000001 'g', 5.00000011 'g'] meets before Interval[5.00000012 'g', 8.50 'g']
        define QuantityIntervalMeetsBeforeFalse: Interval[8.01 'g', 15.00000001 'g'] meets before Interval[15.00000000 'g', 18.50 'g']
        */
        result = context.resolveExpressionRef(library, "TestMeetsBeforeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        MeetsAfter
        define TestMeetsAfterNull: Interval(null, 5] meets after Interval[11, null)
        define IntegerIntervalMeetsAfterTrue: IntegerIntervalTest2 meets after IntegerIntervalTest
        define IntegerIntervalMeetsAfterFalse: IntegerIntervalTest3 meets after IntegerIntervalTest
        define DecimalIntervalMeetsAfterTrue: Interval[55.00000123, 128.032156] meets after Interval[12.00258, 55.00000122]
        define DecimalIntervalMeetsAfterFalse: Interval[55.00000124, 150.222222] meets after Interval[12.00258, 55.00000122]
        define QuantityIntervalMeetsAfterTrue: Interval[55.00000123 'g', 128.032156 'g'] meets after Interval[12.00258 'g', 55.00000122 'g']
        define QuantityIntervalMeetsAfterFalse: Interval[55.00000124 'g', 150.222222 'g'] meets after Interval[12.00258 'g', 55.00000122 'g']
        */
        result = context.resolveExpressionRef(library, "TestMeetsAfterNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalMeetsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalMeetsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalMeetsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        NotEqual
        define IntegerIntervalNotEqualTrue: IntegerIntervalTest != IntegerIntervalTest2
        define IntegerIntervalNotEqualFalse: IntegerIntervalTest != IntegerIntervalTest
        define DecimalIntervalNotEqualTrue: DecimalIntervalTest != DecimalIntervalTest2
        define DecimalIntervalNotEqualFalse: DecimalIntervalTest != DecimalIntervalTest
        define QuantityIntervalNotEqualTrue: QuantityIntervalTest != QuantityIntervalTest2
        define QuantityIntervalNotEqualFalse: QuantityIntervalTest != QuantityIntervalTest
        */
        result = context.resolveExpressionRef(library, "IntegerIntervalNotEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalNotEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalNotEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalNotEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalNotEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalNotEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        Overlaps
        define TestOverlapsNull: IntegerIntervalTest overlaps null
        define IntegerIntervalOverlapsTrue: IntegerIntervalTest overlaps IntegerIntervalTest4
        define IntegerIntervalOverlapsFalse: IntegerIntervalTest overlaps IntegerIntervalTest2
        define DecimalIntervalOverlapsTrue: DecimalIntervalTest overlaps DecimalIntervalTest3
        define DecimalIntervalOverlapsFalse: DecimalIntervalTest overlaps DecimalIntervalTest2
        define QuantityIntervalOverlapsTrue: QuantityIntervalTest overlaps QuantityIntervalTest3
        define QuantityIntervalOverlapsFalse: QuantityIntervalTest overlaps QuantityIntervalTest2
        */
        result = context.resolveExpressionRef(library, "TestOverlapsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        OverlapsBefore
        define TestOverlapsBeforeNull: NullInterval overlaps before IntegerIntervalTest
        define IntegerIntervalOverlapsBeforeTrue: IntegerIntervalTest overlaps before IntegerIntervalTest4
        define IntegerIntervalOverlapsBeforeFalse: IntegerIntervalTest4 overlaps before IntegerIntervalTest
        define DecimalIntervalOverlapsBeforeTrue: DecimalIntervalTest overlaps before DecimalIntervalTest3
        define DecimalIntervalOverlapsBeforeFalse: DecimalIntervalTest3 overlaps before DecimalIntervalTest
        define QuantityIntervalOverlapsBeforeTrue: QuantityIntervalTest overlaps before QuantityIntervalTest3
        define QuantityIntervalOverlapsBeforeFalse: QuantityIntervalTest3 overlaps before QuantityIntervalTest
        */
        result = context.resolveExpressionRef(library, "TestOverlapsBeforeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsBeforeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsBeforeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        OverlapsAfter
        define TestOverlapsAfterNull: NullInterval overlaps after IntegerIntervalTest
        define IntegerIntervalOverlapsAfterTrue: IntegerIntervalTest5 overlaps after IntegerIntervalTest
        define IntegerIntervalOverlapsAfterFalse: IntegerIntervalTest4 overlaps after IntegerIntervalTest
        define DecimalIntervalOverlapsAfterTrue: DecimalIntervalTest4 overlaps after DecimalIntervalTest
        define DecimalIntervalOverlapsAfterFalse: DecimalIntervalTest3 overlaps after DecimalIntervalTest
        define QuantityIntervalOverlapsAfterTrue: QuantityIntervalTest4 overlaps after QuantityIntervalTest
        define QuantityIntervalOverlapsAfterFalse: QuantityIntervalTest3 overlaps after QuantityIntervalTest
        */
        result = context.resolveExpressionRef(library, "TestOverlapsAfterNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalOverlapsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalOverlapsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalOverlapsAfterFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        ProperlyIncludes
        define TestProperlyIncludesNull: NullInterval properly includes IntegerIntervalTest
        define IntegerIntervalProperlyIncludesTrue: IntegerIntervalTest properly includes IntegerIntervalTest4
        define IntegerIntervalProperlyIncludesFalse: IntegerIntervalTest properly includes IntegerIntervalTest5
        define DecimalIntervalProperlyIncludesTrue: DecimalIntervalTest properly includes DecimalIntervalTest3
        define DecimalIntervalProperlyIncludesFalse: DecimalIntervalTest properly includes DecimalIntervalTest4
        define QuantityIntervalProperlyIncludesTrue: QuantityIntervalTest properly includes QuantityIntervalTest
        define QuantityIntervalProperlyIncludesFalse: QuantityIntervalTest properly includes QuantityIntervalTest4
        */
        result = context.resolveExpressionRef(library, "TestProperlyIncludesNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalProperlyIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalProperlyIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalProperlyIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalProperlyIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalProperlyIncludesTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalProperlyIncludesFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        ProperlyIncludedIn
        define TestProperlyIncludedInNull: IntegerIntervalTest properly included in NullInterval
        define IntegerIntervalProperlyIncludedInTrue: IntegerIntervalTest4 properly included in IntegerIntervalTest
        define IntegerIntervalProperlyIncludedInFalse: IntegerIntervalTest5 properly included in IntegerIntervalTest
        define DecimalIntervalProperlyIncludedInTrue: DecimalIntervalTest3 properly included in DecimalIntervalTest
        define DecimalIntervalProperlyIncludedInFalse: DecimalIntervalTest4 properly included in DecimalIntervalTest
        define QuantityIntervalProperlyIncludedInTrue: QuantityIntervalTest3 properly included in QuantityIntervalTest
        define QuantityIntervalProperlyIncludedInFalse: QuantityIntervalTest properly included in QuantityIntervalTest4
        */
        result = context.resolveExpressionRef(library, "TestProperlyIncludedInNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalProperlyIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalProperlyIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalProperlyIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalProperlyIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalProperlyIncludedInTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalProperlyIncludedInFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*
        //Interval
        define IntegerIntervalTest: Interval[1, 10]
        define DecimalIntervalTest: Interval[1.0, 10.0]
        define QuantityIntervalTest: Interval[1.0 'g', 10.0 'g']
        //define DateTimeIntervalTest: Interval[@2016-05-01T00:00:00Z, @2016-05-02T00:00:00Z)
        //define TimeIntervalTest: Interval[@T00:00:00Z, @T23:59:59Z]
         */
        result = context.resolveExpressionRef(library, "IntegerIntervalTest").getExpression().evaluate(context);
        assertThat(result, is(new Interval(1, true, 10, true)));

        result = context.resolveExpressionRef(library, "DecimalIntervalTest").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new BigDecimal("1.0"), true, new BigDecimal("10.0"), true)));

        result = context.resolveExpressionRef(library, "QuantityIntervalTest").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("10.0")).withUnit("g"), true)));

        //result = context.resolveExpressionRef(library, "DateTimeIntervalTest").getExpression().evaluate(context);
        //assertThat(result, is(new Interval(new Partial("2016-05-01T00:00:00Z"), true, new Partial("2016-05-02T00:00:00Z", false))));

        //result = context.resolveExpressionRef(library, "TimeIntervalTest").getExpression().evaluate(context);
        //assertThat(result, is(new Interval(new PartialTime("T00:00:00Z"), true, new PartialTime("T23:59:59Z"), true)));

        /*
        //MinValue
        define IntegerMinValue: minimum Integer
        define DecimalMinValue: minimum Decimal
        define QuantityMinValue: minimum Quantity
        //define DateTimeMinValue: minimum DateTime
        //define TimeMinValue: minimum Time
         */
        result = context.resolveExpressionRef(library, "IntegerMinValue").getExpression().evaluate(context);
        assertThat(result, is(Integer.MIN_VALUE));

        result = context.resolveExpressionRef(library, "DecimalMinValue").getExpression().evaluate(context);
        assertThat(result, is(Interval.minValue(BigDecimal.class)));

        result = context.resolveExpressionRef(library, "QuantityMinValue").getExpression().evaluate(context);
        assertThat(result, is(Interval.minValue(Quantity.class)));

        //result = context.resolveExpressionRef(library, "DateTimeMinValue").getExpression().evaluate(context);
        //assertThat(result, is(Interval.minValue(Partial.class)));

        //result = context.resolveExpressionRef(library, "TimeMinValue").getExpression().evaluate(context);
        //assertThat(result, is(Interval.minValue(PartialTime.class)));

        /*
        //MaxValue
        define IntegerMaxValue: maximum Integer
        define DecimalMaxValue: maximum Decimal
        define QuantityMaxValue: maximum Quantity
        //define DateTimeMaxValue: maximum DateTime
        //define TimeMaxValue: maximum Time
        */
        result = context.resolveExpressionRef(library, "IntegerMaxValue").getExpression().evaluate(context);
        assertThat(result, is(Integer.MAX_VALUE));

        result = context.resolveExpressionRef(library, "DecimalMaxValue").getExpression().evaluate(context);
        assertThat(result, is(Interval.maxValue(BigDecimal.class)));

        result = context.resolveExpressionRef(library, "QuantityMaxValue").getExpression().evaluate(context);
        assertThat(result, is(Interval.maxValue(Quantity.class)));

        //result = context.resolveExpressionRef(library, "DateTimeMaxValue").getExpression().evaluate(context);
        //assertThat(result, is(Interval.maxValue(Partial.class)));

        //result = context.resolveExpressionRef(library, "TimeMaxValue").getExpression().evaluate(context);
        //assertThat(result, is(Interval.maxValue(PartialTime.class)));

        /*
        //Successor
        define IntegerSuccessor: successor of 1
        define DecimalSuccessor: successor of 1.0
        define QuantitySuccessor: successor of 1.0 'g'
        //define DateTimeSuccessor: successor of @2016-05-01T00:00:00Z
        //define TimeSuccessor: successor of @T00:00:00Z
         */
        result = context.resolveExpressionRef(library, "IntegerSuccessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.successor(1)));

        result = context.resolveExpressionRef(library, "DecimalSuccessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.successor(new BigDecimal(1.0))));

        result = context.resolveExpressionRef(library, "QuantitySuccessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.successor(new Quantity().withValue(new BigDecimal(1.0)).withUnit("g"))));

        //result = context.resolveExpressionRef(library, "DateTimeSuccessor").getExpression().evaluate(context);
        //assertThat(result, is(Interval.successor(new Partial("2016-05-01T00:00:00Z"))));

        //result = context.resolveExpressionRef(library, "TimeSuccessor").getExpression().evaluate(context);
        //assertThat(result, is(Interval.successor(new PartialTime("2016-05-01T00:00:00Z"))));

        /*
        //Predecessor
        define IntegerPredecessor: predecessor of 1
        define DecimalPredecessor: predecessor of 1.0
        define QuantityPredecessor: predecessor of 1.0 'g'
        //define DateTimePredecessor: predecessor of @2016-05-01T00:00:00Z
        //define TimePredecessor: predecessor of @T00:00:00Z
         */
        result = context.resolveExpressionRef(library, "IntegerPredecessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.predecessor(1)));

        result = context.resolveExpressionRef(library, "DecimalPredecessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.predecessor(new BigDecimal(1.0))));

        result = context.resolveExpressionRef(library, "QuantityPredecessor").getExpression().evaluate(context);
        assertThat(result, is(Interval.predecessor(new Quantity().withValue(new BigDecimal(1.0)).withUnit("g"))));

        //result = context.resolveExpressionRef(library, "DateTimePredecessor").getExpression().evaluate(context);
        //assertThat(result, is(Interval.predecessor(new Partial("2016-05-01T00:00:00Z"))));

        //result = context.resolveExpressionRef(library, "TimePredecessor").getExpression().evaluate(context);
        //assertThat(result, is(Interval.predecessor(new PartialTime("2016-05-01T00:00:00Z"))));

        /*
        //Start
        define IntegerIntervalStart: start of IntegerIntervalTest
        define DecimalIntervalStart: start of DecimalIntervalTest
        define QuantityIntervalStart: start of QuantityIntervalTest
        //define DateTimeIntervalStart: start of DateTimeIntervalTest
        //define TimeIntervalStart: start of TimeIntervalTest
         */
        result = context.resolveExpressionRef(library, "IntegerIntervalStart").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "DecimalIntervalStart").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("1.0")));

        result = context.resolveExpressionRef(library, "QuantityIntervalStart").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g")));

        //result = context.resolveExpressionRef(library, "DateTimeIntervalStart").getExpression().evaluate(context);
        //assertThat(result, is(new Partial("@2016-05-01T00:00:00Z")));

        //result = context.resolveExpressionRef(library, "TimeIntervalStart").getExpression().evaluate(context);
        //assertThat(result, is(new PartialTime("T00:00:00Z")));

        /*Starts
        define TestStartsNull: NullInterval starts IntegerIntervalTest
        define IntegerIntervalStartsTrue: IntegerIntervalTest4 starts IntegerIntervalTest5
        define IntegerIntervalStartsFalse: IntegerIntervalTest starts IntegerIntervalTest4
        define DecimalIntervalStartsTrue: DecimalIntervalTest3 starts DecimalIntervalTest4
        define DecimalIntervalStartsFalse: DecimalIntervalTest starts DecimalIntervalTest3
        define QuantityIntervalStartsTrue: QuantityIntervalTest3 starts QuantityIntervalTest4
        define QuantityIntervalStartsFalse: QuantityIntervalTest starts QuantityIntervalTest3
        */
        result = context.resolveExpressionRef(library, "TestStartsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalStartsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IntegerIntervalStartsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "DecimalIntervalStartsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "DecimalIntervalStartsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "QuantityIntervalStartsTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "QuantityIntervalStartsFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        /*Union
        define TestUnionNull: NullInterval union IntegerIntervalTest
        define IntegerIntervalUnion1To15: IntegerIntervalTest union IntegerIntervalTest5
        define IntegerIntervalUnionNull: IntegerIntervalTest union IntegerIntervalTest2
        define DecimalIntervalUnion1To15: DecimalIntervalTest union DecimalIntervalTest4
        define DecimalIntervalUnionNull: DecimalIntervalTest union DecimalIntervalTest2
        define QuantityIntervalUnion1To15: QuantityIntervalTest union QuantityIntervalTest4
        define QuantityIntervalUnionNull: QuantityIntervalTest union QuantityIntervalTest2
        */
        result = context.resolveExpressionRef(library, "TestUnionNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IntegerIntervalUnion1To15").getExpression().evaluate(context);
        assertThat(result, is(new Interval(1, true, 15, true)));

        result = context.resolveExpressionRef(library, "IntegerIntervalUnionNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DecimalIntervalUnion1To15").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new BigDecimal("1.0"), true, new BigDecimal("15.0"), true)));

        result = context.resolveExpressionRef(library, "DecimalIntervalUnionNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "QuantityIntervalUnion1To15").getExpression().evaluate(context);
        assertThat(result, is(new Interval(new Quantity().withValue(new BigDecimal("1.0")).withUnit("g"), true, new Quantity().withValue(new BigDecimal("15.0")).withUnit("g"), true)));

        result = context.resolveExpressionRef(library, "QuantityIntervalUnionNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        /*
        Width
        define IntegerIntervalTestWidth9: width of IntegerIntervalTest
        define IntervalTestWidthNull: width of null
        define DecimalIntervalTestWidth11: width of DecimalIntervalTest4
        define QuantityIntervalTestWidth5: width of QuantityIntervalTest3
        */
        result = context.resolveExpressionRef(library, "IntegerIntervalTestWidth9").getExpression().evaluate(context);
        assertThat(result, is(9));

        result = context.resolveExpressionRef(library, "IntervalTestWidthNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "DecimalIntervalTestWidth11").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("11.0")));

        result = context.resolveExpressionRef(library, "QuantityIntervalTestWidth5").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("5.0")).withUnit("g")));

        /*
        //End
        define IntegerIntervalEnd: end of IntegerIntervalTest
        define DecimalIntervalEnd: end of DecimalIntervalTest
        define QuantityIntervalEnd: end of QuantityIntervalTest
        //define DateTimeIntervalEnd: end of DateTimeIntervalTest
        //define TimeIntervalEnd: end of TimeIntervalTest
         */
        result = context.resolveExpressionRef(library, "IntegerIntervalEnd").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef(library, "DecimalIntervalEnd").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("10.0")));

        result = context.resolveExpressionRef(library, "QuantityIntervalEnd").getExpression().evaluate(context);
        assertThat(result, is(new Quantity().withValue(new BigDecimal("10.0")).withUnit("g")));

        //result = context.resolveExpressionRef(library, "DateTimeIntervalEnd").getExpression().evaluate(context);
        //assertThat(result, is(Interval.predecessor(new Partial("@2016-05-02T00:00:00Z"))));

        //result = context.resolveExpressionRef(library, "TimeIntervalEnd").getExpression().evaluate(context);
        //assertThat(result, is(new PartialTime("T23:59:59Z")));
    }
}
