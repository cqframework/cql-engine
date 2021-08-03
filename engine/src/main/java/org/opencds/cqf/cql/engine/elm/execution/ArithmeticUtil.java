package org.opencds.cqf.cql.engine.elm.execution;

import java.util.HashMap;

public class ArithmeticUtil {

    private static HashMap<String, Integer> TYPE_WEIGHT = new HashMap<>();
    static {
        TYPE_WEIGHT.put("Integer", 1);
        TYPE_WEIGHT.put("Long", 2);
        TYPE_WEIGHT.put("BigDecimal", 3);
    }

    public static String determineClassTypeForArithmetic(Object left, Object right) {
        String leftType = left.getClass().getSimpleName();
        String rightType = right.getClass().getSimpleName();

        if (TYPE_WEIGHT.containsKey(leftType) && TYPE_WEIGHT.containsKey(rightType)) {

            if (TYPE_WEIGHT.get(leftType) >= TYPE_WEIGHT.get(rightType)) {
                return leftType;
            }
            return rightType;
        } else {
            return "";
        }
    }
}
