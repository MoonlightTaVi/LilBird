package com.github.tavi.lilbird.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.HandledServerException;
import com.github.tavi.lilbird.api.exception.NotFoundException;
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

    private final ServiceLogger log = new ServiceLogger();


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
        log.saving(entry);

        try {
            entry = entriesRepo.save(entry);
        } catch (final OptimisticLockingFailureException e) {
            log.failed(entry, e);
            throw new HandledServerException(
                "The entry is currently locked (try again later)"
            );
        } catch (final DataIntegrityViolationException e) {
            log.duplication(entry);
            throw new HandledServerException(
                "The entry already exists"
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
        log.saving(synonym);
        
        try {
            synonym = synonymsRepo.save(synonym);
        } catch (final OptimisticLockingFailureException e) {
            log.failed(synonym, e);
            throw new HandledServerException(
                "The synonym is currently locked (try again later)"
            );
        }

        return synonym;
    }


    // ==== READ ====

    /**
     * Checks if a {@link BirdEntry} with this id exists.
     * It is recommended to check for entity existence
     * before performing expensive operations.
     * 
     * @param id            The ID of the bird entry.
     * @return              {@code true} if the entry exists;
     *                      {@code false} otherwise.
     */
    public boolean entryExists(final long id) {
        return entriesRepo.existsById(id);
    }

    /**
     * Finds the entry for the bird by its unique ID.
     * 
     * @param id            The ID of the bird.
     * @return              The bird entity by this ID if it exists.
     * 
     * @throws NotFoundException If the entity does not exist.
     */
    public BirdEntry getEntry(final long id) throws NotFoundException {
        final BirdEntry entry = entriesRepo
                .findById(id)
                .orElseThrow(
                    () -> new NotFoundException(
                        "The entry by this ID does not exist"
                    )
                );
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
        final List<BirdSynonym> synonyms = synonymsRepo.findByReference(entryId);
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
     * A custom wrapper fpr Logback that is used only by
     * the {@code BirdIndexService}.
     */
    private static class ServiceLogger {

        final Logger log = LoggerFactory.getLogger(
            BirdIndexService.class
        );
        

        /**
         * Logs a debug message about saving a bird entry.
         * 
         * @param entry     The entry that is being saved.
         */
        void saving(final BirdEntry entry) {
            log.debug("Saving an entry: {}", entry);
        }
        
        /**
         * Logs a debug message about saving a bird name synonym.
         * 
         * @param synonym   The synonym that is being saved.
         */
        void saving(final BirdSynonym synonym) {
            log.debug("Saving a synonym: {}", synonym);
        }

        /**
         * Logs an info message which says that the entry was tried
         * to be duplicated ({@code DataIntegrityViolationException}).
         * 
         * @param entry     The entry that already exists.
         */
        void duplication(final BirdEntry entry) {
            log.info("Duplication prevented: {}", entry);
        }

        /**
         * Logs a generic warning about some database operation
         * that was failed.
         * 
         * @param entity    The entity that caused the exception.
         * @param e         The exception.
         */
        void failed(final Object entity, final Exception e) {
            log.warn(
                    "Operation failed for {} due to {}: {}", 
                    entity,
                    e.getClass().getSimpleName(),
                    e.getLocalizedMessage()
                );
        }
    }
}
