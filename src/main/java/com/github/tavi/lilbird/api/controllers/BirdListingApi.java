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

import com.github.tavi.lilbird.api.dto.BirdCard;
import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.NameGroupEntry;
import com.github.tavi.lilbird.services.BirdCardsService;

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
    value = "api/v1/birds", 
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class BirdListingApi {

    @Autowired
    private BirdCardsService service;


    // === All birds (latin names) ===

    @Operation(
        summary = "Get list of all birds (latin names only)"
    )
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<List<BirdEntry>> listAll() {
        final List<BirdEntry> entryList = service.getEntryList();
        return ResponseEntity.ok(entryList);
    }


    // === Bird cards === 
    
    @Operation(
        summary = "Find a filled bird card by the latin name"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{name_latin}/card")
    public ResponseEntity<BirdCard> getCard(
            @Schema(
                description = "The latin name of the bird"
            )
            @PathVariable("name_latin") 
                String nameLatin
        ) 
    {
        final BirdCard card = new BirdCard();

        // Find by normal name
        nameLatin = BirdEntry.normalize(nameLatin);
        final BirdEntry birdEntry = service.getEntry(nameLatin);
        card.setNameLatin(birdEntry.getNameLatin());

        // Collect all alternative names
        final List<NameGroupEntry> nameEntries = service
                .getNamesOf(birdEntry.getId());
        nameEntries.stream().forEach(n -> card.addNameGroup(n));

        return ResponseEntity.ok(card);
    }

    // === Basic info === 

    @Operation(
        summary = "Get latin and normal names by numeric ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BirdEntry> getById(
            @PathVariable("id") 
                final long id
        ) 
    {
        return ResponseEntity.ok(service.getEntry(id));
    }

    
    @Operation(
        summary = "Get list of alternative names for a bird"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content)
    })
    @GetMapping("/{id}/names")
    public ResponseEntity<List<NameGroupEntry>> synonymsOf(
            @Valid
            @Min(1)
            @PathVariable("id") 
                final long id
        ) 
    {
        if (!service.entryExists(id)) {
            throw new NotFoundException(
                    "An entry by this ID does not exist"
                );
        }

        final List<NameGroupEntry> entryList = service.getNamesOf(id);
        return ResponseEntity.ok(entryList);
    }

}
