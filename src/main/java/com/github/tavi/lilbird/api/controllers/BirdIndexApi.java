package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.db.entities.BirdEntryEntity;
import com.github.tavi.lilbird.db.entities.BirdSynonymEntity;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;


/**
 * The public API to the database; it only allows reading the public
 * information that is managed by administrators.
 */
@RestController
@RequestMapping(
    value = "api/birds", 
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class BirdIndexApi {

    @Autowired
    private BirdIndexService service;


    @Operation(
        summary = "Get list of all birds"
    )
    @ApiResponse(
        responseCode = "200"
    )
    @GetMapping("")
    public ResponseEntity<List<BirdEntryEntity>> listAll() {
        final List<BirdEntryEntity> entryList = service.getEntryList();
        return ResponseEntity.ok(entryList);
    }

    
    @Operation(
        summary = "Get a bird by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200"
        ),
        @ApiResponse(
            description = "This entry does not exist",
            responseCode = "404",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BirdEntryEntity> getById(
            @PathVariable("id") 
                final long id
        ) 
    {
        return ResponseEntity.ok(service.getEntry(id));
    }

    
    @Operation(
        summary = "Get list of alternative names for a bird"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200"
        ),
        @ApiResponse(
            description = "This entry does not exist",
            responseCode = "404",
            content = @Content
        )
    })
    @GetMapping("/{id}/alt-names")
    public ResponseEntity<List<BirdSynonymEntity>> synonymsOf(
            @Valid
            @Min(1)
            @Schema(
                description = "The ID of an existing bird entry",
                example = "1"
            )
            @PathVariable("id") 
                final long id
        ) 
    {
        if (!service.entryExists(id)) {
            throw new NotFoundException(
                    "An entry by this ID does not exist"
                );
        }

        final List<BirdSynonymEntity> entryList = service.getSynonymsOf(id);
        return ResponseEntity.ok(entryList);
    }

}
