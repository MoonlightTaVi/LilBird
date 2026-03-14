package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.HandledServerException;
import com.github.tavi.lilbird.api.dto.BirdDTO;
import com.github.tavi.lilbird.api.dto.IdResponseDTO;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


/**
 * The RESTful controller for the {@link BirdIndexService}.
 * Allows managing the information about bird names
 * and other details <b>by administators</b>.
 */
@RestController
@RequestMapping("api/admin/birds")
@Validated
public class BirdIndexAdminApi {

    @Autowired
    private BirdIndexService service;


    @Operation(summary = "Create a new entry for a bird.")
    @ApiResponse(
        description = "The body contains the ID of the new bird entry",
        responseCode = "200",
        useReturnTypeSchema = true
    )
    @ApiResponse(
        description = "The request could not be processed (details provided)",
        responseCode = "400",
        useReturnTypeSchema = false
    )
    @PostMapping(
        value = "/create", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IdResponseDTO> newEntry(
            @RequestBody @Valid final BirdDTO birdDto
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

}
