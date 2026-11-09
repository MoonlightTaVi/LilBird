package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.models.TaxonCard;
import com.github.tavi.lilbird.enums.TaxonType;
import com.github.tavi.lilbird.services.BirdCardsService;


/**
 * @see com.github.tavi.lilbird.models.entities.Taxon
 * @see TaxonomyService
 */
@RestController
@RequestMapping("api/v1/taxons")
public class TaxonomyListingController {

    @Autowired
    BirdCardsService service;


    /**
     * Filters the taxons by the taxon type/name and the species length.
     */
    @GetMapping("/filter/{taxonType}")
    public ResponseEntity<List<TaxonCard>> getBySpecies(
            @PathVariable(
                "taxonType"
            ) TaxonType taxonType,
            @RequestParam(
                name = "species"
            ) int species,
            @RequestParam(
                name = "range", 
                required = false, 
                defaultValue = "0"
            ) int range
        ) 
    {
        return ResponseEntity.ok(
            service.filterTaxons(
                taxonType, 
                species, 
                species + range
            )
        );
    }

}
