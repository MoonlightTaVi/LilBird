package com.github.tavi.lilbird.api.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.api.models.BirdCard;
import com.github.tavi.lilbird.services.BirdService;


/** The public API for database viewing. */
@RestController
@RequestMapping(value = "api/v1/birds")
public class BirdsPublicAPI {

    @Autowired
    private BirdService service;
    
    
    /**
     * Returns the bird card for this latin name.
     * The path value is normalized before the lookup.
     */
    @GetMapping("/{name_latin}")
    public ResponseEntity<BirdCard> getCardByName(
            @PathVariable("name_latin") String nameLatin
        )
    {
        return ResponseEntity.ok(service.getCard(nameLatin));
    }

}
