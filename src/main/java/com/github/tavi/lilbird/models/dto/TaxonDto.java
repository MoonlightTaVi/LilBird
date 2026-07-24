package com.github.tavi.lilbird.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.models.entities.Taxon;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A single unit that identifies a group of birds in the classification.
 * 
 * @see Taxon
 */
@Data @NoArgsConstructor
public class TaxonDto {

    /** The name that identifies this taxon type (e.g. "family", "genus", etc.) */
    @JsonProperty("taxon_name")
    String taxonName;

    /** The main latin name of the taxon. */
    @JsonProperty("latin_name")
    String latinName;

    /** The most common name of this taxon. */
    @JsonProperty("name")
    String commonName;

    /** The total number of species in this taxon. */
    @Min(1)
    @JsonProperty("species_length")
    int speciesLength;


}
