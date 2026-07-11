package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.BirdEntity;
import com.github.tavi.lilbird.models.entities.NameGroupEntity;
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
public class BirdManagementController {

    @Autowired
    private BirdCardsService service;


    @Operation(summary = "Create a new entry for a bird.")
    @ApiResponses({
        @ApiResponse(
            description = "Returns the new bird entry unique ID",
            responseCode = "200"
        )
    })
    @PostMapping("/birds")
    public ResponseEntity<Long> newEntry(@RequestBody @Valid final BirdCardDto card) {
        BirdEntity bird = new BirdEntity();
        bird.setNameLatin(card.getNameLatin());
        bird.setNameMain(card.getNameMain());

        // After the ID field is set:
        bird = service.save(bird);
        for (int i = 0; i < card.nameCount(); i++) {
            final NameGroupEntity nameGroup = card.getNameGroup(i);
            nameGroup.setEntry(bird);
            service.save(nameGroup);
        }

        return ResponseEntity.ok(bird.getId());
    }

}
