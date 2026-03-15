package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.dto.BirdDTO;
import com.github.tavi.lilbird.api.dto.BirdSynonymDTO;
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
 * The RESTful controller for the {@link BirdIndexService}.
 * Allows managing the information about bird names
 * and other details <b>by administators</b>.
 */
@RestController
@RequestMapping(
    value = "api/admin",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class BirdIndexAdminApi {

    @Autowired
    private BirdIndexService service;


    @Operation(
        summary = "Create a new entry for a bird."
    )
    @ApiResponses(value = {
        @ApiResponse(
            description = "Returns the new bird entry",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "The request could not be processed (details provided)",
            responseCode = "400",
            content = @Content
        )
    })
    @PostMapping("/bird")
    public ResponseEntity<BirdEntry> newEntry(
            @RequestBody 
            @Valid 
                final BirdDTO birdDto
        ) 
    {
        final BirdEntry entry = service.save(birdDto.toEntity());
        return ResponseEntity
                .ok(entry);
    }

    
    @Operation(
        summary = "Assigns a new alternative name to the existing bird."
    )
    @ApiResponses(value = {
        @ApiResponse(
            description = "Returns the new synonym entity",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "The request could not be processed (details provided)",
            responseCode = "400",
            content = @Content
        ),
        @ApiResponse(
            description = "The entry does not exist",
            responseCode = "404",
            content = @Content
        )
    })
    @PostMapping("/bird/{id}/alt-name")
    public ResponseEntity<BirdSynonym> newSynonym(
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
                final long id,
            @RequestBody 
            @Valid 
                final BirdSynonymDTO synonymDto
        ) 
    {
        final BirdEntry original = service.getEntry(id);
        BirdSynonym synonym = synonymDto.toEntity();
        synonym.setOriginalEntry(original);
        synonym = service.save(synonym);
        return ResponseEntity
                .ok(synonym);
    }

}
