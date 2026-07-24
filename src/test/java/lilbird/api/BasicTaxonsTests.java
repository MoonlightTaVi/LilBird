package lilbird.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.github.tavi.lilbird.App;
import com.github.tavi.lilbird.models.dto.TaxonDto;


/**
 * Tests for taxonomy services.
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
public class BasicTaxonsTests {

    static final String publicApi = "/api/v1/taxons";
    static final String privateApi = "/api/v1/admin/taxons";
    
    static final String taxonName = "family";
    static final String latinName = "Corvidae";
    static final int speciesLength = 135;
    static TaxonDto taxon;

    @BeforeAll
    public static void setup() {
        taxon = new TaxonDto();
        taxon.setTaxonName(taxonName);
        taxon.setLatinName(latinName);
        taxon.setSpeciesLength(speciesLength);
    }


    @Autowired
    RestTestClient client;

    @Test
    @Order(1)
    public void notFoundSuccess() {
        client.get()
            .uri(publicApi + "/{name}", taxon.getLatinName())
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    public void postSuccess() {
        client.post()
            .uri(privateApi)
            .body(taxon)
            .exchange()
            .expectStatus().isCreated();
    }

    @Test
    @Order(3)
    public void getByNameSuccess() {
        client.get()
            .uri(publicApi + "/{name}", taxon.getLatinName())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(TaxonDto.class)
            .value(body -> body.equals(taxon));
    }

    @Test
    @Order(4)
    public void getByLengthSuccess() {
        client.get()
            .uri(
                publicApi + "/filter?taxon={taxon}&species={species}", 
                taxon.getTaxonName(), 
                taxon.getSpeciesLength()
            )
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(List.class)
            .value(l -> assertEquals(1, l.size()))
            .value(l -> l.contains(taxon));
    }

    @Test
    @Order(4)
    public void getByLengthRangeSuccess() {
        int offset = 5;
        client.get()
            .uri(
                publicApi + "/filter?taxon={taxon}&species={species}&range={range}", 
                taxon.getTaxonName(), 
                taxon.getSpeciesLength(),
                offset * 2
            )
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(List.class)
            .value(l -> assertEquals(1, l.size()))
            .value(l -> l.contains(taxon));
    }

    @Test
    @Order(4)
    public void getByLengthNotFound() {
        client.get()
            .uri(
                publicApi + "/filter?taxon={taxon}&species={species}", 
                taxon.getTaxonName(), 
                0
            )
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(List.class)
            .value(l -> assertEquals(0, l.size()));
    }

}
