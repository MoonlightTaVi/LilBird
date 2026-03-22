package com.github.tavi.lilbird.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.db.entities.BirdEntryEntity;


/**
 * The base repository for birds (as unique entries) in the database.
 */
public interface BirdEntriesRepo extends JpaRepository<BirdEntryEntity, Long> {

    /**
     * @see BirdEntryEntity#getTitleId()
     */
    @Query("SELECT e FROM BirdEntryEntity e WHERE e.titleId = :title")
    public Optional<BirdEntryEntity> findByTitle(@Param("title") String title);

}
