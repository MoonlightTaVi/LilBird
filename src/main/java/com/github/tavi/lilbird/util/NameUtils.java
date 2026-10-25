package com.github.tavi.lilbird.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;


/** Utility methods for bird names. */
public class NameUtils {

    /**
     * Normalizes the given string to {@link Form#NFC}, also
     * removing the trailing and consequtive white space.
     * 
     * @param text      Some string to normalize.
     * @return          This string, normalized (e.g. for searching).
     */
    public static String normalize(String text) {
        return Normalizer.normalize(text, Form.NFC)
            .trim()
            .replaceAll("\\s+", " ")
            .toLowerCase(Locale.ENGLISH);
    }

    /**
     * Same as {@link NameUtils#normalize(String)}, but with a custom locale.
     * 
     * @param text      Some string to normalize.
     * @param locale    The locale of this text.
     * @return          This string, normalized (e.g. for searching).
     */
    public static String normalize(String text, Locale locale) {
        return Normalizer.normalize(text, Form.NFC)
            .trim()
            .replaceAll("\\s+", " ")
            .toLowerCase(locale);
    }

}
