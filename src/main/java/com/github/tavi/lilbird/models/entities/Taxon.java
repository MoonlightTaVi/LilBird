package com.github.tavi.lilbird.models.entities;

import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;


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
 * @see #taxonName
 */
@Entity(name = "Taxon")
@Table(name = "Taxons")
@Data @NoArgsConstructor
public class Taxon {

    /** The name that identifies this taxon type (e.g. "family", "genus", etc.) */
    @Column(name = "taxon_name")
    String taxonName;

    /** Same as {@link #latinName}, but normalized. Do not change it manually. */
    @Id
    @Column(name = "normal_name")
    String normalName;

    /** The main latin name of the taxon. */
    @Column(name = "latin_name")
    String latinName;

    /** The most common name of this taxon. */
    @Column(name = "common_name")
    String commonName;

    /** The total number of species in this taxon. */
    @Min(1)
    @Column(name = "species_length")
    int speciesLength;


    /** Sets both the latin and the normalized names of the entry. */
    public void setLatinName(String latinName) {
        this.latinName = latinName;
        normalName = NameUtils.normalize(latinName);
    }

}
