package com.github.tavi.lilbird.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.db.entities.BirdSynonymEntity;


/**
 * The repository of synonyms for bird names.
 */
public interface BirdSynonymsRepo extends JpaRepository<BirdSynonymEntity, Long> {

    /**
     * Filters all synonyms that correspond to the given bird and returns them.
     * 
     * @param entryId       The unique ID of the bird.
     * @return              List of synonymous names for this bird.
     */
    @Query(
        "SELECT s FROM BirdSynonymEntity s WHERE s.originalEntry.id = :entry_id"
    )
    public List<BirdSynonymEntity> findByReference(
            @Param("entry_id") Long entryId
        );
    
}
