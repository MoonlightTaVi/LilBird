package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.entities.BirdEntity;
import com.github.tavi.lilbird.models.entities.NameGroupEntity;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.repositories.NamesRepo;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.validation.constraints.NotNull;


/**
 * This service manages the creation of new bird cards and their updates.
 */
@Service
@Validated
public class BirdCardsService {

    @Autowired
    private BirdsRepo entriesRepo;
    @Autowired
    private NamesRepo namesRepo;


    /* ==== CREATE or UPDATE ==== */

    /** Creates a new database entry OR updates the existent one. */
    public BirdEntity save(@NotNull BirdEntity bird) {
        bird = entriesRepo.save(bird);
        return bird;
    }

    /** Creates OR updates a database entity for an alternative bird name. */
    public NameGroupEntity save(@NotNull NameGroupEntity name) {
        name = namesRepo.save(name);
        return name;
    }


    /* ==== READ ==== */

    /** Checks if a {@link BirdEntity} with this id exists. */
    public boolean entryExists(String latinName) {
        latinName = NameUtils.normalize(latinName);
        return entriesRepo.existsById(latinName);
    }

    /** Finds the entry for the bird by its unique ID. */
    public BirdEntity getEntryById(String latinName) throws NotFoundException {
        latinName = NameUtils.normalize(latinName);
        final BirdEntity entry = entriesRepo
                .findById(latinName)
                .orElseThrow(
                    () -> new NotFoundException(
                        "The entry by this ID does not exist"
                    )
                );
        return entry;
    }

    /** Finds the entry for the bird by its unique latin name ID. */
    public BirdEntity getEntryByName(String latinName) throws NotFoundException {
        latinName = NameUtils.normalize(latinName);
        final BirdEntity entry = entriesRepo
                .findById(latinName)
                .orElseThrow(
                    () -> new NotFoundException(
                        "The entry by this title ID does not exist"
                    )
                );
        return entry;
    }

    /** Returns the list of all bird entries in the database. */
    public List<BirdEntity> getAllBirds() {
        return entriesRepo.findAll();
    }

    /** Returns the list of synonymous names for the bird entry. */
    public List<NameGroupEntity> getNamesOf(String latinName) {
        latinName = NameUtils.normalize(latinName);
        final List<NameGroupEntity> synonyms = namesRepo.findByReference(latinName);
        return synonyms;
    }

    /** Same as {@link #getNamesOf(long)}, but accepts the entry itself. */
    public List<NameGroupEntity> getNamesOf(@NotNull BirdEntity entry) {
        return getNamesOf(entry.getNameNormal());
    }

    // ==== DROP ====

    // TODO Create methods for dropping

}
