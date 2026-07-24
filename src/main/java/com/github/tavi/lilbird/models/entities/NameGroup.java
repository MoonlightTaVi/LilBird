package com.github.tavi.lilbird.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The entity for the table of alternative bird names.
 */
@Data @NoArgsConstructor
@Entity(name = "NameGroup")
@Table(name = "BirdNames")
public class NameGroup {

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
    private Bird entry;


    /** Ethymology of this name group. */
    @NotBlank
    private String etymology;

    /** 
     * Alternative names, separated by a delimiter. 
     * @see NameUtils.NAME_DELIMITER
    */
    @JsonIgnore @NotBlank
    @Column(name = "names")
    private String namesStr;


    /** Sets the names for this group. Overrides the old value. */
    public void setNames(String... names) {
        namesStr = String.join(NameUtils.NAME_DELIMITER, names);
    }

    /** Returns the array of names for this group. */
    public String[] getNames() {
        if (namesStr == null)
            return new String[0];
        return namesStr.split(NameUtils.NAME_DELIMITER);
    }
    
}
