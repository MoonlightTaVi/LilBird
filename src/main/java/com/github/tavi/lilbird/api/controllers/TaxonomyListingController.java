package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.services.TaxonomyService;


/**
 * @see Taxon
 * @see TaxonomyService
 */
@RestController
@RequestMapping("api/v1/taxons")
public class TaxonomyListingController {

    @Autowired
    TaxonomyService service;


    /** Returns the taxon with the given latin name if it exists. */
    @GetMapping("/{latin_name}")
    public ResponseEntity<Taxon> getTaxon(@PathVariable("latin_name") String latinName) {
        return ResponseEntity.ok(service.get(latinName));
    }

    /**
     * Filters the taxons by the taxon type/name and the species length.
     * 
     * @param taxonType     Example: family, order, class, species, etc.
     * @param species       Number of species.
     * @param range         The optional number added to the species length
     *                      to filter within a specific range.
     * @return              The filtered list of taxons.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Taxon>> getBySpecies(
            @RequestParam(name = "taxon")
                String taxonType,
            @RequestParam(name = "species")
                int species,
            @RequestParam(name = "range", required = false, defaultValue = "0")
                int range
        ) 
    {
        List<Taxon> result = service
                .getAllOfType(taxonType)
                .stream().parallel()
                .filter(t -> {
                    int s = t.getSpeciesLength();
                    return (s >= species && s <= species + range);
                })
                .toList();
        return ResponseEntity.ok(result);
    }

}
