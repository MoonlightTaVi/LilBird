package com.github.tavi.lilbird.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * The base interface for a bird alternative names model.
 */
public interface NameGroupModel {

    /**
     * A group of alternative names for the bird that
     * share the same etymology.
     */
    @Schema(
        description = "A group of alternative names for this bird",
        type = "array",
        example = "[\"Owl\"]"
    )
    public List<String> getNames();

    /**
     * Returns the etymology of the names.
     */
    @Schema(
        description = "(Optional) Etymology for this group of names",
        example = "They are called 'owls' because the are 'owling' in the night :)"
    )
    public String getEtymology();

    /**
     * Sets the etymology of the names.
     */
    public void setEtymology(String etymology);

    /**
     * Adds a new name to the group.
     * 
     * @param name          A new alternative name.
     */
    public void addName(String name);


    /**
     * Sets the lists of names from the source.
     * <p>
     * The default implementation add each name separately,
     * using the {@link #addName(String)} method,
     * due to the fact that the returned list may be unmodifyable.
     * 
     * @param names         A list of alternative names.
     */
    public default void setNames(final List<String> names) {
        for (final String name : names)
            addName(name);
    }
    
    /**
     * Copies the state from the other model.
     * 
     * @param o             Some other name group.
     */
    public default void copyFrom(final NameGroupModel o) {
        setEtymology(o.getEtymology());
        setNames(o.getNames());
    }

}
