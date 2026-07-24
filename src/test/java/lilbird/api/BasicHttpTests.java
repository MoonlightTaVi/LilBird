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
import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.NameGroup;


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

    /** Some latin name to use. */
    private final static String existentId = "Bubo scandiacus";
    /** Some placeholder latin name to not be used. */
    private final static String unexistentId = "Corvus corax";

    /** The request body. */
    private static BirdCardDto cardDto;


    @Autowired
    private RestTestClient rest;


    /**
     * Initializes data transfer objects for POSTing.
     * They may be updated from here.
     */
    @BeforeAll
    public static void setup() {
        cardDto = new BirdCardDto();
        cardDto.setNameLatin(existentId);
        cardDto.setNameMain("Owl");

        final NameGroup altNameGroup = new NameGroup();
        altNameGroup.setNames(new String[] {"Polar Owl", "Snowy Owl"});
        altNameGroup.setEtymology("Etymology of the owl.");
        cardDto.addAltName(altNameGroup);
    }


    /** Posts a new bird before each test. */
    @BeforeEach
    public void postBird() {
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(cardDto)
            .exchange()
            .expectStatus().is2xxSuccessful();
    }


    @Test
    @DirtiesContext
    public void birdStatusCreated() {
        BirdCardDto dto = new BirdCardDto();
        dto.setNameLatin(unexistentId);
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(dto)
            .exchange()
            .expectStatus().isCreated();
    }

    /** Must always respond HTTP 200. */
    @Test
    @DirtiesContext
    public void setupSuccess() {
        rest.get()
            .uri("/api/v1/birds")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    /** This card should NOT exist. */
    @Test
    @DirtiesContext
    public void getNotFound() {
        rest.get()
            .uri("/api/v1/birds/{title}", unexistentId)
            .exchange()
            .expectStatus().isNotFound();
    }

    /** This card should exist. */
    @Test
    @DirtiesContext
    public void getSuccessful() {
        rest.get()
            .uri("/api/v1/birds/{title}", cardDto.getNameLatin())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(BirdCardDto.class)
            .value(card -> assertEquals(cardDto, card));
    }

}
