package org.opencds.cqf.cql.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidOperatorArgument extends CqlException
{
    public InvalidOperatorArgument(String message)
    {
        super(message);
    }

    public InvalidOperatorArgument(String operatorName, Object ... args)
    {
        super(
                String.format(
                        "Cannot evaluate the %s operation with argument(s) of type: %s",
                        operatorName,
                        String.join(" and ", Arrays.stream(args).map(x -> x.getClass().getName()).collect(Collectors.toList()))
                )
        );
    }
}
