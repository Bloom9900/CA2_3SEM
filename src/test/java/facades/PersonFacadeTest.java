package facades;


import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Address a1 = new Address("Strandparken 2A", "");
    private static Person p1 = new Person("cph-jm312@cphbusiness.dk", "Jannich", "Højmose", a1);
    private static Address a2 = new Address("Egevangen 4", "");
    private static Person p2 = new Person("cph-db145@cphbusiness.dk", "Daniel", "Bengtsen", a2);
    private static Address a3 = new Address("Gadevang 25", "");
    private static Person p3 = new Person("cph-eg60@cphbusiness.dk", "Emil", "Grønlund", a3);
    private static Address a4 = new Address("Københavnsvej 96", "");
    private static Person p4 = new Person("cph-jp327@cphbusiness.dk", "Jimmy", "Pham", a4);

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }
    
    
    //For at denne test kan virke med assertThat kræver det en equals metode i PersonDTO, ellers kan den ikke sammenligne.
    @Test
    public void testGetAllPersons() {
        int expResult = 4;
        PersonsDTO result = facade.getAllPersons();
        assertEquals(expResult, result.getAll().size());
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        PersonDTO p3DTO = new PersonDTO(p3);
        PersonDTO p4DTO = new PersonDTO(p4);
        assertThat(result.getAll(), containsInAnyOrder(p1DTO, p2DTO, p3DTO, p4DTO));
    }
    
    @Test
    public void testGetPerson() {
        PersonDTO result = facade.getPerson(p1.getId());
        PersonDTO expected = new PersonDTO(p1);
        assertEquals(result, expected);
    }
    
    @Disabled
    @Test
    public void testAddPerson() {
        String email = "test@gmail.com";
        String fName = "Test";
        String lName = "Tester";
        String street = "Testvej";
        String additionalInfo = "Test info";
        String zipCode = "1000";
        String city = "TestCity";
        String phoneNums = "12345678,87654321";
        String phoneDescs = "Arbejde,Privat";
        PersonDTO result = facade.addPerson(email, fName, lName, phoneNums, phoneDescs, street, additionalInfo, zipCode, city);
        Person p = new Person(email, fName, lName, new Address(street, additionalInfo));
        PersonDTO expected = new PersonDTO(p);
        assertEquals(expected.getEmail(), result.getEmail());
    }
}