package com.github.tavi.lilbird.util;

import java.util.Locale;


/** Utility methods for bird names. */
public class NameUtils {

    /**
     * Converts a latin name to the form used as an id.
     * Leading and trailing white space is removed. 
     * The result is lower-case ({@link Locale#ENGLISH}) 
     * and each remaining run of white space becomes {@code +}.
     *
     * @param title     The original latin name.
     * @return          The same name, normalized.
     */
    public static String normalize(final String title) {
        return title.trim()
            .replaceAll("\\s+", "+")
            .toLowerCase(Locale.ENGLISH);
    }

}
