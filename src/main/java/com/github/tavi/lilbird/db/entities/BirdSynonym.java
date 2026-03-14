package com.github.tavi.lilbird.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The entity for the table of synonyms of bird names.
 */
@Entity
@Table(name = "Synonyms")
@Data
@NoArgsConstructor
public class BirdSynonym {

    @Min(
        value = 1,
        message = "The ID is always > 0"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private BirdEntry originalEntry;

    /**
     * The synonymous name for the bird.
     */
    @NotNull
    @Column(nullable = false)
    private String name;

    /**
     * Some additional comment on this synonym
     * (for example, used language, dialect, etc.);
     * may be a JSON string.
     */
    private String comment = null;

}
