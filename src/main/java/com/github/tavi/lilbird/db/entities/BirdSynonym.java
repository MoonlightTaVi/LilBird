package com.github.tavi.lilbird.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonInclude(Include.NON_NULL)
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
    @Schema(
        description = "The alternative name (synonym) of this bird",
        example = "Owl"
    )
    private String name;

    /**
     * Some additional comment on this synonym
     * (for example, used language, dialect, etc.);
     * may be a JSON string.
     */
    @Schema(
        description = "(Optional) Additional comment on this synonym",
        example = "They are called 'owls' because the are 'owling' in the night :)"
    )
    private String comment = null;

}
