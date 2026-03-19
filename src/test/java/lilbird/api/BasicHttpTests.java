package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.github.tavi.lilbird.App;
import com.github.tavi.lilbird.api.dto.BirdEntryDTO;
import com.github.tavi.lilbird.api.dto.BirdSynonymDTO;
import com.github.tavi.lilbird.db.entities.BirdEntryEntity;
import com.github.tavi.lilbird.db.entities.BirdSynonymEntity;


/**
 * Tests for the basic REST API.
 * <p>
 * <b>Warning</b>: The tests change the database state, 
 * therefore should be ran in the H2 {@code mem} mode (the 'test' profile).
 */
@SpringBootTest(
    classes = App.class // Initialize the database
)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
@TestMethodOrder(
    MethodOrderer.OrderAnnotation.class // Ordering is required
)
public class BasicHttpTests {

    /**
     * The first posted object must be assigned this ID.
     */
    private final static int expectedId = 1;

    private static BirdEntryDTO birdDto;
    private static BirdSynonymDTO synonymDto;


    /**
     * Initializes data transfer objects for POSTing.
     * They may be updated from here.
     */
    @BeforeAll
    public static void setup() {
        birdDto = new BirdEntryDTO();
        birdDto.setTitle("Raven");
        birdDto.setCommonName("Draven");

        synonymDto = new BirdSynonymDTO();
        synonymDto.setName("DRAVEN");
        synonymDto.setComment("Same, but ALL_CAPS.");
    }


    @Autowired
    private RestTestClient rest;


    /**
     * A simple HTTP 200 test.
     */
    @Test
    @Order(1)
    public void setupSuccess() {
        rest.get().uri("/api/birds")
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    /**
     * The database must be empty, and the server should respond with 404.
     */
    @Test
    @Order(2)
    public void getNotFound() {
        // An unexistent bird
        rest.get()
            .uri("/api/birds/{id}", expectedId)
            .exchange()
            .expectStatus()
            .isNotFound();
        
        // Getting synonyms of the unexistent bird
        rest.get()
            .uri("/api/birds/{id}/alt-names", expectedId)
            .exchange()
            .expectStatus()
            .isNotFound();
        
        // Posting a synonym to the unexistent bird
        rest.post()
            .uri("/api/admin/bird/{id}/alt-name", expectedId)
            .body(synonymDto)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    @Order(3)
    public void postSuccessessful() {
        // Posting the bird (now it is existent)
        rest.post()
            .uri("/api/admin/bird")
            .body(birdDto)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(BirdEntryEntity.class)
            // Assert with DTO fields
            .value(bird -> {
                assertEquals(expectedId, bird.getId());
                assertEquals(birdDto.getTitle(), bird.getTitle());
                assertEquals(birdDto.getCommonName(), bird.getCommonName());
            });
        
        // Getting the synonyms of the bird (now it is an empty list)
        rest.get()
            .uri("/api/birds/{id}/alt-names", expectedId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(
                    new ParameterizedTypeReference<List<BirdSynonymEntity>>() {}
                )
            .value(
                list -> assertTrue(list.isEmpty())
            );
        
        // Posting a synonym to this bird
        rest.post()
            .uri("/api/admin/bird/{id}/alt-name", expectedId)
            .body(synonymDto)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(BirdSynonymEntity.class)
            // Assert with DTO fields
            .value(synonym -> {
                assertEquals(expectedId, synonym.getId());
                assertEquals(synonymDto.getName(), synonym.getName());
                assertEquals(synonymDto.getComment(), synonym.getComment());
            });
    }

}
