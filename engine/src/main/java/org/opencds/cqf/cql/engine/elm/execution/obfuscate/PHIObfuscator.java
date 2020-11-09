package org.opencds.cqf.cql.engine.elm.execution.obfuscate;

import javax.annotation.Nullable;

public interface PHIObfuscator {
    String obfuscate(@Nullable Object source);
}
