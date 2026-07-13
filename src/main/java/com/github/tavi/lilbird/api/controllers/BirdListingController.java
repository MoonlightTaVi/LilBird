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

import io.swagger.v3.oas.annotations.media.Schema;
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

    @GetMapping
    public ResponseEntity<List<BirdEntity>> getBirds() {
        final List<BirdEntity> birds = service.getAllBirds();
        return ResponseEntity.ok(birds);
    }

    // === Bird cards === 
    
    @GetMapping("/cards/{name_latin}")
    public ResponseEntity<BirdCardDto> getCardByName(
            @Schema(description = "The latin name of the bird") @PathVariable("name_latin") 
            final String nameLatin
        ) 
    {
        final BirdCardDto card = new BirdCardDto();

        final BirdEntity bird = service.getEntryByName(NameUtils.normalize(nameLatin));
        card.setNameLatin(bird.getNameLatin());
        card.setNameMain(bird.getNameMain());

        final List<NameGroupEntity> nameGroups = service.getNamesOf(bird.getId());
        for (final NameGroupEntity nameGroup : nameGroups) {
            card.addAltName(nameGroup);
        }

        return ResponseEntity.ok(card);
    }

    // === Basic info === 

    @GetMapping("/{id}")
    public ResponseEntity<BirdEntity> getBirdById(
            @PathVariable("id") final long id
        ) 
    {
        return ResponseEntity.ok(service.getEntryById(id));
    }

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
