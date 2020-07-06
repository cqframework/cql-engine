package org.opencds.cqf.cql.engine.execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.opencds.cqf.cql.engine.runtime.Tuple;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class RuntimeTests {
    @Test
    public void testQuantityToString() {
        Quantity q = new Quantity().withValue(null).withUnit(null);
        assertThat(q.toString(), is("null 'null'"));

        q = new Quantity();
        assertThat(q.toString(), is("0.0 '1'"));

        q = new Quantity().withValue(new BigDecimal("1.0")).withUnit("g");
        assertThat(q.toString(), is("1.0 'g'"));
    }

    @Test
    public void testTupleToString() {
        Tuple t = new Tuple();
        assertThat(t.toString(), is("Tuple { : }"));

        t = new Tuple();
        t.getElements().put("id", 1);
        t.getElements().put("value", new Quantity().withValue(new BigDecimal("1.0")).withUnit("g"));
        assertThat(t.toString(), is("Tuple {\n\t\"id\": 1\n\t\"value\": 1.0 'g'\n}"));
    }
}
