package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Schuler on 6/13/2017.
 */
public class ChildrenEvaluator extends org.cqframework.cql.elm.execution.Children {

    private static void addQuantity(List<Object> list, Quantity quantity) {
        list.add(quantity.getValue());
        list.add(quantity.getUnit());
    }

    private static void addCode(List<Object> list, Code code) {
        list.add(code.getSystem());
        list.add(code.getVersion());
        list.add(code.getCode());
        list.add(code.getSystem());
    }

    private static void addConcept(List<Object> list, Concept concept) {
        for (Code code : concept.getCodes()) {
            addCode(list, code);
        }

        list.add(concept.getDisplay());
    }

    private static void addDateTime(List<Object> list, DateTime dateTime) {
        for (int i = 0; i < dateTime.getPartial().size(); ++i) {
            list.add(dateTime.getPartial().get(DateTime.getField(i)));
        }

        list.add(dateTime.getTimezoneOffset());
    }

    private static void addTime(List<Object> list, Time time) {
        for (int i = 0; i < time.getPartial().size(); ++i) {
            list.add(time.getPartial().get(Time.getField(i)));
        }

        list.add(time.getTimezoneOffset());
    }

    private static void addList(List<Object> list, List<Object> listToProcess) {
        for (Object o : listToProcess) {
            list.add(children(o));
        }

        list = (List<Object>) FlattenEvaluator.flatten(list);
    }

    public static Object children(Object source) {
        if (source == null) {
            return null;
        }

        List<Object> ret = new ArrayList<>();

        if (source instanceof Integer || source instanceof BigDecimal
                || source instanceof String || source instanceof Boolean)
        {
            ret.add(source);
        }

        else if (source instanceof Quantity) {
            addQuantity(ret, (Quantity) source);
        }

        else if (source instanceof Code) {
            addCode(ret, (Code) source);
        }

        else if (source instanceof Concept) {
            addConcept(ret, (Concept) source);
        }

        else if (source instanceof DateTime) {
            addDateTime(ret, (DateTime) source);
        }

        else if (source instanceof Time) {
            addTime(ret, (Time) source);
        }

        else if (source instanceof Iterable) {
            addList(ret, (List<Object>) source);
        }

        // TODO: Intervals and Tuples?

        return ret;
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), children(source), source);
    }
}
