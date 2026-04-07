package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.dto.BirdCardDto;
import com.github.tavi.lilbird.api.dto.NameGroupDto;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.services.BirdCardsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


/**
 * The RESTful controller for the {@link BirdCardsService}.
 * Allows managing the information about bird names
 * and other details <b>by administators</b>.
 */
@RestController
@RequestMapping(
    value = "api/v1/admin",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class BirdManagementApi {

    @Autowired
    private BirdCardsService service;


    @Operation(
        summary = "Create a new entry for a bird."
    )
    @ApiResponses(value = {
        @ApiResponse(
            description = "Returns the new bird entry",
            responseCode = "200"
        )
    })
    @PostMapping("/birds")
    public ResponseEntity<BirdEntry> newEntry(
            @RequestBody 
            @Valid 
                final BirdCardDto card
        ) 
    {
        // Save new bird & get its ID
        final BirdEntry birdEntry = service.save(card.extractBirdEntry());

        // Save all names & etymologies
        card.getNames().stream()
            .map(NameGroupDto::mapToEntity)
            .peek(name -> name.setEntry(birdEntry))
            .forEach(service::save);

        return ResponseEntity.ok(birdEntry);
    }

}
