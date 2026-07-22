package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.repositories.TaxonomyRepo;


@Service
public class TaxonomyService {

    @Autowired
    TaxonomyRepo repository;


    public Taxon save(Taxon taxon) {
        return repository.save(taxon);
    }

    public boolean exists(String latinName) {
        return repository.existsById(latinName);
    }

    public Taxon get(String latinName) {
        return repository.findById(latinName)
            .orElseThrow(() -> new NotFoundException("Taxon is not present: " + latinName));
    }

    public List<Taxon> filterBySpecies(int min, int max) {
        return repository.filterBySpecies(min, max);
    }

    public List<Taxon> filterBySpecies(int speciesLength) {
        return repository.filterBySpecies(speciesLength);
    }

}
