package org.opencds.cqf.cql.engine.terminology;

import org.opencds.cqf.cql.engine.runtime.Code;

public interface TerminologyProvider {
    boolean in(Code code, ValueSetInfo valueSet);
    Iterable<Code> expand(ValueSetInfo valueSet);
    Code lookup(Code code, CodeSystemInfo codeSystem);
}
