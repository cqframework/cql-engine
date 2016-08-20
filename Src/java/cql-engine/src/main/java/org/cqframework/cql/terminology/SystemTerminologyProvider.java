package org.cqframework.cql.terminology;

import org.cqframework.cql.runtime.Code;

/**
* Created by Chris on 8/19/2016
*/
public class SystemTerminologyProvider implements TerminologyProvider {
  @Override
  public boolean in(Code code, ValueSetInfo valueSet) {
    return false;
  }

  @Override
  public Iterable<Code> expand(ValueSetInfo valueSet) {
    return null;
  }

  @Override
  public Code lookup(Code code, CodeSystemInfo codeSystem) {
    return null;
  }
}
