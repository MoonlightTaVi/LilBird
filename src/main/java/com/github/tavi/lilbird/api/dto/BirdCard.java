package com.github.tavi.lilbird.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.models.BirdModel;
import com.github.tavi.lilbird.models.NameGroupModel;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A card that accumulates all the information about a bird.
 * 
 * @see BirdEntry
 */
@Data
@NoArgsConstructor
public class BirdCard implements BirdModel {

    /**
     * TODO WTF is that
     */
    private List<String> photoUrls = new ArrayList<>();

    private String nameLatin;
    private List<BasicNameGroup> names = new ArrayList<>();
    

    public void addNameGroup(final BasicNameGroup nameGroup) {
        names.add(nameGroup);
    }

    public void addNameGroup(final NameGroupModel nameGroup) {
        final BasicNameGroup basicNameGroup = new BasicNameGroup();
        basicNameGroup.copyFrom(nameGroup);
        addNameGroup(basicNameGroup);
    }
}
