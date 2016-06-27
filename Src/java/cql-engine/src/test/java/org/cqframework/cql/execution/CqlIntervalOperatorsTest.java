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

        result = context.resolveExpressionRef(library, "Except12").getExpression().evaluate(context);
        assertThat((Interval)result, is(new Interval(1, true, 2, true)));

        /*
        In
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
        Interval
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
        Start
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

        /*
        Starts
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

        /*
        Union
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
        End
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
