package com.github.tavi.lilbird.models.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tavi.lilbird.models.NameGroup;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The entity for the table of alternative bird names.
 */
@Data @NoArgsConstructor
@Entity(name = "NameGroup")
@Table(name = "BirdNames")
public class NameGroupEntity implements NameGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The reference to the original bird entry. */
    @ManyToOne
    @JoinColumn(
        name = "ref_name_normal",               // This column
        referencedColumnName = "name_normal",   // Referenced column
        nullable = false
    )
    @JsonIgnore
    private BirdEntity entry;


    /** Ethymology of this name group. */
    @Column(nullable = false)
    private String etymology;

    /** 
     * Alternative names, separated by a delimiter. 
     * @see NameUtils.NAME_DELIMITER
    */
    @JsonIgnore
    private String namesStr;


    @Override
    public void setNames(final List<String> names) {
        namesStr = String.join(NameUtils.NAME_DELIMITER, names);
    }

    /** @apiNote Returns an unmodifiable list of alternative names group. */
    @Override
    public List<String> getNames() {
        if (namesStr == null)
            return List.of();
        return List.of(namesStr.split(NameUtils.NAME_DELIMITER));
    }
    
}
