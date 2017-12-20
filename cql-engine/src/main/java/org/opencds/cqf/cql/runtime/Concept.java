package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.InEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Concept {
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
        for (Code code : codes) {
            this.codes.add(code);
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

    public Boolean equal(Concept other) {
        for (Code code : this.getCodes()) {
            if (!InEvaluator.in(code, other.getCodes(), null)) {
                return false;
            }
        }

        return true;
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
