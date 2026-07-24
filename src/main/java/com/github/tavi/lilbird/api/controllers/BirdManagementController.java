package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.Bird;
import com.github.tavi.lilbird.models.entities.NameGroup;
import com.github.tavi.lilbird.services.BirdCardsService;

import jakarta.validation.Valid;


/**
 * The RESTful controller for the {@link BirdCardsService}.
 * Allows managing the information about bird names
 * and other details <b>by administators</b>.
 */
@Validated
@RestController
@RequestMapping("api/v1/admin")
public class BirdManagementController {

    @Autowired
    private BirdCardsService service;


    /** 
     * Creates a new {@link Bird} and the {@link NameGroup} objects
     * that are associated with it.
     */
    @PostMapping("/birds")
    @ResponseStatus(HttpStatus.CREATED)
    public void newBird(@RequestBody @Valid BirdCardDto card) {
        Bird bird = new Bird();
        bird.setNameLatin(card.getNameLatin());
        bird.setNameMain(card.getNameMain());

        // After the ID field is set:
        bird = service.save(bird);
        for (int i = 0; i < card.nameCount(); i++) {
            final NameGroup nameGroup = card.getNameGroup(i);
            nameGroup.setEntry(bird);
            service.save(nameGroup);
        }
    }

}
