package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.util.*;

/*
collapse(argument List<Interval<T>>) List<Interval<T>>

The collapse operator returns the unique set of intervals that completely covers the ranges present in the given list of intervals.
If the list of intervals is empty, the result is empty.
If the list of intervals contains a single interval, the result is a list with that interval.
If the list of intervals contains nulls, they will be excluded from the resulting list.
If the argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class CollapseEvaluator extends org.cqframework.cql.elm.execution.Collapse {

    public static Object collapse(Iterable list) {

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
                    temporals.add((BaseTemporal) interval.getStart());
                }
                if (interval.getEnd() != null) {
                    temporals.add((BaseTemporal) interval.getEnd());
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
        Iterable list = (Iterable)getOperand().evaluate(context);

        return context.logTrace(this.getClass(), collapse(list), list);
    }
}
