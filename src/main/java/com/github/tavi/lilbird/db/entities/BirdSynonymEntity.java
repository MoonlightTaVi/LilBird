package com.github.tavi.lilbird.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.tavi.lilbird.models.BirdSynonym;

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
 * The entity for the table of synonyms of bird names.
 */
@JsonInclude(Include.NON_NULL)
@Entity
@Table(
    name = "Synonyms"
)
@Data
@NoArgsConstructor
public class BirdSynonymEntity implements BirdSynonym {

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
    private BirdEntryEntity originalEntry;

    @Column(
        nullable = false
    )
    private String name;

    private String comment = null;

}
