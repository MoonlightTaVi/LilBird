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

import com.github.tavi.lilbird.api.HandledServerException;
import com.github.tavi.lilbird.api.NotFoundException;
import com.github.tavi.lilbird.api.dto.BirdDTO;
import com.github.tavi.lilbird.api.dto.BirdSynonymDTO;
import com.github.tavi.lilbird.api.dto.IdResponseDTO;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.BirdSynonym;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/admin")
@Validated
public class BirdIndexAdminApi {

    @Autowired
    private BirdIndexService service;


    @Operation(summary = "Create a new entry for a bird.")
    @ApiResponses(value = {
        @ApiResponse(
            description = "The body contains the ID of the new bird entry",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "The request could not be processed (details provided)",
            responseCode = "400",
            useReturnTypeSchema = false
        )
    })
    @PostMapping(
        value = "/bird", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IdResponseDTO> newEntry(
            @RequestBody 
            @Valid 
                final BirdDTO birdDto
        ) 
    {
        try {
            final BirdEntry entry = service.save(birdDto.toEntity());
            return new IdResponseDTO()
                    .created(entry);
        } catch (final HandledServerException e) {
            return new IdResponseDTO()
                    .badRequest(e);
        }
    }

    
    @Operation(summary = "Assigns a new alternative name to the existing bird.")
    @ApiResponses(value = {
        @ApiResponse(
            description = "The body contains the ID of the new synonym entity",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "The request could not be processed (details provided)",
            responseCode = "400",
            useReturnTypeSchema = false
        ),
        @ApiResponse(
            description = "The entry does not exist",
            responseCode = "404",
            useReturnTypeSchema = false
        )
    })
    @PostMapping(
        value = "/bird/{id}/alt-name", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IdResponseDTO> newSynonym(
            @Schema(
                description = "The ID of the original bird entry"
            )
            @PathVariable("id")
            @Valid 
            @Min(1) 
                final long id,
            @RequestBody 
            @Valid 
                final BirdSynonymDTO synonymDto
        ) 
    {
        try {
            final BirdEntry original = service.getEntry(id);
            BirdSynonym synonym = synonymDto.toEntity();
            synonym.setOriginalEntry(original);
            synonym = service.save(synonym);
            return new IdResponseDTO()
                    .created(synonym);
        } catch (final NotFoundException e) {
            return new IdResponseDTO()
                    .notFound(e);
        } catch (final HandledServerException e) {
            return new IdResponseDTO()
                    .badRequest(e);
        }
    }

}
