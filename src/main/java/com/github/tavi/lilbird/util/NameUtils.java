package com.github.tavi.lilbird.util;


/** Utility methods for bird names. */
public class NameUtils {

    /**
     * This special symbol is used to separate names in a string.
     */
    public static String NAME_DELIMITER = ";";

    /**
     * Converts a name to a normal form.
     * <p>
     * The normal form is case-insensitive and does not include spaces.
     * 
     * @param title     The original name for the bird entry.
     * @return          The same name, but normalized
     */
    public static String normalize(final String title) {
        return title.toLowerCase()
                .replace("\s", "+");
    }

}
