package com.github.tavi.lilbird.api.dto;

import com.github.tavi.lilbird.db.entities.BirdSynonym;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This DTO is used to create a new alternative name (synonym)
 * entry for a bird.
 */
@Data
@NoArgsConstructor
public class BirdSynonymDTO {

    @NotBlank
    @Size(max = 40)
    @Schema(
        description = "The alternative name (synonym) of this bird",
        example = "Owl"
    )
    private String value;

    @Schema(
        description = "(Optional) Additional comment on this synonym",
        example = "They are called 'owls' because the are 'owling' in the night :)"
    )
    private String comment;


    /**
     * Prepares this Data Transfer Object for saving to the database
     * by converting it to {@link BirdSynonym}.
     * <p>
     * <b>Warning</b>: Does not assign the reference to the original bird entry
     * automatically (because the DTO knows only about the ID of this entry).
     * 
     * @return          The database entity that contains 
     *                  most of the information from this DTO,
     *                  for saving.
     */
    public BirdSynonym toEntity() {
        final BirdSynonym entity = new BirdSynonym();
        entity.setName(value);
        entity.setComment(comment);
        return entity;
    }
}
