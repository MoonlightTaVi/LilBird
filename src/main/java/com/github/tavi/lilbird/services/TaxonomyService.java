package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.repositories.TaxonomyRepo;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


/** @see Taxon */
@Service
@Validated
public class TaxonomyService {

    @Autowired
    TaxonomyRepo repository;


    /** Creates or updates a taxon database entry. */
    public Taxon save(@NotNull Taxon taxon) {
        return repository.save(taxon);
    }

    /** Checks if the taxon with the given name exists. */
    public boolean exists(String latinName) {
        latinName = NameUtils.normalize(latinName);
        return repository.existsById(latinName);
    }

    /** Returns the taxon with the given name if it exists.
     * 
     * @throws NotFoundException    If no taxon with this name is present.
     */
    public Taxon get(final String latinName) {
        return repository.findById(NameUtils.normalize(latinName))
            .orElseThrow(() -> new NotFoundException("Taxon is not present: " + latinName));
    }

    /** Returns the list of all taxons with the given taxon name (type). */
    public List<Taxon> getAllOfType(String taxonName) {
        return repository.filterByType(taxonName);
    }

    /** Returns the list of all taxons with the given range of species. */
    public List<Taxon> filterBySpecies(@Min(1) int min, @Min(1) int max) {
        return repository.filterBySpecies(min, max);
    }

    /** Returns the list of all taxons that have exactly this number of species. */
    public List<Taxon> filterBySpecies(@Min(1) int speciesLength) {
        return repository.filterBySpecies(speciesLength);
    }

}
