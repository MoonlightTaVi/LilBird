package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


/**
 * Tests for the bird card API.
 * <p>
 * Each test starts from an empty database. Use only the {@code test} profile.
 */
@SpringBootTest(classes = App.class)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicHttpTests {

    private static BirdCardDto cardDto;

    @BeforeAll
    public static void setup() {
        cardDto = new BirdCardDto("Bubo scandiacus");
        cardDto.setAlternativeNames(List.of(
            "Owl", "Polar Owl", "Полярная сова", "Nyctea scandiaca"
        ));
    }


    @Autowired
    private RestTestClient rest;

    /** Posts {@link #cardDto} before each test. */
    @BeforeEach
    public void postBird() {
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(cardDto)
            .exchange()
            .expectStatus().isCreated();
    }

    @Test
    public void birdStatusCreated() {
        BirdCardDto dto = new BirdCardDto("Corvus corax");
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(dto)
            .exchange()
            .expectStatus().isCreated();
    }

    @Test
    public void listBirds() {
        rest.get()
            .uri("/api/v1/birds")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(body -> {
                assertTrue(body.contains("\"nameLatin\":\"Bubo scandiacus\""));
                assertTrue(body.contains("\"nameNormal\":\"bubo+scandiacus\""));
            });
    }

    @Test
    public void getNotFound() {
        rest.get()
            .uri("/api/v1/birds/{title}", "Corvus corax")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void getSuccessful() {
        rest.get()
            .uri("/api/v1/birds/{title}", cardDto.getNameLatin())
            .exchange()
            .expectStatus().isOk()
            .expectBody(BirdCardDto.class)
            .value(card -> assertEquals(cardDto, card));
    }

    /** The normalized id resolves to the same card as the displayed latin name. */
    @Test
    public void getByNormalizedId() {
        rest.get()
            .uri("/api/v1/birds/{title}", "bubo+scandiacus")
            .exchange()
            .expectStatus().isOk()
            .expectBody(BirdCardDto.class)
            .value(card -> assertEquals(cardDto, card));
    }

    @Test
    public void nameValidation() {
        BirdCardDto invalidCard = new BirdCardDto("Hello, World!");
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(invalidCard)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void missingLatinName() {
        rest.post()
            .uri("/api/v1/admin/birds")
            .body(new BirdCardDto())
            .exchange()
            .expectStatus().isBadRequest();
    }

}
