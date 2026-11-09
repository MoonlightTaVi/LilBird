package com.github.tavi.lilbird.api.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
import com.github.tavi.lilbird.util.annotations.IsLatinWord;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A filled bird card. Currently not in use.
 */
@Data
@NoArgsConstructor
public class BirdCard {
    
    /** The language tag associated with the locale; defaults to {@code ru}. */
    @JsonProperty("lang_tag")
    private String langTag = "ru";

    /** The bird's own latin name. */
    @NotBlank
    @IsLatinWord
    @JsonProperty("name_latin")
    private String nameLatin;
    
    /** (Optional) The primary localized name. */
    @JsonProperty("name_primary")
    private String namePrimary;

    /** Alternative (localized) names. */
    @JsonProperty("alternative_names")
    private List<@NotNull String> alternativeNames = new ArrayList<>();


    public BirdCard(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    public BirdCard(BirdEntity birdEntity) {
        nameLatin = birdEntity.getNameLatin();
    }

    public void addAltName(@NotNull NameEntity nameEntity) {
        alternativeNames.add(nameEntity.getNameDisplayed());
    }

}
