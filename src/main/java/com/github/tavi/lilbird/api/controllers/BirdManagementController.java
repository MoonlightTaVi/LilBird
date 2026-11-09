package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.models.BirdCard;
import com.github.tavi.lilbird.services.BirdCardsService;

import jakarta.validation.Valid;


/**
 * The RESTful controller for the {@link BirdCardsService}.
 * Allows administrators to create a bird and the names attached to it.
 */
@Validated
@RestController
@RequestMapping("api/v1/admin")
public class BirdManagementController {

    @Autowired
    private BirdCardsService service;


    /**
     * Disassembles the card into different database entities
     * and saves them separately.
     */
    @PostMapping("/birds")
    @ResponseStatus(HttpStatus.CREATED)
    public void newBird(@RequestBody @Valid BirdCard card) {
        service.save(card);
    }

}
