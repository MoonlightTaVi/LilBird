package com.github.tavi.lilbird.util;

import java.util.Arrays;
import java.util.List;

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

    /** 
     * A helper method to compare two lists of String arrays, 
     * for the name groups.
     * 
     * @return {@code true} If both lists are equal, {@code false} otherwise.
     */
    public static boolean equals(List<String[]> groupA, List<String[]> groupB) {
        if (groupA.size() != groupB.size())
            return false;
        for (int i = 0; i < groupA.size(); i++) {
            if (!Arrays.equals(groupA.get(i), groupB.get(i)))
                return false;
        }
        return true;
    }

}
