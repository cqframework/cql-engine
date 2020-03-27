package org.opencds.cqf.cql;

public class Helpers {
    public static boolean isFileUri(String uri) {
        if (uri == null) {
            return false;
        }

        return uri.startsWith("file") || !uri.matches("\\w+?://.*");
    }
}