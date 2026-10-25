package com.github.tavi.lilbird.models.entities;

import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
