package com.github.tavi.lilbird.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.BirdSynonym;
import com.github.tavi.lilbird.db.repositories.BirdEntriesRepo;
import com.github.tavi.lilbird.db.repositories.BirdSynonymsRepo;

import jakarta.validation.constraints.NotNull;

/**
 * This service manages the index of the bird database: 
 * listing existent birds, adding new birds, updating existent birds,
 * assigning new synonyms/aliases to existent bird names, etc.
 */
@Service
@Validated
public class BirdIndexService {

    private final Logger log = LoggerFactory.getLogger(
            BirdIndexService.class
        );


    @Autowired
    private BirdEntriesRepo entriesRepo;
    @Autowired
    private BirdSynonymsRepo synonymsRepo;


    // ==== CREATE or UPDATE ====

    /**
     * Creates a new database entity for the bird entry 
     * OR updates the existent one and saves the changes.
     * 
     * @param entry     A new / an existent bird entity to be saved 
     *                  to the database.
     * @return          This bird entity after it was saved.
     * 
     * @see             BirdEntry
     */
    public BirdEntry save(@NotNull BirdEntry entry) {
        logSaving(entry, entry.getId() == null);

        try {
            entry = entriesRepo.save(entry);
        } catch (final OptimisticLockingFailureException e) {
            log.warn(
                "The entry could not be saved: {} (reason: {}).",
                entry.toString(),
                e.getLocalizedMessage()
            );
        }
        return entry;
    }

    /**
     * Creates a new database entity for the synonymous bird name
     * OR updates the existent one and saves the changes.
     * 
     * @param entry     A new / an existent synonym entity to be saved 
     *                  to the database.
     * @return          This synonym entity after it was saved.
     * 
     * @see             BirdSynonym
     */
    public BirdSynonym save(@NotNull BirdSynonym synonym) {
        logSaving(synonym, synonym.getId() == null);
        
        try {
            synonym = synonymsRepo.save(synonym);
        } catch (final OptimisticLockingFailureException e) {
            log.warn(
                "The synonym could not be saved: {} (reason: {}).",
                synonym.toString(),
                e.getLocalizedMessage()
            );
        }
        return synonym;
    }


    // ==== READ ====

    /**
     * Finds the entry for the bird by its unique ID.
     * 
     * @param id            The ID of the bird.
     * @return              The bird entity by this ID if it exists;
     *                      {@code null} otherwise.
     */
    public BirdEntry getEntry(final long id) {
        final BirdEntry entry = entriesRepo
                .findById(id)
                .orElse(null);
        return entry;
    }

    /**
     * Returns the list of all bird entries in the database.
     * 
     * @return              List of bird entries.
     */
    public List<BirdEntry> getEntryList() {
        return entriesRepo.findAll();
    }

    /**
     * Returns the list of synonymous names for the bird entry 
     * by the ID of this entry.
     * 
     * @param entryId       A unique ID of the bird.
     * @return              A list of synonymous names for this bird.
     */
    public List<BirdSynonym> getSynonymsOf(final long entryId) {
        final List<BirdSynonym> synonyms = null;//synonymsRepo.findByReference(entryId);
        return synonyms;
    }

    /**
     * Same as {@link #getSynonymsOf(long)}, but accepts the entry itself.
     * 
     * @param entry         The entry on the bird.
     * @return              A list of synonymous names for this bird.
     */
    public List<BirdSynonym> getSynonymsOf(@NotNull final BirdEntry entry) {
        return getSynonymsOf(entry.getId());
    }

    // ==== DROP ====

    // TODO Create methods for dropping


    // ==== MISC ====

    /**
     * Logs a debug message about creating/updating some entity.
     * 
     * @param entity        Some database entity.
     * @param isNew         Whether this entity is created or updated.
     */
    private void logSaving(final Object entity, final boolean isNew) {
        final String message = isNew ? 
                "Creating a new entity: {}." : 
                "Updating an existent entity: {}.";
        log.debug(message, entity.toString());
    }
}
