package com.github.tavi.lilbird.api.pvt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.models.TaxonCard;
import com.github.tavi.lilbird.services.TaxonService;
import com.github.tavi.lilbird.util.enums.TaxonType;

import jakarta.validation.Valid;


/** 
 * Manages the creation of taxon entries in the database. 
 * 
 * @see TaxonType
 */
@Validated
@RestController
@RequestMapping("api/v1/admin/taxons")
public class TaxonsAdminAPI {

    @Autowired
    TaxonService service;

    /** Create a new taxon database entry. */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void postTaxon(
        @RequestBody @Valid TaxonCard taxonCard
    ) 
    {
        service.saveTaxonCard(taxonCard);
    }

}
