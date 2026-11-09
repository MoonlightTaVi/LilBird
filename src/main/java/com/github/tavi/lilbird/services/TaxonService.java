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
    public BirdEntity save(@NotNull TaxonCard taxon, @NotNull TaxonType type) {
        final Locale locale = Locale.forLanguageTag("ru"); // TODO Using RU locale currently
        BirdEntity e = new BirdEntity(taxon.getNameLatin());
        e.setTaxonType(type);
        e.setSpeciesCount(taxon.getSpeciesCount());
        e = birdsRepo.save(e);
        if (taxon.hasPrimaryName())
            namesRepo.save(new NameEntity(taxon.getNamePrimary(), locale, e));
        return e;
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
        List<BirdEntity> entities = birdsRepo.filterByTaxon(
            type, 
            minSpecies, 
            minSpecies + speciesRange
        );
        List<TaxonCard> result = new ArrayList<>();
        for (BirdEntity e : entities) {
            TaxonCard t = new TaxonCard(e);
            // TODO Add alt names
            result.add(t);
        }
        return result;
    }

}
