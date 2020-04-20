package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.exception.CqlException;
import org.opencds.cqf.cql.exception.InvalidLiteral;
import org.opencds.cqf.cql.execution.Context;

import javax.xml.namespace.QName;
import java.math.BigDecimal;

public class LiteralEvaluator extends org.cqframework.cql.elm.execution.Literal {

    @Override
    protected Object internalEvaluate(Context context) {
        QName valueType = context.fixupQName(this.getValueType());
        switch (valueType.getLocalPart()) {
            case "Boolean": return Boolean.parseBoolean(this.getValue());
            case "Integer":
                int intValue;
                try {
                    intValue = Integer.parseInt(this.getValue());
                } catch(NumberFormatException e){
                    throw new CqlException("Bad format for Integer literal");
                }
                return intValue;
            case "Decimal":
                BigDecimal bigDecimalValue;

                try {
                    bigDecimalValue = new BigDecimal(this.getValue());
                } catch (NumberFormatException nfe) {
                    throw new CqlException(nfe.getMessage());
                }
                return bigDecimalValue;
            case "String": return this.getValue();
            default: throw new InvalidLiteral(String.format("Cannot construct literal value for type '%s'.", valueType.toString()));
        }
    }
}
