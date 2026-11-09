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
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
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

    
    /** 
     * Decomposes the card into {@link BirdEntity} 
     * and a set of its {@link NameEntity} instances,
     * then saves them separately.
     */
    @Transactional
    public BirdEntity save(@NotNull TaxonCard taxonCard, @NotNull TaxonType type) {
        final Locale locale = Locale.forLanguageTag(taxonCard.getLangTag());
        BirdEntity taxonEntity = new BirdEntity(taxonCard.getNameLatin());
        taxonEntity.setTaxonType(type);
        taxonEntity.setSpeciesCount(taxonCard.getSpeciesCount());
        taxonEntity = birdsRepo.save(taxonEntity);
        // TODO Currently saving the primary name to alternative ones
        if (taxonCard.hasNamePrimary())
            namesRepo.save(new NameEntity(taxonCard.getNamePrimary(), locale, taxonEntity));
        for (String altName : taxonCard.getAlternativeNames())
            namesRepo.save(new NameEntity(altName, locale, taxonEntity));
        return taxonEntity;
    }

    /** 
     * Returns the list of all bird entries that correspond 
     * to the gieven taxon type and species range.
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
            TaxonCard taxonCard = new TaxonCard(taxonEntity);
            for (NameEntity nameEntity : namesRepo.findNamesByReference(taxonEntity.getId())) {
                // TODO Currently holding the primary name in alternative ones
                if (!taxonCard.hasNamePrimary())
                    taxonCard.setNamePrimary(nameEntity.getNameDisplayed());
                else
                    taxonCard.addAltName(nameEntity);
            }
                
            taxonCards.add(taxonCard);
        }
        return taxonCards;
    }

}
