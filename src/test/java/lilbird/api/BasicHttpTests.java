package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.github.tavi.lilbird.App;
import com.github.tavi.lilbird.api.dto.BirdEntryDTO;
import com.github.tavi.lilbird.api.dto.BirdSynonymDTO;
import com.github.tavi.lilbird.db.entities.BirdEntryEntity;


/**
 * Tests for the basic REST API.
 * <p>
 * <b>Warning</b>: The tests teardown the database when executed.
 * Use only within the 'test' profile.
 */
@SpringBootTest(
    classes = App.class // Initialize the database
)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
public class BasicHttpTests {

    /**
     * The first posted object must be assigned this ID.
     */
    private final static int id = 1;

    private static BirdEntryDTO birdDto;
    private static BirdSynonymDTO synonymDto;


    @Autowired
    private RestTestClient rest;


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


    @BeforeEach
    public void postBird() {
        rest.post()
            .uri("/api/admin/bird")
            .body(birdDto)
            .exchange()
            .expectStatus().is2xxSuccessful();
    }


    @Test
    @DirtiesContext
    public void setupSuccess() {
        rest.get()
            .uri("/api/birds")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    @Test
    @DirtiesContext
    public void getNotFound() {
        final int unexistentId = id + 1;
        rest.get()
            .uri("/api/birds/{id}", unexistentId)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @DirtiesContext
    public void postNotFound() {
        final int unexistentId = id + 1;
        rest.post()
            .uri("/api/admin/bird/{id}/alt-name", unexistentId)
            .body(synonymDto)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @DirtiesContext
    public void postSynonymSuccess() {
        rest.post()
            .uri("/api/admin/bird/{id}/alt-name", id)
            .body(synonymDto)
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    @Test
    @DirtiesContext
    public void titleSearchSuccess() {
        rest.get()
            .uri("/api/search/title/{title}", birdDto.getTitle())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(BirdEntryEntity.class)
            .value(e -> assertEquals(id, e.getId()));
    }

}
