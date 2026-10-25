package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.github.tavi.lilbird.App;
import com.github.tavi.lilbird.enums.TaxonType;
import com.github.tavi.lilbird.models.dto.TaxonCard;


/**
 * Tests for the taxonomy API. Each test starts from an empty database.
 */
@SpringBootTest(classes = App.class)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicTaxonsTests {

    static final String publicApi = "/api/v1/taxons";
    static final String privateApi = "/api/v1/admin/taxons";

    static final TaxonType taxonType = TaxonType.FAMILY;
    static final String latinName = "Corvidae";
    static final int speciesLength = 135;
    static TaxonCard taxon;

    @BeforeAll
    public static void setup() {
        taxon = new TaxonCard();
        taxon.setNameLatin(latinName);
        taxon.setSpeciesCount(speciesLength);
    }


    @Autowired
    RestTestClient client;

    private void postTaxon() {
        client.post()
            .uri(privateApi + "/family")
            .body(taxon)
            .exchange()
            .expectStatus().isCreated();
    }

    //@Test
    public void notFoundSuccess() {
        client.get()
            .uri(publicApi + "/{name}", taxon.getNameLatin())
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void postSuccess() {
        postTaxon();
    }

    //@Test
    public void getByNameSuccess() {
        postTaxon();
        client.get()
            .uri(publicApi + "/{name}", taxon.getNameLatin())
            .exchange()
            .expectStatus().isOk()
            .expectBody(TaxonCard.class)
            .value(body -> assertEquals(taxon, body));
    }

    @Test
    public void getByLengthSuccess() {
        postTaxon();
        client.get()
            .uri(
                publicApi + "/filter/family?species={species}",
                taxon.getSpeciesCount()
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<TaxonCard>>() {})
            .value(list -> {
                assertEquals(1, list.size());
                assertEquals(taxon, list.get(0));
            });
    }

    @Test
    public void getByLengthRangeSuccess() {
        postTaxon();
        int offset = 5;
        client.get()
            .uri(
                publicApi + "/filter/family?species={species}&range={range}",
                taxon.getSpeciesCount() - offset,
                offset * 2
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<TaxonCard>>() {})
            .value(list -> {
                assertEquals(1, list.size());
                assertEquals(taxon, list.get(0));
            });
    }

    @Test
    public void getByLengthNotFound() {
        postTaxon();
        client.get()
            .uri(
                publicApi + "/filter/family?species={species}",
                0
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<TaxonCard>>() {})
            .value(list -> assertEquals(0, list.size()));
    }

    @Test
    public void rejectInvalidLatinName() {
        TaxonCard invalid = new TaxonCard();
        invalid.setNameLatin("Hello, World!");
        invalid.setSpeciesCount(1);
        client.post()
            .uri(privateApi + "/family")
            .body(invalid)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void rejectNonPositiveSpeciesLength() {
        TaxonCard invalid = new TaxonCard();
        invalid.setNameLatin("Corvidae");
        invalid.setSpeciesCount(0);
        client.post()
            .uri(privateApi + "/family")
            .body(invalid)
            .exchange()
            .expectStatus().isBadRequest();
    }

}
