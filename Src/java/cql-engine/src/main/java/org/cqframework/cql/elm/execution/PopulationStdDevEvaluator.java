package org.cqframework.cql.elm.execution;

import org.apache.commons.math3.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
* The PopulationStdDev operator returns the statistical standard deviation of the elements in source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Return types: BigDecimal & Quantity
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class PopulationStdDevEvaluator extends PopulationStdDev {

  public static Object popStdDev(Object source) {
    if (source instanceof Iterable) {
      Iterable<Object> element = (Iterable<Object>)source;
      Iterator<Object> itr = element.iterator();

      if (!itr.hasNext()) { return null; } // empty list

      DescriptiveStatistics stats = new DescriptiveStatistics();
      double popVariance = 0.0;
      Object value = itr.next();
      while (value == null) { value = itr.next(); }

      if (value instanceof BigDecimal) {
        stats.addValue(((BigDecimal)value).doubleValue());
        while (itr.hasNext()) {
          BigDecimal next = (BigDecimal)itr.next();
          if (next != null) { stats.addValue(next.doubleValue()); }
        }
        popVariance = stats.getPopulationVariance();
        return new BigDecimal(Math.sqrt(popVariance)).setScale(8, RoundingMode.FLOOR);
      }

      else if (value instanceof Quantity) {
        stats.addValue((((Quantity)value).getValue()).doubleValue());
        while (itr.hasNext()) {
          BigDecimal next = ((Quantity)itr.next()).getValue();
          if (next != null) { stats.addValue(next.doubleValue()); }
        }
        popVariance = stats.getPopulationVariance();
        return new Quantity().withValue(new BigDecimal(Math.sqrt(popVariance)).setScale(8, RoundingMode.FLOOR)).withUnit(((Quantity)value).getUnit());
      }

      throw new IllegalArgumentException(String.format("Cannot PopulationStdDev arguments of type '%s'.", value.getClass().getName()));
    }
    return null; 
  }

  @Override
  public Object evaluate(Context context) {
    Object source = getSource().evaluate(context);
    if (source == null) { return null; }

    return popStdDev(source);
  }
}
