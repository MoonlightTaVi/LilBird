package com.github.tavi.lilbird.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.db.entities.NameGroup;


/**
 * The repository of synonyms for bird names.
 */
public interface NamesRepo extends JpaRepository<NameGroup, Long> {

    /**
     * Filters all synonyms that correspond to the given bird and returns them.
     * 
     * @param entryId       The unique ID of the bird.
     * @return              List of synonymous names for this bird.
     */
    @Query(
        "SELECT n FROM NameGroup n WHERE n.entry.id = :entry_id"
    )
    public List<NameGroup> findByReference(
            @Param("entry_id") Long entryId
        );
    
}
