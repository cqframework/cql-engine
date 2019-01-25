package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Date;
import org.opencds.cqf.cql.runtime.Precision;

public class DateEvaluator extends org.cqframework.cql.elm.execution.Date {

    @Override
    public Object evaluate(Context context) {
        Integer year = this.getYear() == null ? null : (Integer) this.getYear().evaluate(context);
        if (year == null) {
            return null;
        }
        Precision precision = Precision.YEAR;

        Integer month = (Integer) this.getMonth().evaluate(context);
        if (month == null) {
            month = 1;
        }
        else {
            precision = Precision.MONTH;
        }

        Integer day = (Integer) this.getDay().evaluate(context);
        if (day == null) {
            day = 1;
        }
        else {
            precision = Precision.DAY;
        }

        return new Date(year, month, day).setPrecision(precision);
    }
}
