package com.github.tavi.lilbird.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.models.BirdModel;
import com.github.tavi.lilbird.models.NameGroupModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @see BirdEntry
 */
@Schema(
    description = "A card that accumulates all the information about a bird"
)
@Data
@NoArgsConstructor
public class BirdCardDto implements BirdModel {

    private List<String> photoUrls = new ArrayList<>();
    private List<NameGroupDto> names = new ArrayList<>();

    /**
     * A unique latin name of the bird.
     */
    private String nameLatin;
    /**
     * Some optional common name.
     */
    private String nameMain;
    

    /**
     * Adds a name group to the contained list.
     * 
     * @param nameGroup     A group of alternative names that share
     *                      the same etymology.
     */
    public void add(final NameGroupDto nameGroup) {
        names.add(nameGroup);
    }

    /**
     * Updates the information of the card from 
     * the given alternative name model.
     * Maps the {@link NameGroupModel} to {@link NameGroupDto} before adding.
     * 
     * @param nameGroup     A name group that will be added to the
     *                      contained list of groups.
     */
    public void accept(final NameGroupModel nameGroup) {
        final NameGroupDto basicNameGroup = new NameGroupDto();
        basicNameGroup.copyFrom(nameGroup);
        add(basicNameGroup);
    }

    /**
     * Updates the information of the card from 
     * the given bird model.
     * 
     * @param bird          A model that contains some of the card information.
     */
    public void accept(final BirdModel bird) {
        nameLatin = bird.getNameLatin();
        nameMain = bird.getNameMain();
    }

    /**
     * Extracts a database entry from this card.
     * 
     * @return              A new database entity that corresponds to the
     *                      information from this card.
     */
    public BirdEntry extractBirdEntry() {
        final BirdEntry entry = new BirdEntry();
        entry.setNameLatin(nameLatin);
        entry.setNameMain(nameMain);
        return entry;
    }
}
