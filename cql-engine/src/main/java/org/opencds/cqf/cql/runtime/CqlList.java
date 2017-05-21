package org.opencds.cqf.cql.runtime;

import org.cqframework.cql.elm.execution.ByExpression;
import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Christopher on 5/19/2017.
 */
public class CqlList {
    private static Context context;
    private static String alias;
    private static Expression expression;

    public static Comparator<Object> valueSort = new Comparator<Object>() {
        public int compare(Object comparandOne, Object comparandTwo) {
            if (comparandOne instanceof Integer) {
                return (Integer)comparandOne - (Integer)comparandTwo;
            }

            else if (comparandOne instanceof BigDecimal) {
                return ((BigDecimal)comparandOne).compareTo((BigDecimal)comparandOne);
            }

            else if (comparandOne instanceof Quantity) {
                return ((Quantity)comparandOne).getValue().compareTo(((Quantity)comparandTwo).getValue());
            }

            else if (comparandOne instanceof DateTime) {
                return ((DateTime)comparandOne).compareTo(((DateTime)comparandTwo));
            }

            else if (comparandOne instanceof String) {
                return ((String)comparandOne).compareTo(((String)comparandTwo));
            }

            else if (comparandOne instanceof Time) {
                return ((Time)comparandOne).compareTo(((Time)comparandTwo));
            }

            throw new IllegalArgumentException("Type is not comparable");
        }
    };

    private static Comparator<Object> expressionSort = new Comparator<Object>() {
        public int compare(Object comparandOne, Object comparandTwo) {

            try {
                context.push(new Variable().withName(alias).withValue(comparandOne));
                comparandOne = expression.evaluate(context);
            }
            finally {
                context.pop();
            }

            try {
                context.push(new Variable().withName(alias).withValue(comparandTwo));
                comparandTwo = expression.evaluate(context);
            }
            finally {
                context.pop();
            }

            if (comparandOne == null && comparandTwo == null) return 0;
            else if (comparandOne == null) return 1;
            else if (comparandTwo == null) return -1;

            if (comparandOne instanceof Integer) {
                return (Integer)comparandOne - (Integer)comparandTwo;
            }

            else if (comparandOne instanceof BigDecimal) {
                return ((BigDecimal)comparandOne).compareTo((BigDecimal)comparandTwo);
            }

            else if (comparandOne instanceof Quantity) {
                return ((Quantity)comparandOne).getValue().compareTo(((Quantity)comparandTwo).getValue());
            }

            else if (comparandOne instanceof DateTime) {
                return ((DateTime)comparandOne).compareTo(((DateTime)comparandTwo));
            }

            else if (comparandOne instanceof String) {
                return ((String)comparandOne).compareTo(((String)comparandTwo));
            }

            else if (comparandOne instanceof Time) {
                return ((Time)comparandOne).compareTo(((Time)comparandTwo));
            }

            throw new IllegalArgumentException("Type is not comparable");
        }
    };

    public static ArrayList<Object> sortList(ArrayList<Object> values) {
        Collections.sort(values, CqlList.valueSort);
        return values;
    }

    public static Iterable<Object> ensureIterable(Object source) {
        if (source instanceof Iterable) {
            return (Iterable<Object>)source;
        }
        else {
            ArrayList sourceList = new ArrayList();
            if (source != null)
                sourceList.add(source);
            return sourceList;
        }
    }

    public static List<Object> sortByExpression(List<Object> resources, Context theContext, ByExpression sortInfo, String theAlias) {
        context = theContext;
        alias = theAlias;
        expression = sortInfo.getExpression();

        Collections.sort(resources, expressionSort);

        if (sortInfo.getDirection().name().toLowerCase().charAt(0) == 'd') {
            Collections.reverse(resources);
        }

        return resources;
    }
}
