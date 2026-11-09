package com.github.tavi.lilbird.services;

import java.util.ArrayList;
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
import jakarta.validation.constraints.NotNull;


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
    public void saveTaxonOfType(@NotNull TaxonCard taxonCard, @NotNull TaxonType taxonType) {
        BirdEntity referencedEntity = saveCardAsEntity(taxonCard, taxonType);
        if (taxonCard.hasNamePrimary())
            savePrimaryName(referencedEntity, taxonCard);
        saveAlternativeNames(referencedEntity, taxonCard);
    }

    private BirdEntity saveCardAsEntity(TaxonCard taxonCard, TaxonType taxonType) {
        BirdEntity taxonEntity = new BirdEntity(taxonCard.getNameLatin());
        taxonEntity.setSpeciesCount(taxonCard.getSpeciesCount());
        taxonEntity.setTaxonType(taxonType);
        taxonEntity = birdsRepo.save(taxonEntity);
        return taxonEntity;
    }

    private void savePrimaryName(BirdEntity reference, TaxonCard taxonCard) {
        // TODO Card must rely on locale, not on language tag
        Locale locale = Locale.forLanguageTag(taxonCard.getLangTag());
        PrimaryNameEntity primaryNameEntity = new PrimaryNameEntity();
        primaryNameEntity.setReference(reference);
        primaryNameEntity.setLocale(locale);
        primaryNameEntity.setNameValue(taxonCard.getNamePrimary());
        primaryNamesRepo.save(primaryNameEntity);
    }

    private void saveAlternativeNames(BirdEntity reference, TaxonCard taxonCard) {
        Locale locale = Locale.forLanguageTag(taxonCard.getLangTag());
        for (String altName : taxonCard.getAlternativeNames())
            namesRepo.save(new NameEntity(altName, locale, reference));
    }


    /** 
     * Returns the list of all bird entries that correspond 
     * to the given taxon type and species range.
     */
    public List<TaxonCard> filterTaxons(
            TaxonType type, 
            int minSpecies, 
            int speciesRange
        ) 
    {
        List<BirdEntity> taxonEntities = birdsRepo.filterByTaxon(
            type, 
            minSpecies, 
            minSpecies + speciesRange
        );
        List<TaxonCard> taxonCards = new ArrayList<>();
        for (BirdEntity taxonEntity : taxonEntities) {
            // TODO Currently using RU locale
            TaxonCard taxonCard = getTaxonCardFromEntity(
                taxonEntity, 
                Locale.forLanguageTag("ru")
            );
            taxonCards.add(taxonCard);
        }
        return taxonCards;
    }

    private TaxonCard getTaxonCardFromEntity(BirdEntity taxonEntity, Locale locale) {
        TaxonCard taxonCard = new TaxonCard(taxonEntity);
        primaryNamesRepo
            .getOpt(taxonEntity.getId(), locale)
            .ifPresent(primaryName -> {
                taxonCard.setNamePrimary(primaryName.getNameValue());
            });
        for (NameEntity nameEntity : namesRepo.findNamesByReference(taxonEntity.getId())) {
            taxonCard.addAltName(nameEntity);
        }
        return taxonCard;
    }

}
