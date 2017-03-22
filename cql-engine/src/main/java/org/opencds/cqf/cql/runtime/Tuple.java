package org.opencds.cqf.cql.runtime;

import java.util.HashMap;

/**
* Created by Chris Schuler on 6/15/2016
*/
public class Tuple {

  protected HashMap<String, Object> elements;

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
  public String toString() {
    StringBuilder builder = new StringBuilder("Tuple {\n");
    for (String key : elements.keySet()) {
      builder.append("\t").append(key).append(" -> ").append(elements.get(key)).append("\n");
    }
    return builder.append("}").toString();
  }
}
