package com.github.tavi.lilbird.repositories.models;

import java.util.Locale;

import com.github.tavi.lilbird.util.NameUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A name of a {@link BirdEntity} other than the bird's own latin name:
 * either an alternative latin name or a localized name.
 */
@Data @NoArgsConstructor
@Entity(name = "Name")
@Table(name = "Names")
public class NameEntity {

    /** A generated {@link GenerationType#SEQUENCE} ID. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** The bird this name belongs to. */
    @ManyToOne
    @JoinColumn(
        name = "ref_id",
        referencedColumnName = "id",
        nullable = false
    )
    private BirdEntity reference;

    /** The locale of the name. Defaults to latin. */
    @Column(name = "locale", nullable = false)
    private Locale locale = Locale.forLanguageTag("la");
    
    /** The name as it is displayed for user. */
    @Column(name = "name_displayed", nullable = false)
    private String nameDisplayed;
    
    /** The name as it is displayed for user. */
    @Setter(AccessLevel.NONE)
    @Column(name = "name_normal", nullable = false)
    private String nameNormal = null;   // Hibernate needs this to be field


    /** The recommended constructor for manual instantiation. */
    public NameEntity(String nameDisplayed, Locale locale, BirdEntity reference) {
        this.nameDisplayed = nameDisplayed;
        this.locale = locale;
        this.reference = reference;
        nameNormal = NameUtils.normalize(nameDisplayed, locale);
    }
    
    /** @return Whether this name's locale correspond to the given language tag. */
    public boolean isLocale(String languageTag) {
        return Locale.forLanguageTag(languageTag).equals(locale);
    }

}
