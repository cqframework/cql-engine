package org.cqframework.cql.runtime;

import java.util.*;

/**
* Created by Chris Schuler on 6/15/2016
*/
public class Tuple {

  protected HashMap<String, Object> elements;

  public HashMap<String, Object> getElements() {
    if (elements == null) { return new HashMap<String, Object>(); }
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
  public boolean equals(Object other) {
    if (other instanceof Tuple) {
      HashMap<String, Object> compareMap = ((Tuple)other).getElements();
      for (String key : compareMap.keySet()) {
        if (elements.containsKey(key)) {
          if (!Value.equals(compareMap.get(key), elements.get(key))) { return false; }
        }
        else {return false; }
      }
      return true;
      //return compareMap.keySet().equals(elements.keySet()) && compareMap.values().equals(elements.values());
    }
    else { return false; }
  }
}
