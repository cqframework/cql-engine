package org.cqframework.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ConvertEvaluator extends Convert {
    @Override
    public Object evaluate(Context context) {
        // TODO: Fix this
//        String packageName = Convert.class.getPackage().getName();
//        try {
//            Class toClass = Class.forName(String.format("%s.To%s", packageName, getToType().getLocalPart()));
//            Expression expresion = (Expression)toClass.newInstance();
//            Method setOperand = expresion.getClass().getMethod("setOperand",Expression.class);
//            setOperand.invoke(expresion, getOperand());
//
//            return expresion.evaluate(context);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        throw new NotImplementedException("Evaluate not implemented.");
    }
}
