package com.github.tavi.lilbird.api.dto;

import com.github.tavi.lilbird.db.entities.BirdEntryEntity;
import com.github.tavi.lilbird.models.BirdEntry;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Data Transfer Object for a bird entry.
 * 
 * @see BirdEntryEntity
 */
@Data
@NoArgsConstructor
public class BirdEntryDTO implements BirdEntry {

    private String title;
    private String commonName = null;

    /**
     * Converts this DTO to a new database entry (only for creating).
     * 
     * @return A new entry to be saved to the database.
     */
    public BirdEntryEntity toEntity() {
        final BirdEntryEntity entity = new BirdEntryEntity();
        entity.setTitle(title);
        entity.setCommonName(commonName);
        return entity;
    }
    
}
