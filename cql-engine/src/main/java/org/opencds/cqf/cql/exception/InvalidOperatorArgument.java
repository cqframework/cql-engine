package org.opencds.cqf.cql.exception;

public class InvalidOperatorArgument extends CqlException
{
    public InvalidOperatorArgument(String message)
    {
        super(message);
    }

    public InvalidOperatorArgument(String expected, String found)
    {
        super(String.format("Expected %s, Found %s", expected, found));
    }
}
