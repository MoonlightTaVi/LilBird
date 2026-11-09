package com.github.tavi.lilbird.services;

import java.util.ArrayList;
import java.util.List;

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


    /** Creates a new bird row or updates the existing one. */
    public BirdEntity save(@NotNull BirdEntity bird) {
        return birdsRepo.save(bird);
    }

    /** Creates or updates one name row, given its reference. */
    public NameEntity save(@NotNull NameEntity name, @NotNull BirdEntity reference) {
        name.setReference(reference);
        return namesRepo.save(name);
    }

    /**
     * Stores a new bird and the names from the card.
     * The bird row receives only {@link BirdCard#getNameLatin()}.
     */
    @Transactional
    public BirdEntity save(@NotNull BirdCard card) {
        BirdEntity bird = new BirdEntity(card.getNameLatin());
        bird = birdsRepo.save(bird);

        for (String nameValue : card.getAlternativeNames()) {
            // TODO Using RU locale currently
            save(new NameEntity(nameValue, "ru"), bird);
        }

        return bird;
    }

    /**
     * Updates or creates a new bird entity that will hold the information about
     * a taxon of some type.
     */
    @Transactional
    public BirdEntity save(@NotNull TaxonCard taxon, @NotNull TaxonType type) {
        BirdEntity e = new BirdEntity(taxon.getNameLatin());
        e.setTaxonType(type);
        e.setSpeciesCount(taxon.getSpeciesCount());
        e = birdsRepo.save(e);

        if (taxon.hasPrimaryName()) {
            // TODO Using RU locale currently
            save(new NameEntity(taxon.getNamePrimary(), "ru"), e);
        }

        return e;
    }


    /** Checks if a bird with this latin name exists. */
    public boolean checkBirdExists(String nameLatin) {
        return !birdsRepo
                .findByName(NameUtils.normalize(nameLatin))
                .isEmpty();
    }

    /**
     * Finds the bird by its latin name.
     * The argument is normalized before the lookup.
     *
     * @throws NotFoundException    If no bird with this name is present.
     */
    public BirdEntity getBirdByName(String nameLatin) throws NotFoundException {
        return birdsRepo
                .findByName(NameUtils.normalize(nameLatin))
                .orElseThrow(
                    () -> new NotFoundException(
                        "The bird entry does not exist: " + nameLatin
                    )
                );
    }

    /** The same bird as {@link #getBirdByName(String)}, assembled into a card. */
    public BirdCard getCardByName(String nameLatin) {
        BirdEntity entity = getBirdByName(nameLatin);
        BirdCard card = new BirdCard(entity.getNameLatin());

        List<String> alternativeNames = new ArrayList<>();
        for (NameEntity nameEnt : getNamesOf(nameLatin))
            alternativeNames.add(nameEnt.getNameDisplayed());
        card.setAlternativeNames(alternativeNames);

        return card;
    }


    /** Names stored for the bird with this latin name, in insertion order. */
    public List<NameEntity> getNamesOf(@NotNull String latinName) {
        return namesRepo.findNamesByPrimary(NameUtils.normalize(latinName));
    }

    /** Same as {@link #getNamesOf(String)} for a bird that is already loaded. */
    public List<NameEntity> getNamesOf(@NotNull BirdEntity entry) {
        return namesRepo.findNamesByReference(entry.getId());
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


    /** Returns every bird row. */
    public List<BirdEntity> getAllBirds() {
        return birdsRepo.findAll();
    }

}
