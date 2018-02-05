package org.opencds.cqf.cql.data;

import java.util.List;

/**
 * Created by Darren on 2018 Feb 5.
 */
public interface ExternalFunctionProvider {
    Object evaluate(String staticFunctionName, List<Object> arguments);
}
