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
import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.BirdEntity;
import com.github.tavi.lilbird.models.entities.NameGroupEntity;
import com.github.tavi.lilbird.services.BirdCardsService;
import com.github.tavi.lilbird.util.NameUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;


/** The public API for database viewing. */
@RestController
@RequestMapping(
    value = "api/v1/birds", 
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class BirdListingController {

    @Autowired
    private BirdCardsService service;


    // === All birds (latin names) ===

    @Operation(summary = "Get list of all birds (primary names only)")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<List<BirdEntity>> getBirds() {
        final List<BirdEntity> birds = service.getBirdNames();
        return ResponseEntity.ok(birds);
    }

    // === Bird cards === 
    
    @Operation(summary = "Find a filled bird card by the latin name")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{name_latin}/card")
    public ResponseEntity<BirdCardDto> getCardByName(
            @Schema(description = "The latin name of the bird") @PathVariable("name_latin") 
            final String nameLatin
        ) 
    {
        final BirdCardDto card = new BirdCardDto();

        final BirdEntity bird = service.getEntry(NameUtils.normalize(nameLatin));
        card.setNameLatin(bird.getNameLatin());
        card.setNameMain(bird.getNameMain());

        final List<NameGroupEntity> nameGroups = service.getNamesOf(bird.getId());
        for (final NameGroupEntity nameGroup : nameGroups) {
            card.addAltName(nameGroup);
        }

        return ResponseEntity.ok(card);
    }

    // === Basic info === 

    @Operation(summary = "Get a bird DB entry by numeric ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BirdEntity> getBirdById(
            @PathVariable("id") final long id
        ) 
    {
        return ResponseEntity.ok(service.getEntry(id));
    }

    @Operation(summary = "Get list of alternative names for a bird"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{id}/names")
    public ResponseEntity<List<NameGroupEntity>> getBirdNamesById(
            @Valid @Min(1) @PathVariable("id") final long id
        ) 
    {
        if (!service.entryExists(id)) {
            throw new NotFoundException("An entry by this ID does not exist");
        }
        final List<NameGroupEntity> nameGroups = service.getNamesOf(id);
        return ResponseEntity.ok(nameGroups);
    }

}
