import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class RestAssuredTest {



    WireMockServer wireMockServer;

    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        setupStub();
    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }

    public void setupStub() {

    wireMockServer.stubFor(get(urlEqualTo("/an/endpoint"))
            .willReturn(aResponse()
            .withHeader("Content-Type", "text/plain")
            .withStatus(200)
            .withBodyFile("json/andyMegaResponse.json")));
    }



        //    seria como un: select id(1) where status = "running";
        //    solo 1 id

        //    necesito rescatar un id, cualquiera, pero solo 1, donde el status sea "running"

        //    Do you know how can I set Rest Assured to return only 1 register with status "running" ?
        //    I'm building a script that will validate all notive link in the app return 200 code. In order to do this I need the script to get "active" or "running" ids for each register for me to include them in the URLs I'm testing
        //    for example:
        //    https://[ENVIRONMENT]/publisher/dashboard/performance?publisher=[PUBLISHER_ID]
        //    I will need to obtain a [PUBLISHER_ID] that is status = "running"
        //    and set it in the URL and them hit the endpoint
        //    expecting a 200
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

        //System.out.println(pincodes.get(0));

    }


}
