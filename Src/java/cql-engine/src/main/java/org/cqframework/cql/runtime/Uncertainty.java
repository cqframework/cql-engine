package org.cqframework.cql.runtime;

import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Interval;

import org.joda.time.IllegalFieldValueException;

import java.util.ArrayList;
import java.util.Arrays;

/**
* Created by Chris Schuler on 6/25/2016
*/
public class Uncertainty {
  private Interval uncertainty;

  public Interval getUncertaintyInterval() {
    return uncertainty;
  }

  public void setUncertaintyInterval(Interval uncertainty) {
    this.uncertainty = uncertainty;
  }

  public Uncertainty withUncertaintyInterval(Interval uncertainty) {
    setUncertaintyInterval(uncertainty);
    return this;
  }

  public static Interval toUncertainty(Object point) {
    return new Interval(point, true, point, true);
  }

  public static boolean isUncertain(DateTime dt, String precision) {
    try {
      int test = dt.getPartial().getValue(dt.getFieldIndex(precision));
    } catch (IndexOutOfBoundsException e) {
      return true;
    }
    return false;
  }

  /**
  This method's purpose is to return a list of DateTimes with max and min values
  For example:
  DateTime(2012) where precision is days
  Would result in the following DateTimes being returned:
  low = (2012, 1, 1)
  high = (2012, 12, 31)
  The uncertainty interval can then be constructed by running the high and low DateTimes
  through the operation that called this method.
  So, the following expression:
  days between DateTime(2012) and DateTime(2013, 10, 15)
  would result in evaluating
  days between DateTime(2012, 12, 31) and DateTime(2013, 10, 15) -- for the low point of the interval
  and
  days between DateTime(2012, 1, 1) and DateTime(2013, 10, 15) -- for the high point of the interval
  */
  public static ArrayList<DateTime> getHighLowList(DateTime uncertain, String precision) {
    if (isUncertain(uncertain, precision)) {
      DateTime low = new DateTime().withPartial(uncertain.getPartial());
      DateTime high = new DateTime().withPartial(uncertain.getPartial());

      int idx = DateTime.getFieldIndex(precision);
      if (idx == -1) { idx = DateTime.getFieldIndex2(precision); }
      if (idx != -1) {
        // expand the high and low date times with respective max and min values
        for (int i = uncertain.getPartial().size(); i < idx + 1; ++i) {
          low.setPartial(low.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMinimumValue()));

          if (i == 2) {
            // the method used here to determine the max number of days in a given month is pretty hacky
            int days = 28;
            try {
              while (true) {
                high.setPartial(high.getPartial().with(DateTime.getField(i), days++));
              }
            } catch (IllegalFieldValueException e) {
              high.setPartial(high.getPartial().with(DateTime.getField(i), days - 2));
            }
          }

          else {
            high.setPartial(high.getPartial().with(DateTime.getField(i), DateTime.getField(i).getField(null).getMaximumValue()));
          }
        }
        return new ArrayList<DateTime>(Arrays.asList(low, high));
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration unit: %s", precision));
      }
    }

    throw new IllegalArgumentException("Specified DateTime is not uncertain.");
  }

}
