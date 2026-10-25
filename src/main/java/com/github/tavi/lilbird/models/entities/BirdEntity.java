package com.github.tavi.lilbird.models.entities;

import com.github.tavi.lilbird.enums.TaxonType;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * The core entity for bird species. 
 * Contains its current primary latin name for convenience.
 * The primary name may be updated at any moment.
 */
@Data @NoArgsConstructor
@Entity(name = "Bird")
@Table(name = "Birds")
public class BirdEntity {

    /** A generated {@link GenerationType#SEQUENCE} ID. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** The normalized primary latin name, for look-up. */
    @Setter(AccessLevel.NONE)
    @Column(name = "name_normal", unique = true)
    private String nameNormal;
    
    /** 
     * The primary latin name, for visual clearance. 
     * Can be changed (e.g. for different biological systematics), 
     * but must be unique. 
     */
    @Column(name = "name_latin")
    private String nameLatin;


    /** Type of this entry's taxon. Default to {@link TaxonType#SPECIES}. */
    @Column(name = "taxon_type", nullable = false)
    private TaxonType taxonType = TaxonType.SPECIES;

    /** Number of species in this entry's taxon. Defaults to 1. */
    @Min(1)
    @Column(name = "species_count", nullable = false)
    private int speciesCount = 1;


    /** Creates a new bird entity from its primary latin name. */
    public BirdEntity(String nameLatin) {
        setNameLatin(nameLatin);
    }


    /** Updates both the displayed and the normalized names. */
    public void setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
        nameNormal = NameUtils.normalize(nameLatin);
    }

}
