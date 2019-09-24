import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

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


    @Test
    public void testJmeter(){
        StandardJMeterEngine jm = new StandardJMeterEngine();
        // jmeter.properties
//        JMeterUtils.loadJMeterProperties("c:/tmp/jmeter.properties");

        JMeterUtils.setProperty("sense.delay", "10000");
        ;

        HashTree hashTree = new HashTree();

        // HTTP Sampler
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setDomain("www.google.com");
        httpSampler.setPort(80);
        httpSampler.setPath("/");
        httpSampler.setMethod("GET");

        // Loop Controller
        TestElement loopCtrl = new LoopController();
        ((LoopController)loopCtrl).setLoops(1);
        ((LoopController)loopCtrl).addTestElement(httpSampler);
        ((LoopController)loopCtrl).setFirst(true);

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(1);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController((LoopController)loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");

        hashTree.add("testPlan", testPlan);
        hashTree.add("loopCtrl", loopCtrl);
        hashTree.add("threadGroup", threadGroup);
        hashTree.add("httpSampler", httpSampler);

        jm.configure(hashTree);

        jm.run();
    }
}
