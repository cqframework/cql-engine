package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.elm.execution.AliasedQuerySource;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;
import org.opencds.cqf.cql.runtime.Value;

import java.util.ArrayList;
import java.util.List;

import static org.opencds.cqf.cql.runtime.Value.ensureIterable;

/**
 * Created by Bryn on 5/25/2016.
 */
public class QueryEvaluator extends org.cqframework.cql.elm.execution.Query {

    private boolean shouldInclude;

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
        if (shouldInclude) {
            if (this.getReturn() != null)
                return this.getReturn().getExpression().evaluate(context);
        }
        return element;
    }

    public void sortResult(List<Object> result) {
        org.cqframework.cql.elm.execution.SortClause sortClause = this.getSort();
        if (sortClause != null) {
            for (org.cqframework.cql.elm.execution.SortByItem byItem : sortClause.getBy()) {
                String direction = byItem.getDirection().value();
                if (direction == null || direction.equals("asc") || direction.equals("ascending")) {
                    java.util.Collections.sort(result, Value.valueSort);
                }
                else if (direction.equals("desc") || direction.equals("descending")) {
                    java.util.Collections.sort(result, Value.valueSort);
                    java.util.Collections.reverse(result);
                }
            }
        }
    }

    public Object multisourceQuery(Context context) {
        // pushing variables
        boolean sourceIsList = false;
        for (AliasedQuerySource source : this.getSource()) {
            Object sourceObject = source.getExpression().evaluate(context);
            sourceIsList = sourceObject instanceof Iterable;
            Iterable<Object> sourceData = ensureIterable(sourceObject);

            for (Object element : sourceData) {
                if (sourceIsList) {
                    Variable v = new Variable().withName(source.getAlias()).withValue(element);
                    v.setIsList(true);
                    context.push(v);
                }
                else
                    context.push(new Variable().withName(source.getAlias()).withValue(element));
            }
        }

        List<Object> result = new ArrayList<>();
        for (AliasedQuerySource source : this.getSource()) {
            Object sourceObject = source.getExpression().evaluate(context);
            Iterable<Object> sourceData = ensureIterable(sourceObject);

            for (Object element : sourceData) {
                shouldInclude = true;
                resolveRelationship(context);
                resolveWhere(context);
                result.add(resolveResult(context, element));
            }
        }

        if (this.getReturn() != null && this.getReturn().isDistinct()) {
            result = DistinctEvaluator.distinct(result);
        }
        sortResult(result);

        context.pop();
        // TODO: not sure if this is right --> sourceIsList logic...
        return sourceIsList ? result : result.get(0);
    }

    @Override
    public Object evaluate(Context context) {
        // Single-source query with where clause only at this point
        if (this.getSource().size() != 1) {
            return multisourceQuery(context);
        }

        if (this.getLet().size() != 0) {
            throw new NotImplementedException("Let clauses within queries are not currently implemented.");
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
                result.add(resolveResult(context, element));
            }
            finally {
                context.pop();
            }
        }

        if (this.getReturn() != null && this.getReturn().isDistinct()) {
            result = DistinctEvaluator.distinct(result);
        }
        sortResult(result);

        return sourceIsList ? result : result.get(0);
    }
}
