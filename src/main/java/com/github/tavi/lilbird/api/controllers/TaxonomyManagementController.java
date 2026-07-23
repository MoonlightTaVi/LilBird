package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.entities.Taxon;
import com.github.tavi.lilbird.services.TaxonomyService;

import jakarta.validation.Valid;


/**
 * @see Taxon
 * @see TaxonomyService
 */
@Validated
@RestController
@RequestMapping("api/v1/admin")
public class TaxonomyManagementController {

    @Autowired
    TaxonomyService service;

    @PostMapping("/taxons")
    @ResponseStatus(HttpStatus.CREATED)
    public void postTaxon(@RequestBody @Valid Taxon taxon) {
        service.save(taxon);
    }

}
