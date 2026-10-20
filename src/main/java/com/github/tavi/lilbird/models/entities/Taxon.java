package com.github.tavi.lilbird.models.entities;

import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A single unit that identifies a group of birds in the classification.
 * The following groups can be types of taxons:
 * <p> - Class
 * <p> - Order
 * <p> - Family
 * <p> - Genus
 * <p> - Species
 * <p> and some others.
 * 
 * @see #taxonType
 */
@Entity(name = "Taxon")
@Table(name = "Taxons")
@Data @NoArgsConstructor
public class Taxon {

    // TODO Use enums
    /** The name that identifies this taxon type (e.g. "family", "genus", etc.) */
    @Column(name = "taxon_type")
    String taxonType;

    /** 
     * Same as {@link #nameLatin}, but normalized. 
     * Derived in {@link #setNameLatin(String)}. 
     */
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "name_normal")
    String nameNormal;

    /** The main latin name of the taxon. */
    @Column(name = "name_latin")
    String nameLatin;

    /** The most common localized name of this taxon. May be {@code null}. */
    @Column(name = "name_primary")
    String namePrimary;

    /** The total number of species in this taxon. */
    @Min(1)
    @Column(name = "species_count")
    int speciesCount;


    /** Sets both the latin and the normalized names of the entry. */
    public void setNameLatin(String latinName) {
        this.nameLatin = latinName;
        nameNormal = NameUtils.normalize(latinName);
    }

}
