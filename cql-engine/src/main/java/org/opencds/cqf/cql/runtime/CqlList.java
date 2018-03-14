package org.opencds.cqf.cql.runtime;

import org.cqframework.cql.elm.execution.ByColumn;
import org.cqframework.cql.elm.execution.ByExpression;
import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Christopher on 5/19/2017.
 */
public class CqlList {
    private static Context context;
    private static String alias;
    private static Expression expression;

    private static String path;

    public static Boolean similar(Iterable left, Iterable right, EqualEvaluator.SimilarityMode mode) {
        Iterator leftIterator = left.iterator();
        Iterator rightIterator = right.iterator();

        while (leftIterator.hasNext()) {
            Object leftObject = leftIterator.next();
            if (rightIterator.hasNext()) {
                Object rightObject = rightIterator.next();
                Boolean elementSimilar = EqualEvaluator.similar(leftObject, rightObject, mode);
                if (elementSimilar == null || !elementSimilar) {
                    return elementSimilar;
                }
            }
            else {
                return false;
            }
        }

        if (rightIterator.hasNext()) {
            return rightIterator.next() == null ? null : false;
        }

        return true;
    }

    public static Integer compareTo(Object comparandOne, Object comparandTwo) {
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

    public static Comparator<Object> valueSort = new Comparator<Object>() {
        public int compare(Object comparandOne, Object comparandTwo) {
            return compareTo(comparandOne, comparandTwo);
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

            return compareTo(comparandOne, comparandTwo);
        }
    };

    private static Comparator<Object> columnSort = new Comparator<Object>() {
        public int compare(Object comparandOne, Object comparandTwo) {
            Object one = context.resolvePath(comparandOne, path);
            Object two = context.resolvePath(comparandTwo, path);

            if (one == null && two == null) return 0;
            else if (one == null) return 1;
            else if (two == null) return -1;

            return compareTo(one, two);
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

    public static List<Object> sortByColumn(List<Object> resources, Context theContext, ByColumn column) {
        context = theContext;
        path = column.getPath();

        Collections.sort(resources, columnSort);

        if (column.getDirection().name().toLowerCase().charAt(0) == 'd') {
            Collections.reverse(resources);
        }

        return resources;
    }
}
