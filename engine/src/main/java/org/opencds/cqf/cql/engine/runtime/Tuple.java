package org.opencds.cqf.cql.engine.runtime;

import org.opencds.cqf.cql.engine.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.ToStringEvaluator;

import java.util.HashMap;
import java.util.Map;

public class Tuple implements CqlType {

    protected HashMap<String, Object> elements;

    public Tuple() {
        this.elements = new HashMap<>();
    }

    public Object getElement(String key) {
        return elements.get(key);
    }

    public HashMap<String, Object> getElements() {
        if (elements == null) { return new HashMap<>(); }
        return elements;
    }

    public void setElements(HashMap<String, Object> elements) {
        this.elements = elements;
    }

    public Tuple withElements(HashMap<String, Object> elements) {
        setElements(elements);
        return this;
    }

    @Override
    public Boolean equivalent(Object other) {
        if (this.getElements().size() != ((Tuple) other).getElements().size()) {
            return false;
        }

        for (String key : ((Tuple) other).getElements().keySet()) {
            if (this.getElements().containsKey(key)) {
                Object areKeyValsSame = EquivalentEvaluator.equivalent(((Tuple) other).getElements().get(key), this.getElements().get(key));
                if (!(Boolean) areKeyValsSame) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean equal(Object other) {
        if (this.getElements().size() != ((Tuple) other).getElements().size()) {
            return false;
        }

        for (String key : ((Tuple) other).getElements().keySet()) {
            if (this.getElements().containsKey(key)) {
                if (((Tuple) other).getElements().get(key) == null
                        && this.getElements().get(key) == null)
                {
                    continue;
                }
                Boolean equal = EqualEvaluator.equal(((Tuple) other).getElements().get(key), this.getElements().get(key));
                if (equal == null) { return null; }
                else if (!equal) { return false; }
            }
            else { return false; }
        }

        return true;
    }

    @Override
    public String toString() {
        if (elements.size() == 0) {
            return "Tuple { : }";
        }

        StringBuilder builder = new StringBuilder("Tuple {\n");
        for (Map.Entry<String, Object> entry : elements.entrySet()) {
            builder.append("\t\"").append(entry.getKey()).append("\": ").append(ToStringEvaluator.toString(entry.getValue())).append("\n");
        }
        return builder.append("}").toString();
    }
}
