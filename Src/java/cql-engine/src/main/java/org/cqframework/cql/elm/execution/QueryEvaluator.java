package org.cqframework.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.execution.Variable;

import java.util.*;

import static org.cqframework.cql.runtime.Value.ensureIterable;

/**
 * Created by Bryn on 5/25/2016.
 */
public class QueryEvaluator extends Query {

    @Override
    public Object evaluate(Context context) {
        // Single-source query with where clause only at this point
        if (this.getSource().size() != 1) {
            throw new NotImplementedException("Multi-source queries are not currently implemented.");
        }

        if (this.getLet().size() != 0) {
            throw new NotImplementedException("Let clauses within queries are not currently implemented.");
        }

        if (this.getSort() != null) {
            throw new NotImplementedException("Sort clause within a query is not currently implemented.");
        }

        AliasedQuerySource source = this.getSource().get(0);
        Object sourceObject = source.getExpression().evaluate(context);
        boolean sourceIsList = sourceObject instanceof Iterable;
        Iterable<Object> sourceData = ensureIterable(sourceObject);
        java.util.List<Object> result = new ArrayList<Object>();

        for (Object element : sourceData) {
            context.push(new Variable().withName(source.getAlias()).withValue(element));
            try {
                boolean shouldInclude = true;

                // TODO: This is the most naive possible implementation here, but it should perform okay with 1) caching and 2) small data sets
                for (RelationshipClause relationship : getRelationship()) {
                    boolean hasSatisfyingData = false;
                    Iterable<Object> relatedSourceData = ensureIterable(relationship.getExpression().evaluate(context));
                    for (Object relatedElement : relatedSourceData) {
                        context.push(new Variable().withName(relationship.getAlias()).withValue(relatedElement));
                        try {
                            Object satisfiesRelatedCondition = relationship.getSuchThat().evaluate(context);
                            if (relationship instanceof With) {
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

                    if ((relationship instanceof With && !hasSatisfyingData) || (relationship instanceof Without && hasSatisfyingData)) {
                        shouldInclude = false;
                        break; // Once we have determined the row should not be included, no need to continue testing other related information
                    }
                }

                if (shouldInclude && getWhere() != null) {
                    Object satisfiesCondition = this.getWhere().evaluate(context);
                    if (!(satisfiesCondition instanceof Boolean && (Boolean)satisfiesCondition)) {
                        shouldInclude = false;
                    }
                }

                if (shouldInclude) {
                    if (this.getReturn() != null) {
                        Object returnValue = this.getReturn().getExpression().evaluate(context);
                        result.add(returnValue);
                    }
                    else {
                        result.add(element);
                    }
                }
            }
            finally {
                context.pop();
            }
        }

        if (this.getReturn() != null && this.getReturn().isDistinct()) {
            result = DistinctEvaluator.distinct(result);
        }

        if (sourceIsList) {
            return result;
        }
        else {
            return result.size() == 1 ? result.get(0) : null;
        }
    }
}
