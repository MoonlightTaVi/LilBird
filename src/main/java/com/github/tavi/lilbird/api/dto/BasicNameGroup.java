package com.github.tavi.lilbird.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.models.NameGroupModel;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A group of alternative bird names and their shred etymology.
 */
@Data
@NoArgsConstructor
public class BasicNameGroup implements NameGroupModel {

    List<String> names = new ArrayList<>();
    String etymology;
    

    @Override
    public void addName(final String name) {
        names.add(name);
    }

    
    @Override
    public void copyFrom(final NameGroupModel nameGroup) {
        names = nameGroup.getNames();
        etymology = nameGroup.getEtymology();
    }
}
