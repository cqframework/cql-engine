package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class RuntimeTests {
    @Test
    public void testQuantityToString() {
        Quantity q = new Quantity().withValue(null).withUnit(null);
        assertThat(q.toString(), is("null null"));

        q = new Quantity();
        assertThat(q.toString(), is("0.0 1"));

        q = new Quantity().withValue(new BigDecimal("1.0")).withUnit("g");
        assertThat(q.toString(), is("1.0 g"));
    }
}
