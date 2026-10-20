package com.github.tavi.lilbird.models.entities;

import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A bird. The only value stored on the bird itself is its latin name.
 * The normalized form of that name is the primary key.
 * Localized names and alternative latin names belong to {@link BirdName}.
 */
@Data @NoArgsConstructor
@Entity(name = "Bird")
@Table(name = "Birds")
public class Bird {

    /** Normalized latin name. Derived from {@link #nameLatin}; this is the id. */
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "name_normal")
    private String nameNormal;

    /** Latin name, as it is displayed. */
    @Column(name = "name_latin", nullable = false)
    private String nameLatin;

    /** The most common localized name of this bird. Can be changed. May be {@code null}. */
    @Column(name = "name_primary")
    private String namePrimary;


    /** Creates a bird entity directly from its primary latin name. */
    public Bird(String nameLatin) {
        setNameLatin(nameLatin);
    }

    /** Sets the displayed latin name and refreshes {@link #nameNormal}. */
    public void setNameLatin(final String nameLatin) {
        this.nameLatin = nameLatin;
        nameNormal = NameUtils.normalize(nameLatin);
    }

}
