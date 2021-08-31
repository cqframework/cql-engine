package org.opencds.cqf.cql.engine.elm.execution;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.elm.execution.*;
import org.opencds.cqf.cql.engine.debug.DebugAction;
import org.opencds.cqf.cql.engine.debug.SourceLocator;
import org.opencds.cqf.cql.engine.exception.CqlException;
import org.opencds.cqf.cql.engine.execution.Context;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Executable
{
    public void prepare(Context context) {
        // Visiting behavior is implemented here for abstract expression types
        // Since the evaluator descendants will only be instantiated for leaf classes
        if (this instanceof UnaryExpression) {
            UnaryExpression unaryExpression = (UnaryExpression)this;
            if (unaryExpression.getOperand() != null) {
                unaryExpression.getOperand().prepare(context);
            }
        }
        else if (this instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression)this;
            for (Expression operand : binaryExpression.getOperand()) {
                operand.prepare(context);
            }
        }
        else if (this instanceof TernaryExpression) {
            TernaryExpression ternaryExpression = (TernaryExpression)this;
            for (Expression operand : ternaryExpression.getOperand()) {
                operand.prepare(context);
            }
        }
        else if (this instanceof NaryExpression) {
            NaryExpression naryExpression = (NaryExpression)this;
            for (Expression operand : naryExpression.getOperand()) {
                operand.prepare(context);
            }
        }
        else if (this instanceof AggregateExpression) {
            AggregateExpression aggregateExpression = (AggregateExpression)this;
            if (aggregateExpression.getSource() != null) {
                aggregateExpression.getSource().prepare(context);
            }
        }
    }

    public Object evaluate(Context context) throws CqlException
    {
        try {
            DebugAction action = context.shouldDebug(this);
            Object result = internalEvaluate(context);
            if (action != DebugAction.NONE) {
                context.logDebugResult(this, result, action);
            }
            return result;
        }
        catch (Exception e) {
            if (e instanceof CqlException) {
                CqlException ce = (CqlException)e;
                if (ce.getSourceLocator() == null) {
                    ce.setSourceLocator(SourceLocator.fromNode(this, context.getCurrentLibrary()));
                    DebugAction action = context.shouldDebug(ce);
                    if (action != DebugAction.NONE) {
                        context.logDebugError(ce);
                    }
                }
                throw e;
            }
            else {
                CqlException ce = new CqlException(e, SourceLocator.fromNode(this, context.getCurrentLibrary()));
                DebugAction action = context.shouldDebug(ce);
                if (action != DebugAction.NONE) {
                    context.logDebugError(ce);
                }
                throw ce;
            }
        }
    }

    protected Object internalEvaluate(Context context) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }
}
