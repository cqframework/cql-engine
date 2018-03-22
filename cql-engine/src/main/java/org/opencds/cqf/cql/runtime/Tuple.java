package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;

import java.util.HashMap;

/**
 * Created by Chris Schuler on 6/15/2016
 */
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
        StringBuilder builder = new StringBuilder("Tuple {\n");
        for (String key : elements.keySet()) {
            builder.append("\t").append(key).append(" -> ").append(elements.get(key)).append("\n");
        }
        return builder.append("}").toString();
    }
}
