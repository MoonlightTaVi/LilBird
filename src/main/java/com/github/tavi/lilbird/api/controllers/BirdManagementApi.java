package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.dto.BasicNameGroup;
import com.github.tavi.lilbird.api.dto.BirdCard;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.NameGroupEntry;
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
                final BirdCard card
        ) 
    {
        final List<BasicNameGroup> names = card.getNames();

        // Save new bird & get its ID
        BirdEntry birdEntry = new BirdEntry();
        birdEntry.setNameLatin(card.getNameLatin());
        birdEntry = service.save(birdEntry);

        // Save all names & etymologies
        for (final BasicNameGroup name : names) {
            final NameGroupEntry nameEntry = new NameGroupEntry();
            nameEntry.setEntry(birdEntry);
            nameEntry.copyFrom(name);

            service.save(nameEntry);
        }

        return ResponseEntity.ok(birdEntry);
    }

}
