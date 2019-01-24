package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;
import java.util.List;

/*

Product(argument List<Integer>) Integer
Product(argument List<Decimal>) Decimal
Product(argument List<Quantity>) Quantity

The Product operator returns the geometric product of the elements in source.

If the source contains no non-null elements, null is returned.

If the source is null, the result is null.

*/

public class ProductEvaluator extends org.cqframework.cql.elm.execution.Product {

    public static Object product(Iterable source) {
        if (source == null) {
            return null;
        }

        Object result = null;
        for (Object element : source) {
            if (element == null) return null;
            if (result == null) {
                result = element;
                continue;
            }
            if ((element instanceof Integer && result instanceof Integer)
                    || (element instanceof BigDecimal && result instanceof BigDecimal))
            {
                result = MultiplyEvaluator.multiply(result, element);
            }
            else if (element instanceof Quantity && result instanceof Quantity) {
                if (!((Quantity) element).getUnit().equals(((Quantity) result).getUnit())) {
                    // TODO: try to normalize units?
                    throw new IllegalArgumentException(
                            String.format(
                                    "Found different units during Quantity product evaluation: %s and %s",
                                    ((Quantity) element).getUnit(), ((Quantity) result).getUnit()
                            )
                    );
                }
                ((Quantity) result).setValue(
                        (BigDecimal) MultiplyEvaluator.multiply(
                                ((Quantity) result).getValue(),
                                ((Quantity) element).getValue()
                        )
                );
            }
            else {
                throw new IllegalArgumentException(
                        String.format(
                                "Cannot perform product operation on arguments of type: %s and %s",
                                result.getClass().getName(), element.getClass().getName()
                        )
                );
            }
        }

        return result;
    }

    @Override
    public Object evaluate(Context context) {
        List source = (List) getSource().evaluate(context);

        return product(source);
    }
}
