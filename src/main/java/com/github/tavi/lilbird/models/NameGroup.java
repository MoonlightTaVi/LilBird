package com.github.tavi.lilbird.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * The base interface for a bird alternative names model.
 */
public interface NameGroup {

    /** A group of alternative names with the same etymology. */
    @Schema(
        description = "A group of alternative names for this bird",
        type = "array",
        example = "[\"Owl\"]"
    )
    public List<String> getNames();

    /** Sets the lists of names from the source. */
    public void setNames(final List<String> names);

}
