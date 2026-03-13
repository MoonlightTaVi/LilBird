package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.dto.BirdDTO;
import com.github.tavi.lilbird.api.dto.IdResponseDTO;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


/**
 * The RESTful controller for the {@link BirdIndexService}.
 * Allows viewing and managing the information about bird names
 * and other textual information.
 */
@RestController
@RequestMapping("api/v1/birds")
@Validated
public class BirdIndexApi {

    @Autowired
    private BirdIndexService service;


    @Operation(summary = "Create a new entry for a bird.")
    @ApiResponse(
        description = "Returns the ID of the new bird entry.",
        responseCode = "200",
        useReturnTypeSchema = true
    )
    @PostMapping(
        value = "/create", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IdResponseDTO> postEntry(
            @RequestBody @Valid final BirdDTO birdDto
        ) 
    {
        // TODO Catch possible exceptions to prevent HTTP 500
        final BirdEntry entry = service.save(birdDto.toEntity());
        return ResponseEntity.ok(
                new IdResponseDTO(
                    entry.getId(), 
                    "A new bird entry has been created."
                )
            );
    }

}
