package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.services.TaxonomyService;

@RestController
@RequestMapping("api/v1/taxons")
public class TaxonomyListingController {

    @Autowired
    TaxonomyService service;

    @GetMapping("/{latin_name}")
    public ResponseEntity<Taxon> getTaxon(@PathVariable("latin_name") String latinName) {
        return ResponseEntity.ok(service.get(latinName));
    }

    @GetMapping("/by-species/{length}")
    public ResponseEntity<List<Taxon>> getBySpecies(@PathVariable("length") int speciesLength) {
        return ResponseEntity.ok(service.filterBySpecies(speciesLength));
    }

}
