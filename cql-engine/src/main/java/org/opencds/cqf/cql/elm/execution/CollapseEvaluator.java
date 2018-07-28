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

        if (intervals.size() == 1) {
            return intervals;
        }

        else if (intervals.size() == 0) {
            return null;
        }

        intervals.sort(new CqlList().valueSort);

        for (int i = 0; i < intervals.size(); ++i) {
            if ((i+1) < intervals.size()) {
                Boolean merge = OrEvaluator.or(
                        GreaterOrEqualEvaluator.greaterOrEqual(intervals.get(i).getEnd(), intervals.get(i+1).getStart()),
                        EqualEvaluator.equal(SuccessorEvaluator.successor(intervals.get(i).getEnd()), intervals.get(i+1).getStart())
                );

                if (merge == null) {
                    continue;
                }

                if (merge) {
                    intervals.set(i, new Interval((intervals.get(i)).getStart(), true, (intervals.get(i+1)).getEnd(), true));
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
