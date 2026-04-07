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
     * Etymology of the names.
     */
    @Schema(
        description = "(Optional) Etymology for this group of names",
        example = "They are called 'owls' because the are 'owling' in the night :)"
    )
    public String getEtymology();

    
    /**
     * Adds a new name to the group.
     * 
     * @param name          A new alternative name.
     */
    public void addName(String name);

    /**
     * Copies the state from the other model.
     * 
     * @param o             Some other name group.
     */
    public void copyFrom(NameGroupModel o);

}
