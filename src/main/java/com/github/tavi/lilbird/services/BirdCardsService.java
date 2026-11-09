package com.github.tavi.lilbird.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.models.BirdCard;
import com.github.tavi.lilbird.api.models.TaxonCard;
import com.github.tavi.lilbird.repositories.BirdNamesRepo;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
import com.github.tavi.lilbird.util.NameUtils;
import com.github.tavi.lilbird.util.enums.TaxonType;
import com.github.tavi.lilbird.util.exceptions.NotFoundException;

import jakarta.validation.constraints.NotNull;


/**
 * Creates and reads bird cards.
 * A card's bird row stores only the latin name; every other name is a {@link NameEntity}.
 */
@Service
@Validated
public class BirdCardsService {

    @Autowired
    private BirdsRepo birdsRepo;
    @Autowired
    private BirdNamesRepo namesRepo;


    /** 
     * Decomposes the card into a {@link BirdEntity} 
     * and its {@link NameEntity} instances, then saves them separately.
     */
    @Transactional
    public BirdEntity saveCard(@NotNull BirdCard card) {
        final Locale locale = Locale.forLanguageTag("ru"); // TODO Using RU locale currently
        final BirdEntity bird = getBirdOrCreate(card.getNameLatin());
        card.getAlternativeNames()
            .stream()
            .map(nameStr -> new NameEntity(nameStr, locale, bird))
            .forEach(namesRepo::save);
        return bird;
    }

    /** The same bird as {@link #getBird(String)}, but assembled into a card. */
    public BirdCard getCard(String nameLatin) throws NotFoundException {
        BirdEntity entity = getBird(nameLatin);
        BirdCard card = new BirdCard(entity.getNameLatin());
        card.setAlternativeNames(
            getNamesOf(nameLatin)
            .stream()
            .map(n -> n.getNameDisplayed())
            .toList()
        );
        return card;
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically). 
     * @throws NotFoundException    If no bird with this name is present.
     */
    public BirdEntity getBird(String nameLatin) throws NotFoundException {
        return birdsRepo.getByName(NameUtils.normalize(nameLatin));
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically).
     * @return  The {@link BirdEntity} with this name; 
     *          creates a new entity if not present. 
     */
    public BirdEntity getBirdOrCreate(String nameLatin) {
        return birdsRepo.save(new BirdEntity(nameLatin));
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically).
     * @return {@link Optional} of the bird, if present; 
     *          {@link Optional#empty()} otherwise. 
     */
    public Optional<BirdEntity> getBirdSilently(String nameLatin) {
        return birdsRepo.findByName(NameUtils.normalize(nameLatin));
    }

    /** Returns every bird row. */
    public List<BirdEntity> getAllBirds() {
        return birdsRepo.findAll();
    }


    /** Names stored for the bird with this latin name, in insertion order. */
    public List<NameEntity> getNamesOf(@NotNull String latinName) {
        return namesRepo.findNamesByPrimary(NameUtils.normalize(latinName));
    }

    /** Same as {@link #getNamesOf(String)} for a bird that is already loaded. */
    public List<NameEntity> getNamesOf(@NotNull BirdEntity entry) {
        return namesRepo.findNamesByReference(entry.getId());
    }


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
