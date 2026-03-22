package com.github.tavi.lilbird.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tavi.lilbird.db.entities.BirdEntryEntity;
import com.github.tavi.lilbird.services.BirdIndexService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * The REST API for the entity search.
 */
@RestController
@RequestMapping(
    value = "api/search",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class SearchApi {

    @Autowired
    private BirdIndexService service;
    

    @Operation(
        summary = "Find a bird by its title ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200"
        ),
        @ApiResponse(
            description = "This entry does not exist",
            responseCode = "404",
            content = @Content
        )
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<BirdEntryEntity> getById(
            @PathVariable("title") String title
        ) 
    {
        title = BirdEntryEntity.normalize(title);
        return ResponseEntity.ok(service.getEntry(title));
    }

}
