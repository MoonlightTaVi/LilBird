package com.github.tavi.lilbird.api.dto;

import org.springframework.http.ResponseEntity;

import com.github.tavi.lilbird.db.entities.BirdEntry;
import com.github.tavi.lilbird.db.entities.BirdSynonym;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This Data Transfer Object is sent to the web-client
 * as the response to some request.
 * <p>
 * It contains the unique ID of some database entity
 * and (optionally) some informative message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdResponseDTO {

    @Min(
        value = 1, 
        message = "The ID number is always > 0"
    )
    @Schema(
        description = "The ID that can be used to obtain some database entity"
    )
    private Long id;

    @Schema(
        description = "Some additional message about the response",
        example = "Succesfully created"
    )
    private String message;


    /**
     * A shortcut for an HTTP response about the successful
     * creation of a new database entity (for bird entries).
     * 
     * @param entry     The bird entry that was successfully created.
     * @return          An HTTP response with details about this operation.
     */
    public ResponseEntity<IdResponseDTO> created(final BirdEntry entry) {
        id = entry.getId();
        message = "A new entry has been successfully created";
        return ResponseEntity.ok(this);
    }

    /**
     * A shortcut for an HTTP response about the successful
     * creation of a new database entity (for bird name synonyms).
     * 
     * @param synonym   The bird name synonym that was successfully created.
     * @return          An HTTP response with details about this operation.
     */
    public ResponseEntity<IdResponseDTO> created(final BirdSynonym synonym) {
        id = synonym.getId();
        message = "A new synonym has been successfully created";
        return ResponseEntity.ok(this);
    }
    
}
