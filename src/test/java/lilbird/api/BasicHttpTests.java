package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
import com.github.tavi.lilbird.models.entities.NameGroupEntity;


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
        cardDto.setNameLatin("Raven");
        cardDto.setNameMain("Draven");

        final NameGroupEntity altNameGroup = new NameGroupEntity();
        altNameGroup.setNames(List.of("RAVEN"));
        altNameGroup.setEtymology("Same, but CAPS.");
        cardDto.addAltName(altNameGroup);
    }


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
    public void setupSuccess() {
        rest.get()
            .uri("/api/v1/birds")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }

    @Test
    @DirtiesContext
    public void getNotFound() {
        final int unexistentId = id + 1;
        rest.get()
            .uri("/api/v1/birds/{id}", unexistentId)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @DirtiesContext
    public void titleSearchSuccess() {
        rest.get()
            .uri("/api/v1/birds/{title}/card", cardDto.getNameLatin())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(BirdCardDto.class)
            .value(card -> assertEquals(cardDto, card));
    }

}
