package org.opencds.cqf.cql.engine.elm.execution.obfuscate;

import javax.annotation.Nullable;

public class NoOpPHIObfuscator implements PHIObfuscator {

    @Override
    public String obfuscate(@Nullable Object source) {
        return String.valueOf(source);
    }

}
