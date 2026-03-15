package com.github.tavi.lilbird.api.dto;

import com.github.tavi.lilbird.db.entities.BirdSynonymEntity;
import com.github.tavi.lilbird.models.BirdSynonym;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This DTO is used to create a new alternative name (synonym)
 * entry for a bird.
 */
@Data
@NoArgsConstructor
public class BirdSynonymDTO implements BirdSynonym {

    private String name;
    private String comment;


    /**
     * Prepares this Data Transfer Object for saving to the database
     * by converting it to {@link BirdSynonymEntity}.
     * <p>
     * <b>Warning</b>: Does not assign the reference to the original bird entry
     * automatically (because the DTO knows only about the ID of this entry).
     * 
     * @return          The database entity that contains 
     *                  most of the information from this DTO,
     *                  for saving.
     */
    public BirdSynonymEntity toEntity() {
        final BirdSynonymEntity entity = new BirdSynonymEntity();
        entity.setName(name);
        entity.setComment(comment);
        return entity;
    }
}
