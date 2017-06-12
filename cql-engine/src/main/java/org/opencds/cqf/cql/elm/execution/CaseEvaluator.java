package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.cqframework.cql.elm.execution.CaseItem;
import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.runtime.Value;

/*

A standard case allows any number of conditions, each with a corresponding expression
  that will be the result of the case if the associated condition evaluates to true.
If none of the conditions evaluate to true, the else expression is the result:
case
  when X > Y then X
  when Y > X then Y
  else 0
end

A selected case specifies a comparand, and each case item specifies a possible value for the comparand.
If the comparand is equal to a case item, the corresponding expression is the result of the selected case.
If the comparand does not equal any of the case items, the else expression is the result:
case X
  when 1 then 12
  when 2 then 14
  else 15
end

*/

/**
 *   Created by Chris Schuler on 9/25/2016
 */
public class CaseEvaluator extends org.cqframework.cql.elm.execution.Case {

    public Object selectedCase(Context context, Object comparand) {
        for (CaseItem caseItem : getCaseItem()) {
            Object when = caseItem.getWhen().evaluate(context);
            Boolean check = EquivalentEvaluator.equivalent(comparand, when);
            if (check == null) {
                continue;
            }

            if (check) {
                return caseItem.getThen().evaluate(context);
            }
        }

        return getElse().evaluate(context);
    }

    public Object standardCase(Context context) {

        for (CaseItem caseItem : getCaseItem()) {
            Boolean when = (Boolean)caseItem.getWhen().evaluate(context);

            if (when == null) {
                continue;
            }

            if (when) {
                return caseItem.getThen().evaluate(context);
            }
        }

        return getElse().evaluate(context);
    }

    @Override
    public Object evaluate(Context context) {
        Expression comparand = getComparand();
        Object ret = comparand == null ? standardCase(context) : selectedCase(context, comparand.evaluate(context));
        return context.logTrace(this.getClass(), ret, this.getCaseItem());
    }
}
