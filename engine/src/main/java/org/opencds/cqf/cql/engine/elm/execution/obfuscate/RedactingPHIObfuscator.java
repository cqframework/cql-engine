package org.opencds.cqf.cql.engine.elm.execution.obfuscate;

import javax.annotation.Nullable;

public class RedactingPHIObfuscator implements PHIObfuscator {

    public static final String REDACTED_MESSAGE = "<redacted>";

    @Override
    public String obfuscate(@Nullable Object source) {
        return REDACTED_MESSAGE;
    }

}
