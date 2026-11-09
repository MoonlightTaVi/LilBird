package com.github.tavi.lilbird.repositories;

import java.util.Locale;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.tavi.lilbird.repositories.models.PrimaryNameEntity;


/** 
 * Each bird may have either <b>none</b> or exactly <b>one</b> primary name
 * in any locale. <p>
 * Each locale is independent, therefore a bird can have many primary names.
 */
public interface PrimaryNamesRepo extends JpaRepository<PrimaryNameEntity, PrimaryNameEntity.PrimaryNameId> {

    @Query(
        """
        SELECT pn 
        FROM PrimaryName pn 
        WHERE pn.id.reference.id = :reference_id
        AND pn.id.locale = :locale
        """
    )
    Optional<PrimaryNameEntity> getOpt(
        @Param("reference_id") long birdReferenceId, 
        @Param("locale") Locale locale
    );

}
