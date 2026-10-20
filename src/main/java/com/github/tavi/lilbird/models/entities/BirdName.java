package com.github.tavi.lilbird.models.entities;

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
 * A name of a {@link Bird} other than the bird's own latin name:
 * either an alternative latin name or a localized name.
 */
@Data @NoArgsConstructor
@Entity(name = "BirdName")
@Table(name = "BirdNames")
public class BirdName {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** The bird this name belongs to. */
    @ManyToOne
    @JoinColumn(
        name = "ref_name_normal",
        referencedColumnName = "name_normal",
        nullable = false
    )
    private Bird entry;

    /** The name as it is displayed. */
    @Column(name = "name_value", nullable = false)
    private String nameValue;

    /** Alternative latin name when {@code true}, localized name otherwise. */
    @Column(name = "is_latin", nullable = false)
    private boolean isLatin = false;


    /** Creates a new name entity from its displayed value. */
    public BirdName(String nameValue) {
        this.nameValue = nameValue;
    }

}
