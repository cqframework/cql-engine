package org.cqframework.cql.terminology;

import org.cqframework.cql.runtime.Code;

/**
 * Created by Bryn on 8/2/2016.
 */
public interface TerminologyProvider {
    boolean in(Code code, ValueSetInfo valueSet);
    Iterable<Code> expand(ValueSetInfo valueSet);
    Code lookup(Code code, CodeSystemInfo codeSystem);
}
