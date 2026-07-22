package com.github.tavi.lilbird.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.models.entities.Taxon;


/** @see Taxon */
public interface TaxonomyRepo extends JpaRepository<Taxon,String> {

    /** Filters the taxons based on their {@link Taxon#getSpeciesLength()}. */
    @Query("SELECT t FROM Taxon t WHERE t.speciesLength = :length")
    List<Taxon> filterBySpecies(@Param("length") int length);

    /** Same as {@link #filterBySpecies(int)}, but for range.. */
    @Query("SELECT t FROM Taxon t WHERE t.speciesLength >= :min AND t.speciesLength <= :max")
    List<Taxon> filterBySpecies(@Param("min") int min, @Param("max") int max);

}
