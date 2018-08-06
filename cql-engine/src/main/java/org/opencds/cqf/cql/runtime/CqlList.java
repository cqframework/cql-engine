package org.opencds.cqf.cql.runtime;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;

import java.util.Comparator;
import java.util.Iterator;

public class CqlList {
    private Context context;
    private String alias;
    private Expression expression;
    private String path;

    public CqlList() { }

    public CqlList(Context context, String alias, Expression expression) {
        this.context = context;
        this.alias = alias;
        this.expression = expression;
    }

    public CqlList(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public Comparator<Object> valueSort = new Comparator<Object>() {
        public int compare(Object left, Object right) {
            return compareTo(left, right);
        }
    };

    public Comparator<Object> expressionSort = new Comparator<Object>() {
        public int compare(Object left, Object right) {

            try {
                context.push(new Variable().withName(alias).withValue(left));
                left = expression.evaluate(context);
            }
            finally {
                context.pop();
            }

            try {
                context.push(new Variable().withName(alias).withValue(right));
                right = expression.evaluate(context);
            }
            finally {
                context.pop();
            }

            return compareTo(left, right);
        }
    };

    public Comparator<Object> columnSort = new Comparator<Object>() {
        public int compare(Object left, Object right) {
            Object leftCol = context.resolvePath(left, path);
            Object rightCol = context.resolvePath(right, path);

            return compareTo(leftCol, rightCol);
        }
    };

    public int compareTo(Object left, Object right) {
        if (left == null && right == null) return 0;
        else if (left == null) return -1;
        else if (right == null) return 1;

        try {
            return ((Comparable) left).compareTo(right);
        } catch (ClassCastException cce) {
            throw new NotImplementedException("Type " + left.getClass().getName() + " is not comparable");
        }
    }

    public static Boolean equivalent(Iterable left, Iterable right) {
        Iterator leftIterator = left.iterator();
        Iterator rightIterator = right.iterator();

        while (leftIterator.hasNext()) {
            Object leftObject = leftIterator.next();
            if (rightIterator.hasNext()) {
                Object rightObject = rightIterator.next();
                Boolean elementEquivalent = EquivalentEvaluator.equivalent(leftObject, rightObject);
                if (!elementEquivalent) {
                    return false;
                }
            }
            else { return false; }
        }

        return !rightIterator.hasNext();
    }

    public static Boolean equal(Iterable left, Iterable right) {
        Iterator leftIterator = left.iterator();
        Iterator rightIterator = right.iterator();

        while (leftIterator.hasNext()) {
            Object leftObject = leftIterator.next();
            if (rightIterator.hasNext()) {
                Object rightObject = rightIterator.next();
                if (leftObject instanceof Iterable && rightObject instanceof Iterable) {
                    return equal((Iterable) leftObject, (Iterable) rightObject);
                }
                Boolean elementEquals = EqualEvaluator.equal(leftObject, rightObject);
                if (elementEquals == null || !elementEquals) {
                    return elementEquals;
                }
            }
            else if (leftObject == null) {
                return null;
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
}
