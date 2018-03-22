package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.AliasedQuerySource;
import org.cqframework.cql.elm.execution.ByColumn;
import org.cqframework.cql.elm.execution.ByExpression;
import org.cqframework.cql.elm.execution.LetClause;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;
import org.opencds.cqf.cql.runtime.AliasList;
import org.opencds.cqf.cql.runtime.CqlList;
import org.opencds.cqf.cql.runtime.Tuple;

import java.util.*;

/**
 * Created by Bryn on 5/25/2016.
 */
public class QueryEvaluator extends org.cqframework.cql.elm.execution.Query {

    private boolean shouldInclude;
    private Deque<AliasList> multiQueryStack;

    public Iterable<Object> ensureIterable(Object source) {
        if (source instanceof Iterable) {
            return (Iterable<Object>) source;
        }
        else {
            ArrayList<Object> sourceList = new ArrayList<>();
            if (source != null)
                sourceList.add(source);
            return sourceList;
        }
    }

    /**
     * Resolves let clause using a for-each operation to introduce a Tuple element for each defined expression.
     */
    public void resolveLet(Context context) {
        for (LetClause letClause : this.getLet()) {
            context.addLetExpression(letClause.getIdentifier(), letClause.getExpression());
        }
    }

    public void resolveRelationship(Context context) {
        // TODO: This is the most naive possible implementation here, but it should perform okay with 1) caching and 2) small data sets
        for (org.cqframework.cql.elm.execution.RelationshipClause relationship : getRelationship()) {
            boolean hasSatisfyingData = false;
            Iterable<Object> relatedSourceData = ensureIterable(relationship.getExpression().evaluate(context));
            for (Object relatedElement : relatedSourceData) {
                context.push(new Variable().withName(relationship.getAlias()).withValue(relatedElement));
                try {
                    Object satisfiesRelatedCondition = relationship.getSuchThat().evaluate(context);
                    if (relationship instanceof org.cqframework.cql.elm.execution.With) {
                        if (satisfiesRelatedCondition instanceof Boolean && (Boolean) satisfiesRelatedCondition) {
                            hasSatisfyingData = true;
                            break; // Once we have detected satisfying data, no need to continue testing
                        }
                    }
                }
                finally {
                    context.pop();
                }
            }

            if ((relationship instanceof org.cqframework.cql.elm.execution.With && !hasSatisfyingData)
                    || (relationship instanceof org.cqframework.cql.elm.execution.Without && hasSatisfyingData)) {
                shouldInclude = false;
                break; // Once we have determined the row should not be included, no need to continue testing other related information
            }
        }
    }

    public void resolveWhere(Context context) {
        if (shouldInclude && getWhere() != null) {
            Object satisfiesCondition = this.getWhere().evaluate(context);
            if (!(satisfiesCondition instanceof Boolean && (Boolean)satisfiesCondition)) {
                shouldInclude = false;
            }
        }
    }

    public Object resolveResult(Context context, Object element) {
        return this.getReturn() != null ? this.getReturn().getExpression().evaluate(context) : element;
    }

    public void sortResult(List<Object> result, Context context, String alias) {

        org.cqframework.cql.elm.execution.SortClause sortClause = this.getSort();

        if (sortClause != null) {

            for (org.cqframework.cql.elm.execution.SortByItem byItem : sortClause.getBy()) {

                if (byItem instanceof ByExpression) {
                    result.sort(new CqlList(context, alias, ((ByExpression)byItem).getExpression()).expressionSort);
                }

                else if (byItem instanceof ByColumn) {
                    result.sort(new CqlList(context, ((ByColumn)byItem).getPath()).columnSort);
                }

                else {
                    result.sort(new CqlList().valueSort);
                }

                String direction = byItem.getDirection().value();
                if (direction.equals("desc") || direction.equals("descending")) {
                    java.util.Collections.reverse(result);
                }
            }
        }
    }

    public Object multisourceQuery(Context context) {
        List<Object> returnList = new ArrayList<>();
        try {
            multiQueryStack = new ArrayDeque<>();

            for (AliasedQuerySource source : this.getSource()) {
                // assuming list
                Object sourceObject = source.getExpression().evaluate(context);
                Iterable<Object> sourceData = ensureIterable(sourceObject);
                List<Object> target = new ArrayList<>();
                sourceData.forEach(target::add);
                multiQueryStack.addFirst(new AliasList(source.getAlias()).withBase(target));
            }

            // times operation results in list of Tuples
            AliasList result = times();
            int count = 0;
            // now do the operation
            for (Object tuple : result.getBase()) {
                try {
                    count = 0;
                    for (String key : ((Tuple) tuple).getElements().keySet()) {
                        Variable v = new Variable().withName(key).withValue(((Tuple) tuple).getElements().get(key));
                        context.push(v);
                        count++;
                    }
                    shouldInclude = true;
                    resolveRelationship(context);
                    resolveWhere(context);
                    if (shouldInclude)
                        returnList.add(resolveResult(context, tuple));
                } finally {
                    while (count > 0) {
                        context.pop();
                        count--;
                    }
                }
            }
        }
        finally {
            context.clearLetExpressions();
        }

        // collapse list of Tuples into a singleton Tuple list
        // returnList = collapse(returnList);

        // TODO: sorting for List<Tuple>
        if (returnList.size() > 0 && !(returnList.get(0) instanceof Tuple)) {
            sortResult(returnList, context, null);
        }

        return returnList;
    }

    // Not used, but keeping logic around in case of future implementation...
//    public List<Object> collapse(List toCollapse) {
//        List<Object> returnList = new ArrayList<>();
//        Tuple singletonTuple = new Tuple();
//        for (Object obj : toCollapse) {
//            for (String key : ((Tuple) obj).getElements().keySet()) {
//                if (singletonTuple.getElements().containsKey(key)
//                        && !((List)singletonTuple.getElements().get(key)).contains(((Tuple) obj).getElements().get(key))) {
//                    ((List)singletonTuple.getElements().get(key)).add(((Tuple) obj).getElements().get(key));
//                }
//                else {
//                    List<Object> resourceList = new ArrayList<>();
//                    resourceList.add(((Tuple) obj).getElements().get(key));
//                    singletonTuple.getElements().put(key, resourceList);
//                }
//            }
//        }
//
//        returnList.add(singletonTuple);
//        return returnList;
//    }

    public AliasList times() {
        while (multiQueryStack.size() > 1) {
            AliasList a = multiQueryStack.removeFirst();
            AliasList b = multiQueryStack.removeFirst();
            multiQueryStack.addFirst(cartesianProduct(a, b));
        }

        return multiQueryStack.removeFirst();
    }

    public AliasList cartesianProduct(AliasList a, AliasList b) {
        AliasList result = new AliasList(a.getName() + b.getName());
        for (Object o : a.getBase()) {
            for (Object oo : b.getBase()) {
                if (o instanceof Tuple) {
                    Tuple temp = new Tuple().withElements((HashMap<String, Object>) ((Tuple) o).getElements().clone());
                    temp.getElements().put(b.getName(), oo);
                    result.getBase().add(temp);
                }
                else {
                    HashMap<String, Object> tupleMap = new HashMap<>();
                    tupleMap.put(a.getName(), o);
                    tupleMap.put(b.getName(), oo);
                    result.getBase().add(new Tuple().withElements(tupleMap));
                }
            }
        }
        return result;
    }

    @Override
    public Object evaluate(Context context) {

        if (this.getLet().size() != 0) {
            resolveLet(context);
        }

        if (this.getSource().size() != 1) {
            return multisourceQuery(context);
        }

        org.cqframework.cql.elm.execution.AliasedQuerySource source = this.getSource().get(0);
        Object sourceObject = source.getExpression().evaluate(context);
        boolean sourceIsList = sourceObject instanceof Iterable;
        Iterable<Object> sourceData = ensureIterable(sourceObject);
        List<Object> result = new ArrayList<>();

        for (Object element : sourceData) {
            context.push(new Variable().withName(source.getAlias()).withValue(element));

            try {
                shouldInclude = true;
                resolveRelationship(context);
                resolveWhere(context);
                if (shouldInclude)
                    result.add(resolveResult(context, element));
            }
            finally {
                context.pop();
            }
        }

        if (this.getReturn() != null && this.getReturn().isDistinct()) {
            result = DistinctEvaluator.distinct(result);
        }

        sortResult(result, context, source.getAlias());

        if ((result == null || result.isEmpty()) && !sourceIsList) {
            return null;
        }

        return sourceIsList ? result : result.get(0);
    }
}
