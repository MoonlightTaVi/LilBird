package com.github.tavi.lilbird.enums;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Identifies the type of the bird taxon in biological systematics. */
public enum TaxonType {

    /** E.g. "Passeriformes" for the passerine. */
    ORDER("order"),
    /** E.g. "Corvidae" for the corvids. */
    FAMILY("family"),
    /** E.g. "Corvus". */
    GENUS("genus"),
    /** E.g. "Corvus Corax" for the common raven. */
    SPECIES("species");


    /** Returns the {@link TaxonType} that corresponds to this textual value. */
    @JsonCreator
    public static TaxonType fromTextValue(String source) {
        for (TaxonType t : TaxonType.values()) {
            if (t.textValue.equals(source))
                return t;
        }
        throw new IllegalArgumentException("Unknown taxon type: " + source);
    }


    /** The {@link Converter} to be used by Spring web confguration. */
    public static class TaxonTypeConverter implements Converter<String,TaxonType> {

        @Override
        public TaxonType convert(String source) {
            return TaxonType.fromTextValue(source);
        }

    }


    /** The textual representation of this enum, for REST API. */
    private final String textValue;

    private TaxonType(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public String toString() {
        return textValue;
    }


}
