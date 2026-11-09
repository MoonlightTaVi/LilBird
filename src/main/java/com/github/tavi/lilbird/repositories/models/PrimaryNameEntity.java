package com.github.tavi.lilbird.repositories.models;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The primary name of a bird is a name in <b>any locale aside from latin</b>
 * (the primary latin name is stored at {@link BirdEntity#getNameLatin()}).
 * Each bird can have only <b>one primary name per locale</b>, 
 * but many primary names total.
 * Some birds may have no primary names (they are not localized).
 */
@Data @NoArgsConstructor 
@Entity(name = "PrimaryName")
@Table(name = "PrimaryNames")
public class PrimaryNameEntity {

    @EmbeddedId 
    private PrimaryNameId id = new PrimaryNameId();

    @Column(
        name = "name_value",
        nullable = false
    )
    private String nameValue;


    public PrimaryNameEntity(BirdEntity reference, Locale locale, String nameValue) {
        id = new PrimaryNameId(reference, locale);
        this.nameValue = nameValue;
    }

    public void setReference(BirdEntity reference) {
        id.reference = reference;
    }

    public void setLocale(Locale locale) {
        id.locale = locale;
    }

    
    /** 
     * The embedded ID class identifies a primary name 
     * by its referenced bird and locale. 
     */
    @NoArgsConstructor @AllArgsConstructor
    @Embeddable
    public static class PrimaryNameId implements Serializable {

        /** The bird this primary name belongs to. */
        @OneToOne
        @JoinColumn(
            name = "reference_id",
            referencedColumnName = "id"
        )
        BirdEntity reference;

        /** The locale of this primary name. */
        @Column(name = "language_tag")
        Locale locale = Locale.forLanguageTag("ru");


        /** The ID of the referenced bird, for convenience. */
        public long getReferenceId() {
            return reference.getId();
        }

        // Methods required for an embedded ID:

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof PrimaryNameId id) {
                return 
                    getReferenceId() == id.getReferenceId()
                    && locale.equals(id.locale);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getReferenceId(), locale);
        }

    }
}
