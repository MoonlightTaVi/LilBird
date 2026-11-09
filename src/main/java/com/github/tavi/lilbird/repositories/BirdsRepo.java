package com.github.tavi.lilbird.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.util.enums.TaxonType;
import com.github.tavi.lilbird.util.exceptions.NotFoundException;


/** The base repository for birds (as unique entries) in the database. */
public interface BirdsRepo extends JpaRepository<BirdEntity,Long> {

    /** @param nameNormal   Must be normalized for proper look-up. */
    @Query("SELECT b FROM Bird b WHERE b.nameNormal = :name_normal")
    Optional<BirdEntity> findByName(@Param("name_normal") String nameNormal);

    /** Same as {@link #findByName}, but throws {@link NotFoundException}. */
    default BirdEntity getByName(String nameNormal) throws NotFoundException {
        return findByName(nameNormal)
            .orElseThrow(() -> birdNotFound(nameNormal));
    }

    /** 
     * @return  The list of {@link BirdEntity} that correspond 
     *          to the given taxon type and species range.
     */
    @Query(
        """
        SELECT b 
        FROM Bird b 
        WHERE b.taxonType = :taxon_type
        AND b.speciesCount >= :min_species
        AND b.speciesCount <= :max_species
        """
    )
    public List<BirdEntity> filterByTaxon(
        @Param("taxon_type") TaxonType taxonType,
        @Param("min_species") int minSpecies,
        @Param("max_species") int maxSpecies
    );


    private NotFoundException birdNotFound(String nameNormal) {
        return new NotFoundException("Could not find the entry for: " + nameNormal);
    }

}
