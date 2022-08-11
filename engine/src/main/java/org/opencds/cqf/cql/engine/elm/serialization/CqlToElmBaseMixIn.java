package org.opencds.cqf.cql.engine.elm.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonIgnoreProperties("type")
public interface CqlToElmBaseMixIn {}