package com.github.tavi.lilbird.api.dto;

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
        example = "Successfully created a new database entry."
    )
    private String message;

}
