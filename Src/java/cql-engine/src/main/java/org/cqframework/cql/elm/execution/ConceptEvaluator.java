package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Code;

import java.util.ArrayList;

public class ConceptEvaluator extends Concept {

  @Override
  public Object evaluate(Context context) {
    ArrayList<Code> codes = new ArrayList<>();
    for (int i = 0; i < this.getCode().size(); ++i) {
      codes.add((Code)this.getCode().get(i).evaluate(context));
    }
    String display = this.getDisplay();
    return new org.cqframework.cql.runtime.Concept().withCodes(codes).withDisplay(display);
  }
}
