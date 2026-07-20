package com.github.tavi.lilbird.models.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.models.entities.NameGroupEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


/** A bird card contains all the info on the bird. */
@Schema(
    description = "A card that accumulates all the information about a bird"
)
@Data @NoArgsConstructor
public class BirdCardDto {
    
    private String nameLatin;

    private String nameMain;

    /** A list of etymologies. Must have the same size as {@link #altNames}. */
    private List<String> etymologies = new ArrayList<>();
    /** A list of alternative names, grouped by etymologies. Defaults to an empty array. */
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
