package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.util.*;

/*
collapse(argument List<Interval<T>>) List<Interval<T>>
collapse(argument List<Interval<T>>, per Quantity) List<Interval<T>>

The collapse operator returns the unique set of intervals that completely covers the ranges present in the given list of intervals.
    In other words, adjacent intervals within a sorted list are merged if they either overlap or meet.

Note that because the semantics for overlaps and meets are themselves defined in terms of the interval successor and predecessor operators,
    sets of date/time-based intervals that are only defined to a particular precision will calculate meets and overlaps at that precision.
    For example, a list of DateTime-based intervals where the boundaries are all specified to the hour will collapse at the hour precision,
        unless the collapse precision is overridden with the per argument.

The per argument determines the precision at which the collapse will be performed, and must be a quantity value that is compatible with the
    point type of the input intervals. For numeric intervals, this means a default unit ('1'). For date/time intervals, this means a temporal duration.

If the list of intervals is empty, the result is empty. If the list of intervals contains a single interval, the result is a list with that interval.
    If the list of intervals contains nulls, they will be excluded from the resulting list.

If the list argument is null, the result is null.

If the per argument is null, the default unit interval for the point type of the intervals involved will be used
    (i.e. the interval that has a width equal to the result of the successor function for the point type).
*/

public class CollapseEvaluator extends org.cqframework.cql.elm.execution.Collapse {

    public static Object collapse(Iterable list, Quantity per) {

        if (list == null) {
            return null;
        }

        List<Interval> intervals = new ArrayList<>();
        for (Object interval : list) {
            if (interval != null) {
                intervals.add((Interval) interval);
            }
        }

        if (intervals.size() == 1 || intervals.isEmpty()) {
            return intervals;
        }

        intervals.sort(new CqlList().valueSort);

        String precision = null;
        if (intervals.get(0).getStart() instanceof BaseTemporal || intervals.get(0).getEnd() instanceof BaseTemporal) {
            List<BaseTemporal> temporals = new ArrayList<>();
            for (Interval interval : intervals) {
                if (interval.getStart() != null) {
                    if (per != null && per.getUnit() != null) {
                        temporals.add(((BaseTemporal) interval.getStart()).setPrecision(Precision.fromString(per.getUnit())));
                    }
                    else {
                        temporals.add((BaseTemporal) interval.getStart());
                    }
                }
                if (interval.getEnd() != null) {
                    if (per != null && per.getUnit() != null) {
                        temporals.add(((BaseTemporal) interval.getEnd()).setPrecision(Precision.fromString(per.getUnit())));
                    }
                    else {
                        temporals.add((BaseTemporal) interval.getEnd());
                    }
                }
            }
            precision = BaseTemporal.getHighestPrecision(temporals.toArray(new BaseTemporal[temporals.size()]));
        }

        for (int i = 0; i < intervals.size(); ++i) {
            if ((i+1) < intervals.size()) {
                Boolean doMerge = AnyTrueEvaluator.anyTrue(
                        Arrays.asList(
                                OverlapsEvaluator.overlaps(intervals.get(i), intervals.get(i+1), precision),
                                MeetsEvaluator.meets(intervals.get(i), intervals.get(i+1), precision)
                        )
                );

                if (doMerge == null) {
                    continue;
                }

                if (doMerge) {
                    Boolean isNextEndGreater = GreaterEvaluator.greater((intervals.get(i+1)).getEnd(), (intervals.get(i)).getEnd());
                    intervals.set(
                            i,
                            new Interval(
                                    (intervals.get(i)).getStart(), true,
                                    isNextEndGreater != null && isNextEndGreater ? (intervals.get(i+1)).getEnd() : (intervals.get(i)).getEnd(), true
                            )
                    );
                    intervals.remove(i+1);
                    i -= 1;
                }
            }
        }

        return intervals;
    }

    @Override
    public Object evaluate(Context context) {
        Iterable list = (Iterable) getOperand().get(0).evaluate(context);
        Quantity per = (Quantity) getOperand().get(1).evaluate(context);

        return collapse(list, per);
    }
}