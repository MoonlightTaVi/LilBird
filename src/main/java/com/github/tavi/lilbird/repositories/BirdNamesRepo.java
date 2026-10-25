package com.github.tavi.lilbird.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.models.entities.NameEntity;


/**
 * The repository of synonyms for bird names.
 */
public interface BirdNamesRepo extends JpaRepository<NameEntity,Long> {

    // Hibernate queries use attribute names, not column names

    /** Filters all synonyms that correspond to the given bird and returns them. */
    @Query("SELECT n FROM BirdName n WHERE n.entry.nameNormal = :nameNormal ORDER BY n.id")
    public List<NameEntity> findByReference(@Param("nameNormal") String nameNormal);
    
}
