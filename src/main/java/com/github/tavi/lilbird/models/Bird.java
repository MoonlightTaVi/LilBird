package com.github.tavi.lilbird.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/** The base interface for a bird name entry model. */
public interface Bird {

    /** The unique latin name of the bird. */
    @NotBlank(message = "The primary name cannot be blank")
    @Size(max = 16, message = "The primary name must be concise (< 16 symbols)")
    @Pattern(
        message = "The title must contain only latin symbols, digits and hyphens",
        regexp = "[0-9a-zA-Z\\-\s]+"
    )
    @Schema(
        description = "The primary (unique) name, in latin symbols",
        example = "Bubo scandiacus"
    )
    public String getNameLatin();
    
    /** The most common name (may be {@code null{}). */
    @Size(max = 32, message = "The name must be concise (< 32 symbols)")
    @Schema(
        description = "An optional primary common name",
        example = "Snowy owl"
    )
    public String getNameMain();

}
