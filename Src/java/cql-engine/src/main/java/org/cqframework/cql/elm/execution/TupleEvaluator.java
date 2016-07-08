package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.elm.execution.TupleElement;

import java.util.HashMap;

/**
 * Created by Chris Schuler on 6/15/2016.
 */
public class TupleEvaluator extends Tuple {

  @Override
  public Object evaluate(Context context) {
    HashMap<String, Object> ret = new HashMap<>();
    for (TupleElement element : this.getElement()) {
      ret.put(element.getName(), element.getValue().evaluate(context));
    }
    return new org.cqframework.cql.runtime.Tuple().withElements(ret);
  }
}
