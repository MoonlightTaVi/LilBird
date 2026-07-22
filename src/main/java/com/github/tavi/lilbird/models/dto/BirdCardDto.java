package com.github.tavi.lilbird.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.tavi.lilbird.models.entities.NameGroupEntity;
import com.github.tavi.lilbird.util.NameUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/** A bird card contains all the info on the bird. */
@Schema(
    description = "A card that accumulates all the information about a bird"
)
@Getter @Setter
@ToString
@NoArgsConstructor
public class BirdCardDto {
    
    /** The main latin name of the bird. */
    private String nameLatin;
    /** The most common name of the bird. */
    private String nameMain = null;

    /** A list of etymologies. Must have the same size as {@link #altNames}. */
    private List<String> etymologies = new ArrayList<>();
    /** A list of alternative names, grouped by etymologies. Defaults to an empty array. */
    private List<String[]> altNames = new ArrayList<>();

    /** Photos of the bird from the Internet. */
    private List<String> photoUrls = new ArrayList<>();
    

    /** Prepares a name group entity for the date transfer.  */
    public void addAltName(final NameGroupEntity nameGroup) {
        etymologies.add(nameGroup.getEtymology());
        altNames.add(nameGroup.getNames());
    }

    /** 
     * Returns the name group by the given index.
     * 
     * @param index     Must be less than the {@link #nameCount()}.
     * 
     * @return  The {@link NameGroupEntity} by this index.
     * @throws IndexOutOfBoundsException
     */
    public NameGroupEntity getNameGroup(final int index) {
        final NameGroupEntity nameGroup = new NameGroupEntity();
        nameGroup.setEtymology(etymologies.get(index));
        nameGroup.setNames(altNames.get(index));
        return nameGroup;
    }
    
    /** Total number of alternative name groups for this bird. */
    public int nameCount() {
        return etymologies.size();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof BirdCardDto c) {
            return Objects.equals(nameLatin, c.nameLatin)
                && Objects.equals(nameMain, c.nameMain)
                && etymologies.equals(c.etymologies)
                && NameUtils.equals(altNames, c.altNames)
                && photoUrls.equals(c.photoUrls);
        }
        return false;
    }

}
