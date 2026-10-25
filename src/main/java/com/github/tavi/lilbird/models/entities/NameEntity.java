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
 * A name of a {@link Bird} other than the bird's own latin name:
 * either an alternative latin name or a localized name.
 */
@Data @NoArgsConstructor
@Entity(name = "BirdName")
@Table(name = "BirdNames")
public class NameEntity {

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
    private Bird entry; // TODO Reference Long ID

    /** The locale of the name. Defaults to latin. */
    @Column(name = "locale", nullable = false)
    private Locale locale = Locale.forLanguageTag("la");

    /** The name as it is displayed for user. */
    @Column(name = "displayed_name", nullable = false)
    private String displayedName;

    /** The normalized name, for seraching algorithms. Cannot be set directly. */
    @Setter(AccessLevel.NONE)
    @Column(name = "normal_name", nullable = false)
    private String normalName;


    /** Creates a new name entity from its displayed name value, given its locale. */
    public NameEntity(String nameValue, String languageTag) {
        displayedName = nameValue;
        setLocale(Locale.forLanguageTag(languageTag));
    }


    /** 
     * Updates the displayed name. 
     * If the locale is set, updates the normalized name. 
     */
    public void setDisplayedName(String nameValue) {
        displayedName = nameValue;
        if (locale != null)
            normalName = NameUtils.normalize(displayedName, locale);
    }

    /** 
     * Updates the locale. 
     * If the displayed name is set, updates the normalized name. 
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        if (displayedName != null)
            normalName = NameUtils.normalize(displayedName, locale);
    }
    

    /** Returns whether this name locale corrspond to the given language tag. */
    public boolean isLocale(String languageTag) {
        return Locale.forLanguageTag(languageTag).equals(locale);
    }

}
