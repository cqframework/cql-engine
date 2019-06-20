package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;

public class Executable {
    public Object evaluate(Context context) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }
}
