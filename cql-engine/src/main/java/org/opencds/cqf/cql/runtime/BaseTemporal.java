package org.opencds.cqf.cql.runtime;

import org.joda.time.Partial;
import org.opencds.cqf.cql.elm.execution.GreaterEvaluator;
import org.opencds.cqf.cql.elm.execution.LessEvaluator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Christopher Schuler on 6/11/2017.
 */
public abstract class BaseTemporal {

    protected Partial partial;
    protected BigDecimal timezoneOffset;

    public Partial getPartial() {
        return partial;
    }
    public void setPartial(Partial partial) {
        this.partial = partial;
    }
    public BigDecimal getTimezoneOffset() {
        return timezoneOffset;
    }
    public void setTimezoneOffset(BigDecimal newTimezoneOffset) {
        timezoneOffset = newTimezoneOffset;
    }

    public static Boolean formatCheck(ArrayList<Object> timeElements) {
        boolean prevNull = false;
        for (Object element : timeElements) {
            if (element == null) { prevNull = true; }
            else if (prevNull) {
                return false;
            }
        }
        return true;
    }

    public static int[] getValues(Integer... values) {
        int count = 0;
        int[] temp = new int[7];
        for (Integer value : values) {
            if (value != null) {
                temp[count] = value;
                ++count;
            }
        }
        return Arrays.copyOf(temp, count);
    }

    public Integer compareTo(BaseTemporal other) {
        int size;

        // Uncertainty detection
        if (this.getPartial().size() != other.getPartial().size()) {
            size = this.getPartial().size() > other.getPartial().size() ? other.getPartial().size() : this.getPartial().size();
        }
        else { size = this.getPartial().size(); }

        for (int i = 0; i < size; ++i) {
            Object left = this.getPartial().getValue(i);
            Object right = other.getPartial().getValue(i);
            if (GreaterEvaluator.greater(left, right)) { return 1; }
            else if (LessEvaluator.less(left, right)) { return -1; }
        }
        // Uncertainty wrinkle
        if (this.getPartial().size() != other.getPartial().size()) { return null; }
        return 0;
    }

    @Override
    public String toString() {
        return this.getPartial().toString();
    }
}
