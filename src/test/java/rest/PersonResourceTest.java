package rest;

import dto.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
@Disabled

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Set<String> h1 = new HashSet<>(Arrays.asList("CS:GO", "Programmering"));
    private static Person p1 = new Person("Jannich", "Højmose", "cph-jm312@cphbusiness.dk", "23656270", h1);
    private static Set<String> h2 = new HashSet<>(Arrays.asList("Musik", "Programmering"));
    private static Person p2 = new Person("Daniel", "Bengtsen", "cph-db145@cphbusiness.dk", "51784562", h2);
    private static Set<String> h3 = new HashSet<>(Arrays.asList("Forum", "Programmering"));
    private static Person p3 = new Person("Emil", "Grøndlund", "cph-eg60@cphbusiness.dk", "87562147", h3);
    private static Set<String> h4 = new HashSet<>(Arrays.asList("Vaping", "Programmering"));
    private static Person p4 = new Person("Jimmy", "Pham", "cph-jp327@cphbusiness.dk", "65547125", h4);

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }
    
    //Denne metode skal også bruge equals for at kunne virke
//    @Test
//    public void testGetAllPersons() throws Exception {
//        List<PersonDTO> personsDTO = null;
//        given()
//                .contentType("application/json")
//                .get("/person/all").then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .extract().body().jsonPath().getList("all", PersonDTO.class);
//        PersonDTO p1DTO = new PersonDTO(p1);
//        PersonDTO p2DTO = new PersonDTO(p2);
//        PersonDTO p3DTO = new PersonDTO(p3);
//        
//        assertThat(personsDTO, containsInAnyOrder(p1DTO, p2DTO, p3DTO));
//    }
}