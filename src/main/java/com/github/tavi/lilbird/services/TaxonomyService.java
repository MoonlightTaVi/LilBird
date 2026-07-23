package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.repositories.TaxonomyRepo;


/** @see Taxon */
@Service
public class TaxonomyService {

    @Autowired
    TaxonomyRepo repository;


    /** Creates or updates a taxon database entry. */
    public Taxon save(Taxon taxon) {
        return repository.save(taxon);
    }

    /** Checks if the taxon with the given name exists. */
    public boolean exists(String latinName) {
        return repository.existsById(latinName);
    }

    /** Returns the taxon with the given name if it exists.
     * 
     * @throws NotFoundException    If no taxon with this name is present.
     */
    public Taxon get(String latinName) {
        return repository.findById(latinName)
            .orElseThrow(() -> new NotFoundException("Taxon is not present: " + latinName));
    }

    /** Returns the list of all taxons with the given taxon name (type). */
    public List<Taxon> getAllOfType(String taxonName) {
        return repository.filterByType(taxonName);
    }

    /** Returns the list of all taxons with the given range of species. */
    public List<Taxon> filterBySpecies(int min, int max) {
        return repository.filterBySpecies(min, max);
    }

    /** Returns the list of all taxons that have exactly this number of species. */
    public List<Taxon> filterBySpecies(int speciesLength) {
        return repository.filterBySpecies(speciesLength);
    }

}
