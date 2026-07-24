package com.github.tavi.lilbird.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tavi.lilbird.annotations.IsLatinWord;
import com.github.tavi.lilbird.models.entities.NameGroup;
import com.github.tavi.lilbird.util.NameUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/** A bird card contains all the info on the bird. */
@Getter @Setter
@ToString
@NoArgsConstructor
public class BirdCardDto {
    
    /** The main latin name of the bird. */
    @IsLatinWord
    @JsonProperty("name_latin")
    private String nameLatin;
    /** The most common name of the bird. */
    @JsonProperty("name_main")
    private String nameMain = null;

    /** A list of etymologies. Must have the same size as {@link #altNames}. */
    private List<String> etymologies = new ArrayList<>();
    /** A list of alternative names, grouped by etymologies. Defaults to an empty array. */
    @JsonProperty("alt_names")
    private List<String[]> altNames = new ArrayList<>();

    /** Photos of the bird from the Internet. */
    @JsonProperty("photo_urls")
    private List<String> photoUrls = new ArrayList<>();
    

    /** Prepares a name group entity for the date transfer.  */
    public void addAltName(final NameGroup nameGroup) {
        etymologies.add(nameGroup.getEtymology());
        altNames.add(nameGroup.getNames());
    }

    /** 
     * Returns the name group by the given index.
     * 
     * @param index     Must be less than the {@link #nameCount()}.
     * 
     * @return  The {@link NameGroup} by this index.
     * @throws IndexOutOfBoundsException
     */
    public NameGroup getNameGroup(final int index) {
        final NameGroup nameGroup = new NameGroup();
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
