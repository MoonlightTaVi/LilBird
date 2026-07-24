package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.entities.Bird;
import com.github.tavi.lilbird.models.entities.NameGroup;
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
    public Bird save(@NotNull Bird bird) {
        bird = entriesRepo.save(bird);
        return bird;
    }

    /** Creates OR updates a database entity for an alternative bird name. */
    public NameGroup save(@NotNull NameGroup name) {
        name = namesRepo.save(name);
        return name;
    }


    /* ==== READ ==== */

    /** Checks if a {@link Bird} with this id exists. */
    public boolean entryExists(String latinName) {
        latinName = NameUtils.normalize(latinName);
        return entriesRepo.existsById(latinName);
    }

    /** Finds the entry for the bird by its unique ID. */
    public Bird getEntryById(String latinName) throws NotFoundException {
        latinName = NameUtils.normalize(latinName);
        final Bird entry = entriesRepo
                .findById(latinName)
                .orElseThrow(
                    () -> new NotFoundException(
                        "The entry by this ID does not exist"
                    )
                );
        return entry;
    }

    /** Finds the entry for the bird by its unique latin name ID. */
    public Bird getEntryByName(String latinName) throws NotFoundException {
        latinName = NameUtils.normalize(latinName);
        final Bird entry = entriesRepo
                .findById(latinName)
                .orElseThrow(
                    () -> new NotFoundException(
                        "The entry by this title ID does not exist"
                    )
                );
        return entry;
    }

    /** Returns the list of all bird entries in the database. */
    public List<Bird> getAllBirds() {
        return entriesRepo.findAll();
    }

    /** Returns the list of synonymous names for the bird entry. */
    public List<NameGroup> getNamesOf(String latinName) {
        latinName = NameUtils.normalize(latinName);
        final List<NameGroup> synonyms = namesRepo.findByReference(latinName);
        return synonyms;
    }

    /** Same as {@link #getNamesOf(long)}, but accepts the entry itself. */
    public List<NameGroup> getNamesOf(@NotNull Bird entry) {
        return getNamesOf(entry.getNameNormal());
    }

    // ==== DROP ====

    // TODO Create methods for dropping

}
