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

import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.BirdEntity;
import com.github.tavi.lilbird.models.entities.NameGroupEntity;
import com.github.tavi.lilbird.services.BirdCardsService;


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

    @GetMapping("")
    public ResponseEntity<List<BirdEntity>> getBirds() {
        final List<BirdEntity> birds = service.getAllBirds();
        return ResponseEntity.ok(birds);
    }

    // === Bird cards === 
    
    @GetMapping("/{name_latin}")
    public ResponseEntity<BirdCardDto> getCardByName(
            @PathVariable("name_latin") String nameLatin
        ) 
    {
        final BirdCardDto card = new BirdCardDto();

        final BirdEntity bird = service.getEntryByName(nameLatin);
        card.setNameLatin(bird.getNameLatin());
        card.setNameMain(bird.getNameMain());

        final List<NameGroupEntity> nameGroups = service.getNamesOf(bird);
        for (final NameGroupEntity nameGroup : nameGroups) {
            card.addAltName(nameGroup);
        }

        return ResponseEntity.ok(card);
    }

}
