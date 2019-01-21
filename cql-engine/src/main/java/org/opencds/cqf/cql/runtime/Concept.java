package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.elm.execution.InEvaluator;
import org.opencds.cqf.cql.elm.execution.IntersectEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Concept implements CqlType {
    private String display;
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public Concept withDisplay(String display) {
        setDisplay(display);
        return this;
    }

    private List<Code> codes = new ArrayList<>();
    public Iterable<Code> getCodes() {
        return codes;
    }
    public void setCodes(Iterable<Code> codes) {
        this.codes.clear();
        if (codes != null) {
            for (Code code : codes) {
                this.codes.add(code);
            }
        }
    }
    public Concept withCodes(Iterable<Code> codes) {
        setCodes(codes);
        return this;
    }
    public Concept withCode(Code code) {
        codes.add(code);
        return this;
    }

    public Boolean equivalent(Object other) {
        List intersection = (List) IntersectEvaluator.intersect(this.codes, ((Concept) other).codes);
        return intersection != null && !intersection.isEmpty();
    }

    public Boolean equal(Object other) {
        Boolean codesAreEqual = EqualEvaluator.equal(this.codes, ((Concept) other).codes);
        Boolean displayIsEqual = EqualEvaluator.equal(this.display, ((Concept) other).display);
        return (codesAreEqual == null || displayIsEqual == null) ? null : codesAreEqual && displayIsEqual;

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("Concept {\n");
        for (Code code : getCodes()) {
            builder.append("\t").append(code.toString()).append("\n");
        }

        return builder.append("}").toString();
    }
}
