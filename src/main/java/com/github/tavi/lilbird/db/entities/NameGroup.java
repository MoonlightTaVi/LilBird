package com.github.tavi.lilbird.db.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.tavi.lilbird.models.NameGroupModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The entity for the table of alternative bird names.
 */
@JsonInclude(Include.NON_NULL)
@Entity(name = "NameGroup")
@Table(name = "BirdNames")
@Data
@NoArgsConstructor
public class NameGroup implements NameGroupModel {

    /**
     * This special symbol is used to separate names in a string.
     */
    public static String NAME_DELIMITER = ";";


    @Min(
        value = 1,
        message = "The ID is always > 0"
    )
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * The reference to the original bird entry.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(
        name = "entry_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private BirdEntry entry;

    @Column(
        nullable = false
    )
    private String namesStr;
    @Column
    private String etymology;


    @Override
    public void addName(final String name) {
        namesStr += NAME_DELIMITER + name;
    }

    @Override
    public void setNames(final List<String> names) {
        namesStr = String.join(NAME_DELIMITER, names);
    }

    /**
     * Returns an unmodifiable list of alternative names group.
     */
    @Override
    public List<String> getNames() {
        if (namesStr == null)
            return List.of();
        return List.of(namesStr.split(NAME_DELIMITER));
    }
    
}
