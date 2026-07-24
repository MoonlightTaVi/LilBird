package com.github.tavi.lilbird.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.Bird;
import com.github.tavi.lilbird.models.entities.NameGroup;
import com.github.tavi.lilbird.services.BirdCardsService;


/** The public API for database viewing. */
@RestController
@RequestMapping(value = "api/v1/birds")
public class BirdListingController {

    @Autowired
    private BirdCardsService service;


    /** The list of all {@link Bird} objects. */
    @GetMapping("")
    public ResponseEntity<List<Bird>> getBirds() {
        // TODO Might be expensive to send all of the DB rows; add paging?
        final List<Bird> birds = service.getAllBirds();
        return ResponseEntity.ok(birds);
    }
    
    /** Returns the bird card by the given name ID. */
    @GetMapping("/{name_latin}")
    public ResponseEntity<BirdCardDto> getCardByName(
            @PathVariable("name_latin") String nameLatin
        ) 
    {
        final BirdCardDto card = new BirdCardDto();

        final Bird bird = service.getEntryByName(nameLatin);
        card.setNameLatin(bird.getNameLatin());
        card.setNameMain(bird.getNameMain());

        final List<NameGroup> nameGroups = service.getNamesOf(bird);
        for (final NameGroup nameGroup : nameGroups) {
            card.addAltName(nameGroup);
        }

        return ResponseEntity.ok(card);
    }

}
