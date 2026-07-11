package com.github.tavi.lilbird.models.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.models.entities.NameGroupEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;


/** A bird card contains all the info on the bird. */
@Schema(
    description = "A card that accumulates all the information about a bird"
)
@Data @NoArgsConstructor
public class BirdCardDto {
    
    @NotBlank(
        message = "The primary name cannot be blank"
    )
    @Pattern(
        message = "The title must contain only latin symbols, digits and hyphens",
        regexp = "[0-9a-zA-Z\\-\s]+"
    )
    @Schema(
        description = "The primary (unique) name, in latin symbols",
        example = "Bubo scandiacus"
    )
    private String nameLatin;

    @Schema(
        description = "An optional primary common name",
        example = "Snowy owl"
    )
    private String nameMain;


    @Schema(
        description = "A list of ethymologies for the names of this bird",
        type = "array",
        example = "[\"First ethymology\", \"Another ethymology\"]"
    )
    private List<String> etymologies = new ArrayList<>();

    @Schema(
        description = "A nested list of alternative names",
        type = "array",
        example = "[[\"One name for the first ethymology\"], [\"A couple of names\", \"for the second ethymology\"]]"
    )
    private List<List<String>> altNames = new ArrayList<>();

    /** Photos of the bird from the Internet. */
    private List<String> photoUrls = new ArrayList<>();
    

    /** Prepares a name group entity for the date transfer.  */
    public void addAltName(final NameGroupEntity nameGroup) {
        etymologies.add(nameGroup.getEtymology());
        altNames.add(nameGroup.getNames());
    }

    public NameGroupEntity getNameGroup(final int index) {
        final NameGroupEntity nameGroup = new NameGroupEntity();
        nameGroup.setEtymology(etymologies.get(index));
        nameGroup.setNames(altNames.get(index));
        return nameGroup;
    }
    
    public int nameCount() {
        return etymologies.size();
    }
}
