package com.github.tavi.lilbird.services;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.models.TaxonCard;
import com.github.tavi.lilbird.repositories.BirdNamesRepo;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.repositories.PrimaryNamesRepo;
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
import com.github.tavi.lilbird.repositories.models.PrimaryNameEntity;
import com.github.tavi.lilbird.util.enums.TaxonType;

import jakarta.transaction.Transactional;


/** Same as {@link BirdService}, but for {@link TaxonCard} management. */
@Service 
@Validated 
public class TaxonService {
    
    @Autowired 
    BirdsRepo birdsRepo;
    @Autowired 
    BirdNamesRepo namesRepo;
    @Autowired
    PrimaryNamesRepo primaryNamesRepo;

    /** 
     * Decomposes the card into {@link BirdEntity} 
     * and a set of its {@link NameEntity} instances,
     * then saves them separately.
     */
    @Transactional 
    public void saveCard(TaxonCard taxonCard) {
        new TaxonDescriptor(taxonCard).saveToDatabase();
    }


    /** @param locale Will be used to fetch the alternative localized names. */
    public List<TaxonCard> filterBySpeciesRange(
            TaxonType type, 
            int minSpecies, 
            int speciesRange,
            Locale locale
        ) 
    {
        return birdsRepo.filterByTaxon(
                type, 
                minSpecies, 
                minSpecies + speciesRange
            )
            .stream()
            .map(bird -> new TaxonDescriptor(bird, locale))
            .map(desc -> desc.loadCard())
            .toList();
    }


    /** A helper class that holds the common data between cards and entities. */
    private class TaxonDescriptor {

        public TaxonCard taxonCard;
        public BirdEntity taxonEntity;
        public Locale locale;

        /** Constructs a descriptor from a card for saving an entity. */
        private TaxonDescriptor(TaxonCard taxonCard) {
            this.taxonCard = taxonCard;
            taxonEntity = new BirdEntity();
            taxonEntity.setTaxonType(taxonCard.getTaxonType());
            taxonEntity.setNameLatin(taxonCard.getNameLatin());
            taxonEntity.setSpeciesCount(taxonCard.getSpeciesCount());
            locale = taxonCard.getLocale();
        }

        /** Constructs a descriptor from an entity for loading a card. */
        private TaxonDescriptor(BirdEntity taxonEntity, Locale locale) {
            taxonCard = new TaxonCard(taxonEntity);
            taxonCard.setLocale(locale);
            this.taxonEntity = taxonEntity;
            this.locale = locale;
        }

        
        /** Decomposes the card and saves it to the database. */
        private void saveToDatabase() {
            taxonEntity = birdsRepo.save(taxonEntity);
            if (taxonCard.hasNamePrimary())
                savePrimaryName();
            saveAlternativeNames();
        }

        /** Loads the taxon data and composes it into a card. */
        private TaxonCard loadCard() {
            loadPrimaryName();
            loadAlternativeNames();
            return taxonCard;
        }


        private PrimaryNameEntity savePrimaryName() {
            PrimaryNameEntity primaryNameEntity = new PrimaryNameEntity(
                taxonEntity, 
                locale, 
                taxonCard.getNamePrimary()
            );
            return primaryNamesRepo.save(primaryNameEntity);
        }

        private void saveAlternativeNames() {
            for (String altName : taxonCard.getAlternativeNames())
                namesRepo.save(new NameEntity(altName, locale, taxonEntity));
        }

        private void loadPrimaryName() {
            primaryNamesRepo.getOpt(taxonEntity.getId(), locale)
                .ifPresent(pn -> taxonCard.setNamePrimary(pn.getNameValue()));
        }

        private void loadAlternativeNames() {
            for (NameEntity nameEntity : namesRepo.findNamesByReference(taxonEntity.getId())) {
                taxonCard.addAltName(nameEntity);
            }
        }

    }

}
