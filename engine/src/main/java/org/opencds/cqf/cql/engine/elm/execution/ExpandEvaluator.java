package org.opencds.cqf.cql.engine.elm.execution;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.BaseTemporal;
import org.opencds.cqf.cql.engine.runtime.CqlList;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.Precision;
import org.opencds.cqf.cql.engine.runtime.Quantity;

/*

expand(argument List<Interval<T>>, per Quantity) List<Interval<T>>

The expand operator returns the set of intervals of width per for all the intervals in the input.

The per argument must be a quantity value that is compatible with the point type of the input intervals.
    For numeric intervals, this means a default unit ('1').
    For date/time intervals, this means a temporal duration.

Note that if the values in the intervals are more precise than the per quantity, the more precise values will be
    truncated to the precision specified by the per quantity.

If the list of intervals is empty, the result is empty. If the list of intervals contains nulls, they will be excluded
    from the resulting list.

If the list argument is null, the result is null.

If the per argument is null, the default unit interval for the point type of the intervals involved will be used
    (i.e. the interval that has a width equal to the result of the successor function for the point type).

*/

public class ExpandEvaluator extends org.cqframework.cql.elm.execution.Expand
{
    private static Object addPer(Object addTo, Quantity per)
    {
        if (addTo instanceof Integer)
        {
            return AddEvaluator.add(addTo, per.getValue().intValue());
        }
        else if (addTo instanceof BigDecimal)
        {
            return AddEvaluator.add(addTo, per.getValue());
        }
        else if (addTo instanceof Quantity)
        {
            return AddEvaluator.add(addTo, per);
        }

        throw new InvalidOperatorArgument(
                "Expand(List<Interval<T>>, Quantity)",
                String.format("Expand(%s, %s)" + addTo.getClass().getName(), per.getClass().getName())
        );
    }

    public static List<Interval> getExpandedInterval(Interval interval, Quantity per)
    {
        if (interval.getLow() == null || interval.getHigh() == null )
        {
            return null;
        }

        List<Interval> expansion = new ArrayList<>();
        Object start = interval.getStart();

        if ((start instanceof Integer || start instanceof BigDecimal)
                && !per.getUnit().equals("1"))
        {
            return null;
        }

        if (EqualEvaluator.equal(start, interval.getEnd()))
        {
            expansion.add(new Interval(start, true, start, true));
            return expansion;
        }

        if (start instanceof Integer) {
            Object end = addPer(start, per);
            Object predecessorOfEnd = PredecessorEvaluator.predecessor(end);

            while (LessOrEqualEvaluator.lessOrEqual(predecessorOfEnd, interval.getEnd())) {
                expansion.add(new Interval(start, true, predecessorOfEnd, true));
                start = end;
                end = addPer(start, per);
                predecessorOfEnd = PredecessorEvaluator.predecessor(end);
            }
        } else if(start instanceof BigDecimal) {

            int precision = determineMinPrecision((BigDecimal) start, (BigDecimal) interval.getEnd());
            BigDecimal startDecimal = truncateToPrecision((BigDecimal) start, precision) ;
            BigDecimal endDecimal = truncateToPrecision((BigDecimal) interval.getEnd(), precision) ;
            BigDecimal end = (BigDecimal) addPer(startDecimal, per);
            BigDecimal predecessorOfEnd = (BigDecimal) PredecessorEvaluator.predecessor(end);

            if(end.compareTo(endDecimal) == 0) {
                expansion.add(new Interval(startDecimal, true, end, true));
                return expansion;
            }
            while (LessOrEqualEvaluator.lessOrEqual(predecessorOfEnd, endDecimal)) {
                expansion.add(new Interval(startDecimal, true, predecessorOfEnd, true));
                startDecimal = (BigDecimal) end;
                end = (BigDecimal) addPer(startDecimal, per);
                predecessorOfEnd = (BigDecimal) PredecessorEvaluator.predecessor(end);
            }
        }

        return expansion;
    }

    public static List<Interval> getExpandedInterval(Interval interval, Quantity per, String precision)
    {
        if (interval.getLow() == null || interval.getHigh() == null )
        {
            return null;
        }

        Object i;
        try
        {
            i = DurationBetweenEvaluator.duration(interval.getStart(), interval.getEnd(), Precision.fromString(precision));
        }
        catch (Exception e)
        {
            return null;
        }
        if (i instanceof Integer)
        {
            List<Interval> expansion = new ArrayList<>();
            Interval unit = null;
            Object start = interval.getStart();
            Object end = AddEvaluator.add(start, per);
            Object predecessorOfEnd = PredecessorEvaluator.predecessor(end);
            for (int j = 0; j < (Integer) i; ++j)
            {
                unit = new Interval(start, true, predecessorOfEnd, true);
                expansion.add(unit);
                start = end;
                end = AddEvaluator.add(start, per);
                predecessorOfEnd = PredecessorEvaluator.predecessor(end);
            }

            if (unit != null)
            {
                i = DurationBetweenEvaluator.duration(unit.getEnd(), interval.getEnd(), Precision.fromString(precision));
                if (i instanceof Integer && (Integer) i == 1)
                {
                    expansion.add(new Interval(start, true, PredecessorEvaluator.predecessor(end), true));
                }
            }
            else
            {
                // special case - although the width of Interval[@2018-01-01, @2018-01-01] is 0, expansion result is not empty
                if (((BaseTemporal) start).getPrecision() == Precision.fromString(precision)
                        && ((BaseTemporal) end).getPrecision() == Precision.fromString(precision))
                {
                    expansion.add(new Interval(start, true, end, false));
                }
            }

            return expansion;
        }

        // uncertainty
        return null;
    }

    public static List<Interval> expand(Iterable<Interval> list, Quantity per)
    {
        if (list == null)
        {
            return null;
        }

        List<Interval> intervals = CqlList.toList(list, false);

        if (intervals.isEmpty())
        {
            return intervals;
        }

        boolean isTemporal =
            intervals.get(0).getStart() instanceof BaseTemporal
                || intervals.get(0).getEnd() instanceof BaseTemporal;

        if(per == null) {
            per = determinePer(intervals.get(0), isTemporal);
        }


        // collapses overlapping intervals
        intervals = CollapseEvaluator.collapse(intervals, new Quantity().withValue(BigDecimal.ZERO).withUnit(per == null ? "1" : per.getUnit()));

        intervals.sort(new CqlList().valueSort);

        String precision = per.getUnit().equals("1") ? null : per.getUnit();

        // prevent duplicates
        Set<Interval> set = new TreeSet<>();
        for (Interval interval : intervals) {
            if (interval == null)
            {
                continue;
            }

            List<Interval> temp = isTemporal ? getExpandedInterval(interval, per, precision) : getExpandedInterval(interval, per);
            if (temp == null)
            {
                return null;
            }

            if (!temp.isEmpty())
            {
                set.addAll(temp);
            }
        }

        return set.isEmpty() ? new ArrayList<>() : new ArrayList<>(set);
    }

    /*
      The number with the fewest decimal places determines the per for decimal.
        [1, 45] -> 1        // scale 0
        [1.0, 2.0] -> .1    //scale 1
        [1.000001, 2] -> 1   //scale 0
        [1.0, 2.01] -> .1    // scale 1
        [1, 2.010101010] -> 1  //scale 0
        [2.01010101, 1] -> 1   //scale 0
        [1.00, 2.00] -> .01   //scale 2
        [1.00, 2.0005] -> .01  //scale 2
     */
    private static Quantity determinePer(Interval interval, boolean isTemporal) {
        Quantity per = null;

        if (isTemporal) {
            per = new Quantity()
                .withValue(new BigDecimal("1.0"))
                .withUnit(
                    BaseTemporal.getLowestPrecision(
                        (BaseTemporal) interval.getStart(),
                        (BaseTemporal) interval.getEnd()
                    )
                );
        } else {
            per = new Quantity().withDefaultUnit();

            if ((interval.getStart() instanceof BigDecimal)) {
                int scale = determineMinPrecision(((BigDecimal) interval.getStart()), ((BigDecimal) interval.getEnd()));
                BigDecimal d = BigDecimal.valueOf(Math.pow(10.0, BigDecimal.valueOf(scale).doubleValue()));
                per.withValue(BigDecimal.ONE.divide(d));
            } else {
                per = new Quantity().withValue(new BigDecimal("1.0"));
            }

        }
        return per;
    }

    private static int determineMinPrecision(BigDecimal start, BigDecimal end) {
        return Math.min(start.scale(), end.scale());
    }

    private static BigDecimal truncateToPrecision(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.DOWN);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object internalEvaluate(Context context)
    {
        Iterable<Interval> list = (Iterable<Interval>) getOperand().get(0).evaluate(context);
        Quantity per = (Quantity) getOperand().get(1).evaluate(context);

        return expand(list, per);
    }
}
