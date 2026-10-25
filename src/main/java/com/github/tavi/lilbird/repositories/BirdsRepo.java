package com.github.tavi.lilbird.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.models.entities.BirdEntity;


/** The base repository for birds (as unique entries) in the database. */
public interface BirdsRepo extends JpaRepository<BirdEntity,Long> {

    /** Finds the bird by its normalized primary latin name. */
    @Query("SELECT b FROM Bird b WHERE b.nameNormal = :name_normal")
    public Optional<BirdEntity> findByName(@Param("name_normal") String nameNormal);

}
