package com.github.tavi.lilbird.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * The base interface for a bird entry model,
 * implemented by database entities and REST API data objects.
 */
public interface BirdEntry {

    /**
     * The primary (unique) name of the entry.
     * May be used as an index for an optimized search.
     */
    @NotBlank(
        message = "The primary name cannot be blank"
    )
    @Size(
        max = 16, 
        message = "The primary name must be concise (< 16 symbols)"
    )
    @Pattern(
        message = "The title must contain only latin symbols, digits and hyphens",
        regexp = "[0-9a-zA-Z\\-\s]+"
    )
    @Schema(
        description = "The primary (unique) name, in latin symbols",
        example = "Corvus corax"
    )
    public String getTitle();

    /**
     * The secondary (optional) name of the entry.
     */
    @Size(
        max = 20, 
        message = "The secondary name must be concise (< 20 symbols)"
    )
    @Schema(
        description = "(Optionally) Some more common name of the bird",
        example = "Common raven"
    )
    public String getCommonName();

}
