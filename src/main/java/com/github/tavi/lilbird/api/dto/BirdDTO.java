package com.github.tavi.lilbird.api.dto;

import com.github.tavi.lilbird.db.entities.BirdEntry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Data Transfer Object for a bird entry.
 * 
 * @see BirdEntry
 */
@Data
@NoArgsConstructor
public class BirdDTO {

    /**
     * The primary (unique) name of the bird entry.
     */
    @NotNull(message = "The primary name must be set")
    @NotBlank(message = "The primary name cannot be blank")
    @Size(
        max = 16, 
        message = "The primary name must be concise (< 16 symbols)"
    )
    @Schema(
        description = "The primary name of the bird",
        example = "Corvus corax"
    )
    private String title;

    /**
     * The secondary (optional) name of the bird entry.
     */
    //@NotBlank(message = "The secondary name cannot be blank")
    @Size(
        max = 20, 
        message = "The secondary name must be concise (< 20 symbols)"
    )
    @Schema(
        description = "(Optionally) Some more common name of the bird",
        example = "Common raven"
    )
    private String commonName = null;


    /**
     * Converts this DTO to a new database entry (only for creating).
     * 
     * @return A new entry to be saved to the database.
     */
    public BirdEntry toEntity() {
        final BirdEntry entity = new BirdEntry();
        entity.setTitle(title);
        entity.setCommonName(commonName);
        return entity;
    }
}
