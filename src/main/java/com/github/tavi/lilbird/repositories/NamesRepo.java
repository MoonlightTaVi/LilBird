package com.github.tavi.lilbird.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.models.entities.NameGroupEntity;


/**
 * The repository of synonyms for bird names.
 */
public interface NamesRepo extends JpaRepository<NameGroupEntity, Long> {

    /** Filters all synonyms that correspond to the given bird and returns them. */
    @Query("SELECT n FROM NameGroup n WHERE n.entry.id = :entry_id")
    public List<NameGroupEntity> findByReference(@Param("entry_id") Long entryId);
    
}
