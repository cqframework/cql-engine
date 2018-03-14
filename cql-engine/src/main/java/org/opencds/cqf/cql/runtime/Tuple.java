package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.EqualEvaluator;

import java.util.HashMap;

/**
* Created by Chris Schuler on 6/15/2016
*/
public class Tuple {

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

  public Boolean equal(Tuple other) {
    return this.similar(other, EqualEvaluator.SimilarityMode.EQUAL);
  }

  public Boolean similar(Tuple other, EqualEvaluator.SimilarityMode mode) {
    HashMap<String, Object> leftMap = getElements();
    HashMap<String, Object> rightMap = other.getElements();

    if (leftMap.size() != rightMap.size()) {
      return false;
    }

    for (String key : rightMap.keySet()) {
      if (leftMap.containsKey(key)) {
        Boolean similar = EqualEvaluator.similar(rightMap.get(key), leftMap.get(key), mode);
        if (similar == null) { return null; }
        else if (!similar) { return false; }
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
