package com.github.tavi.lilbird.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.tavi.lilbird.db.entities.NameGroup;
import com.github.tavi.lilbird.models.NameGroupModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(
    description = "A group of alternative bird names and their shred etymology"
)
@Data
@NoArgsConstructor
public class NameGroupDto implements NameGroupModel {

    List<String> names = new ArrayList<>();
    String etymology;
    

    @Override
    public void addName(final String name) {
        names.add(name);
    }

    /**
     * Maps this data transfer object to a database entity.
     */
    public NameGroup mapToEntity() {
        final NameGroup entry = new NameGroup();
        entry.copyFrom(this);
        return entry;
    }

}
