package com.github.tavi.lilbird.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @JsonProperty("taxon_type")
    @NotNull
    private TaxonType taxonType;

    @IsLatinWord
    @JsonProperty("latin_name")
    String nameLatin;

    @Min(1)
    @JsonProperty("species_count")
    int speciesCount;
    
    /** The locale of the primary and alternative names. */
    @JsonProperty("locale")
    @NotNull
    private Locale locale;

    /** (Optional) The primary, most common localized name. */
    @JsonProperty("name_primary")
    String namePrimary;

    /** Alternative (localized) names. */
    @JsonProperty("alternative_names")
    private List<@NotNull String> alternativeNames = new ArrayList<>();


    public TaxonCard(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    public TaxonCard(BirdEntity entity) {
        nameLatin = entity.getNameLatin();
        speciesCount = entity.getSpeciesCount();
        taxonType = entity.getTaxonType();
    }

    public void addAltName(@NotNull NameEntity nameEntity) {
        alternativeNames.add(nameEntity.getNameDisplayed());
    }

    /** @return Whether {@link #getNamePrimary()} is not null and not blank. */
    public boolean hasNamePrimary() {
        return namePrimary != null && !namePrimary.isBlank();
    }

}
