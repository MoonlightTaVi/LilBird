package com.github.tavi.lilbird.api.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
import com.github.tavi.lilbird.util.annotations.IsLatinWord;
import com.github.tavi.lilbird.util.enums.TaxonType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A single unit that identifies a group of birds in the classification,
 * for data transfering via HTTP.
 */
@Data @NoArgsConstructor
public class TaxonCard {

    @JsonProperty 
    private TaxonType taxonType;
    
    /** The language tag associated with the locale; defaults to {@code ru}. */
    @Deprecated(forRemoval = true)
    @JsonProperty("lang_tag")
    private String langTag = "ru";

    /** The main latin name of the taxon. */
    @IsLatinWord
    @JsonProperty("latin_name")
    String nameLatin;

    /** (Optional) The primary, most common localized name. */
    @JsonProperty("name_primary")
    String namePrimary;

    /** Alternative (localized) names. */
    @JsonProperty("alternative_names")
    private List<@NotNull String> alternativeNames = new ArrayList<>();

    /** The total number of species in this taxon. */
    @Min(1)
    @JsonProperty("species_count")
    int speciesCount;


    public TaxonCard(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    public TaxonCard(BirdEntity entity) {
        nameLatin = entity.getNameLatin();
        speciesCount = entity.getSpeciesCount();
    }

    public void addAltName(@NotNull NameEntity nameEntity) {
        alternativeNames.add(nameEntity.getNameDisplayed());
    }

    /** @return Whether {@link #getNamePrimary()} is not null and not blank. */
    public boolean hasNamePrimary() {
        return namePrimary != null && !namePrimary.isBlank();
    }

}
