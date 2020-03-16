package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

/*

    structured type Ratio
    {
      numerator Quantity
      denominator Quantity
    }

    The Ratio type represents a relationship between two quantities, such as a titre (e.g. 1:128), or a concentration
        (e.g. 5 'mg':10’mL'). The numerator and denominator elements must be present (i.e. can not be null).

*/

public class RatioEvaluator extends org.cqframework.cql.elm.execution.Ratio {

    @Override
    protected Object internalEvaluate(Context context) {
        Quantity numerator = (Quantity) getNumerator().evaluate(context);
        Quantity denominator = (Quantity) getDenominator().evaluate(context);

        return new org.opencds.cqf.cql.runtime.Ratio().setNumerator(numerator).setDenominator(denominator);
    }
}
