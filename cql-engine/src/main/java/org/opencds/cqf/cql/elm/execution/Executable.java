package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.exception.CqlException;
import org.opencds.cqf.cql.exception.CqlExceptionHandler;
import org.opencds.cqf.cql.execution.Context;

import java.util.function.Function;

public class Executable
{
    static {
        Thread.setDefaultUncaughtExceptionHandler(new CqlExceptionHandler());
    }
//    public Object execute(Function<Object, Object> function)
//    {
//        Thread.setDefaultUncaughtExceptionHandler(new CqlExceptionHandler());
//        try
//        {
//            return function.apply(null);
//        }
//        catch (Exception e)
//        {
//            if (e instanceof CqlException)
//            {
//                throw e;
//            }
//            else
//            {
//                throw new CqlException(e.getMessage());
//            }
//        }
//    }

    public Object evaluate(Context context) throws CqlException
    {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }
}
