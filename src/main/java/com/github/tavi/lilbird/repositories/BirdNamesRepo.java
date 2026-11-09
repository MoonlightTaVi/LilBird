package com.github.tavi.lilbird.repositories;

import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;


/**
 * The repository of synonyms for bird names.
 */
public interface BirdNamesRepo extends JpaRepository<NameEntity,Long> {

    // Hibernate queries use attribute names, not column names
    
    /** Finds the bird by its normal name. */
    @Query("SELECT n.reference FROM Name n WHERE n.nameNormal = :name_normal")
    public BirdEntity findBirdByName(@Param("name_normal") String nameNormal);

    /** Filters all synonyms that correspond to the given bird ID and returns them. */
    @Query(
        """
        SELECT n 
        FROM Name n 
        WHERE n.reference.id = :ref_id
        AND n.locale = :locale
        ORDER BY n.id
        """
    )
    public List<NameEntity> findNamesByReference(
        @Param("ref_id") long referenceId,
        @Param("locale") Locale locale
    );

    /** Finds all names of a bird with the given primary latin name (normalized). */
    @Query(
        """
        SELECT n 
        FROM Name n 
        WHERE n.reference.id = (
            SELECT b.id
            FROM Bird b
            WHERE b.nameNormal = :name_normal
        )
        AND n.locale = :locale
        ORDER BY n.id
        """
    )
    public List<NameEntity> findNamesByPrimary(
        @Param("name_normal") String nameNormal,
        @Param("locale") Locale locale
    );

}
