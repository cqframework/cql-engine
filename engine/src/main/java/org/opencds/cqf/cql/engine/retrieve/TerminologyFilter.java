package org.opencds.cqf.cql.engine.retrieve;

import org.opencds.cqf.cql.engine.runtime.Code;

import java.util.ArrayList;
import java.util.List;

public class TerminologyFilter {
    public TerminologyFilter(String codePath, Iterable<Code> codes, String valueSet) {
        this.codePath = codePath;
        if (codes != null) {
            this.codes = new ArrayList<Code>();
            for (Code c : codes) {
                this.codes.add(c);
            }
        }
        this.valueSet = valueSet;
    }
    private String codePath;
    public String getCodePath() {
        return codePath;
    }

    private List<Code> codes;
    public Iterable<Code> getCodes() {
        return codes;
    }

    private String valueSet;
    public String getValueSet() {
        return valueSet;
    }
}
