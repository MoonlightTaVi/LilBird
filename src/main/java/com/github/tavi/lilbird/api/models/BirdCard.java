package com.github.tavi.lilbird.api.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.annotations.IsLatinWord;

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

    /** The bird's own latin name. It identifies the bird. */
    @NotBlank
    @IsLatinWord
    @JsonProperty("name_latin")
    private String nameLatin;

    /** Alternative localized names. */
    @JsonProperty("alternative_names")
    private List<@NotNull String> alternativeNames = new ArrayList<>();


    /** Creates a card from its latin name. */
    public BirdCard(String nameLatin) {
        this.nameLatin = nameLatin;
    }

}
