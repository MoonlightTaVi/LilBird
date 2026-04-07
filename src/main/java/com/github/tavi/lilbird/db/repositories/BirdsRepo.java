package com.github.tavi.lilbird.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.db.entities.BirdEntry;


/**
 * The base repository for birds (as unique entries) in the database.
 */
public interface BirdsRepo extends JpaRepository<BirdEntry, Long> {

    /**
     * @see BirdEntry#getNameNormal()
     */
    @Query("SELECT b FROM Bird b WHERE b.nameNormal = :nameNormal")
    public Optional<BirdEntry> findByTitle(@Param("nameNormal") String nameNormal);

}
