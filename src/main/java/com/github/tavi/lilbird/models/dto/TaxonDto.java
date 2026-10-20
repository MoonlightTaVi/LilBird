package com.github.tavi.lilbird.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.annotations.IsLatinWord;
import com.github.tavi.lilbird.models.entities.Taxon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A single unit that identifies a group of birds in the classification.
 * 
 * @see Taxon
 */
@Data @NoArgsConstructor
public class TaxonDto {

    /** The identifier of this taxon type (e.g. "family", "genus", etc.) */
    @NotBlank
    @JsonProperty("taxon_type")
    String taxonType;

    /** The main latin name of the taxon. */
    @NotBlank
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


}
