package org.opencds.cqf.cql.runtime;

public interface CqlType {
    Boolean equivalent(Object other);
    Boolean equal(Object other);
}
