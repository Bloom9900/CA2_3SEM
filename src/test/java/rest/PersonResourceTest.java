package rest;

import dto.CityInfoDTO;
import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import entities.Phone;
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
//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Address a1 = new Address("Strandparken 2A", "");
    private static Person p1 = new Person("cph-jm312@cphbusiness.dk", "Jannich", "Højmose", a1);
    private static Address a2 = new Address("Egevangen 4", "");
    private static Person p2 = new Person("cph-db145@cphbusiness.dk", "Daniel", "Bengtsen", a2);
    private static Address a3 = new Address("Gadevang 25", "");
    private static Person p3 = new Person("cph-eg60@cphbusiness.dk", "Emil", "Grønlund", a3);
    private static Address a4 = new Address("Københavnsvej 96", "");
    private static Person p4 = new Person("cph-jp327@cphbusiness.dk", "Jimmy", "Pham", a4);
    
    private static CityInfo ci1 = new CityInfo("2990", "Nivå");
    private static CityInfo ci2 = new CityInfo("8210", "Aarhus");
    private static CityInfo ci3 = new CityInfo("4140", "Borup");

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
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNativeQuery
                ("INSERT INTO CITYINFO (zip_code, city) VALUES (?, ?),(?, ?),(?, ?)")
                .setParameter(1, ci1.getZipCode())
                .setParameter(2, ci1.getCity())
                .setParameter(3, ci2.getZipCode())
                .setParameter(4, ci2.getCity())
                .setParameter(5, ci3.getZipCode())
                .setParameter(6, ci3.getCity())
                .executeUpdate();
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
    
    @Test
    public void testGetAllPersons() {
        List<PersonDTO> personsDTO = 
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("all", PersonDTO.class);
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        PersonDTO p3DTO = new PersonDTO(p3);
        PersonDTO p4DTO = new PersonDTO(p4);
        
        assertThat(personsDTO, containsInAnyOrder(p1DTO, p2DTO, p3DTO, p4DTO));
    }
    
    @Test
    public void testGetAllCityInfos() {
        List<CityInfoDTO> cityInfosDTO = 
        given()
                .contentType("application/json")
                .get("/person/all/zipcodes").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("all", CityInfoDTO.class);
        CityInfoDTO ci1DTO = new CityInfoDTO(ci1);
        CityInfoDTO ci2DTO = new CityInfoDTO(ci2);
        CityInfoDTO ci3DTO = new CityInfoDTO(ci3);
        
        assertThat(cityInfosDTO, containsInAnyOrder(ci1DTO, ci2DTO, ci3DTO));
    }
    
    @Test
    public void testGetPerson() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/"+p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("email", equalTo(p1.getEmail()));
    }
    
    @Test
    public void testAddPerson() throws Exception {
         given()
                .contentType("application/json")
                 .body(new Person("test@gmail.com", "Jannich", "Højmose", new Address("SecretAddress", "Additional info")))
                .then()
                .body("email", equalTo("test@gmail.com"))
                .body("fName", equalTo("Jannich"))
                .body("lName", equalTo("Højmose"));
    }
    
    @Test
    public void testEditPerson(){
        PersonDTO person = new PersonDTO(p1);
        person.setfName("Børge");
 
        given()
                .contentType("application/json")
                .body(person)
                .then()
                .body("fName", equalTo("Børge"))
                .body("lName", equalTo("Højmose"))
                .body("email", equalTo("cph-jm312@cphbusiness.dk"));
    }
}