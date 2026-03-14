package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;


/**
 * The public API to the database; it only allows reading the public
 * information that is managed by administrators.
 */
@RestController
@RequestMapping("api/birds")
public class BirdIndexApi {

    @Autowired
    private BirdIndexService service;


    @ApiResponse(
        description = "Returns a JSON array of all birds in the database",
        responseCode = "200",
        useReturnTypeSchema = true
    )
    @GetMapping(
        value = "/all", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<BirdEntry>> listAll() {
        final List<BirdEntry> entryList = service.getEntryList();
        return ResponseEntity.ok(entryList);
    }

}
