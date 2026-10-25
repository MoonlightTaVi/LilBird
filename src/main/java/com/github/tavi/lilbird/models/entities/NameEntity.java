package com.github.tavi.lilbird.models.entities;

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

    /** The normalized name, for seraching algorithms. Cannot be set directly. */
    @Setter(AccessLevel.NONE)
    @Column(name = "name_normal", nullable = false)
    private String nameNormal;


    /** Creates a new name entity from its displayed name value, given its locale. */
    public NameEntity(String nameValue, String languageTag) {
        nameDisplayed = nameValue;
        setLocale(Locale.forLanguageTag(languageTag));
    }


    /** 
     * Updates the displayed name. 
     * If the locale is set, updates the normalized name. 
     */
    public void setNameDisplayed(String nameValue) {
        nameDisplayed = nameValue;
        if (locale != null)
            nameNormal = NameUtils.normalize(nameDisplayed, locale);
    }

    /** 
     * Updates the locale. 
     * If the displayed name is set, updates the normalized name. 
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        if (nameDisplayed != null)
            nameNormal = NameUtils.normalize(nameDisplayed, locale);
    }
    

    /** Returns whether this name locale corrspond to the given language tag. */
    public boolean isLocale(String languageTag) {
        return Locale.forLanguageTag(languageTag).equals(locale);
    }

}
