import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;


public class MockTest {


    WireMockServer wireMockServer;

    @Before
    public void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        setupStub();

    }

    @After
    public void teardown () {
        wireMockServer.stop();

    }

    public void setupStub() {

        wireMockServer.stubFor(get(urlEqualTo("/an/endpoint"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)
                        .withBodyFile("json/andyMegaResponse.json")));

        wireMockServer.stubFor(get(urlEqualTo("/an/companies"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)
                        .withBodyFile("json/andyMegaResponse.json")));

    }



    @Test
    public void testStatusCodePositive() {
        String body = given().
                when().
                get("http://localhost:8090/an/endpoint").
                then().
                extract()
                .asString();

        String valorAverificar= "running";
        JSONArray pincodes = JsonPath.read(body,"$[?(@.status=='"+valorAverificar+"' )].id");

        System.out.println(pincodes);

        System.out.println("running");

    }

    @Test
    public void testCompanies() {
        String body = given().
                when().
                get("http://localhost:8090/an/companies").
                then().
                extract()
                .asString();

        String valorAverificar= "running";
//        JSONArray pincodes = JsonPath.read(body);

        System.out.println(body);

        System.out.println("running");

    }
}
