package com.github.tavi.lilbird.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * The base interface for a bird alternative name model,
 * implemented by database entities and REST API data objects.
 */
public interface BirdSynonym {

    /**
     * The synonymous name for the bird.
     */
    @NotBlank(
        message = "The alternative name cannot be blank or null"
    )
    @Size(
        max = 40
    )
    @Schema(
        description = "The alternative name (synonym) of this bird",
        example = "Owl"
    )
    public String getName();

    /**
     * Some additional comment on this synonym
     * (for example, used language, dialect, etc.);
     * may be a JSON string.
     */
    @Schema(
        description = "(Optional) Additional comment on this synonym",
        example = "They are called 'owls' because the are 'owling' in the night :)"
    )
    public String getComment();

}
