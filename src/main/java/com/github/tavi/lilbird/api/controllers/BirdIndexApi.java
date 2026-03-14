package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.BirdSynonym;
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
@RequestMapping("api/birds")
@Validated
public class BirdIndexApi {

    @Autowired
    private BirdIndexService service;


    @Operation(
        summary = "Get list of all birds"
    )
    @ApiResponse(
        description = "Successful",
        responseCode = "200",
        useReturnTypeSchema = true
    )
    @GetMapping(
        value = "", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<BirdEntry>> listAll() {
        final List<BirdEntry> entryList = service.getEntryList();
        return ResponseEntity.ok(entryList);
    }

    
    @Operation(
        summary = "Get list of alternative names for a bird"
    )
    @ApiResponses(value = {
        @ApiResponse(
            description = "Successful",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "Empty array (a bird by this ID does not exist)",
            responseCode = "404",
            content = @Content(
                schema = @Schema(example = "[]")
            )
        )
    })
    @GetMapping(
        value = "/{id}/alt-names", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<BirdSynonym>> synonymsOf(
            @Valid
            @Min(
                value = 1,
                message = "The ID is always > 0"
            )
            @Schema(
                description = "The ID of an existing bird entry",
                example = "1"
            )
            @PathVariable("id") 
                final long id
        ) 
    {
        final List<BirdSynonym> entryList = service.getSynonymsOf(id);
        if (entryList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(entryList);
        }

        return ResponseEntity.ok(entryList);
    }

}
