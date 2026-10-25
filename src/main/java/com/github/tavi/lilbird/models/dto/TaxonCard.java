package com.github.tavi.lilbird.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.annotations.IsLatinWord;
import com.github.tavi.lilbird.models.entities.BirdEntity;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A single unit that identifies a group of birds in the classification,
 * for data transfering via HTTP.
 */
@Data @NoArgsConstructor
public class TaxonCard {

    /** The main latin name of the taxon. */
    @IsLatinWord
    @JsonProperty("latin_name")
    String nameLatin;

    /** The most common name of this taxon. */
    @JsonProperty("name")
    String namePrimary;

    /** The total number of species in this taxon. */
    @Min(1)
    @JsonProperty("species_count")
    int speciesCount;


    /** Constructs the taxon from a database entity. */
    public TaxonCard(BirdEntity entity) {
        nameLatin = entity.getNameLatin();
        speciesCount = entity.getSpeciesCount();
    }

    
    /** Returns whether this taxon instance has any primary localized name. */
    public boolean hasPrimaryName() {
        return namePrimary != null && !namePrimary.isBlank();
    }

}
