package com.github.tavi.lilbird.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.models.entities.Taxon;


/** @see Taxon */
public interface TaxonomyRepo extends JpaRepository<Taxon,String> {

    /** Filters the taxons based on their {@link Taxon#getSpeciesCount()}. */
    @Query("SELECT t FROM Taxon t WHERE t.speciesCount = :count")
    List<Taxon> filterBySpecies(@Param("count") int count);

    /** Same as {@link #filterBySpecies(int)}, but for range. */
    @Query("SELECT t FROM Taxon t WHERE t.speciesCount >= :min AND t.speciesCount <= :max")
    List<Taxon> filterBySpecies(@Param("min") int min, @Param("max") int max);

    /** Returns the list of taxons with the given type name. */
    @Query("SELECT t FROM Taxon t WHERE t.taxonType = :type")
    List<Taxon> filterByType(@Param("type") String taxonName);

}
