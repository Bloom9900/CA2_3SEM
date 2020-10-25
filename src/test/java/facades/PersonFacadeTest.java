package facades;

import dto.CityInfoDTO;
import dto.CityInfosDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import entities.Phone;
import exceptions.DublicateException;
import exceptions.MissingInputException;
import exceptions.ObjectNotFoundException;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    
    private static CityInfo ci1 = new CityInfo("2990", "Nivå");
    private static CityInfo ci2 = new CityInfo("8210", "Aarhus");
    private static CityInfo ci3 = new CityInfo("4140", "Borup");
    

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
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();    
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();   
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();      
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNativeQuery("DELETE FROM HOBBY_PERSON").executeUpdate();
            em.createNativeQuery("ALTER TABLE PERSON AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE ADDRESS AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery
                ("INSERT INTO CITYINFO (zip_code, city) VALUES (?, ?),(?, ?),(?, ?)")
                .setParameter(1, ci1.getZipCode())
                .setParameter(2, ci1.getCity())
                .setParameter(3, ci2.getZipCode())
                .setParameter(4, ci2.getCity())
                .setParameter(5, ci3.getZipCode())
                .setParameter(6, ci3.getCity())
                .executeUpdate();
            CityInfo cityInfo1 = em.find(CityInfo.class, ci1.getZipCode());
            CityInfo cityInfo2 = em.find(CityInfo.class, ci2.getZipCode());
            CityInfo cityInfo3 = em.find(CityInfo.class, ci3.getZipCode());
            a1.setCityInfo(cityInfo1);
            a2.setCityInfo(cityInfo2);
            a3.setCityInfo(cityInfo3);
            p1.addAddress(a1);
            p2.addAddress(a2);
            p3.addAddress(a3);
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
    public void testGetAllPersons() throws ObjectNotFoundException {
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
    public void testGetAllCityInfos() throws ObjectNotFoundException {
        int expResult = 3;
        CityInfosDTO result = facade.getAllCityInfos();
        assertEquals(expResult, result.getAll().size());
        CityInfoDTO ci1DTO = new CityInfoDTO(ci1);
        CityInfoDTO ci2DTO = new CityInfoDTO(ci2);
        CityInfoDTO ci3DTO = new CityInfoDTO(ci3);
        assertThat(result.getAll(), containsInAnyOrder(ci1DTO, ci2DTO, ci3DTO));
    }
    
    
    
    @Test
    public void testGetPerson() throws ObjectNotFoundException {
        PersonDTO result = facade.getPerson(p1.getId());
        PersonDTO expected = new PersonDTO(p1);
        assertEquals(result, expected);
    }
    
    @Test
    public void testAddPerson() throws MissingInputException, ObjectNotFoundException, DublicateException {
        String email = "test@gmail.com";
        String fName = "Test";
        String lName = "Tester";
        String street = "Testvej";
        String additionalInfo = "Test info";
        String zipCode = "2990";
        String city = "Nivå";
        String phoneNums = "12345678,87654321";
        String phoneDescs = "Arbejde,Privat";
        String hobbyNames = "Dans,Taekwondo";
        String hobbyDescs = "testDescs";
        PersonDTO result = facade.addPerson(email, fName, lName, phoneNums, phoneDescs, street, additionalInfo, zipCode, city, hobbyNames, hobbyDescs);
        Person p = new Person(email, fName, lName, new Address(street, additionalInfo));
        PersonDTO expected = new PersonDTO(p);
        assertEquals(expected.getEmail(), result.getEmail());
    }
    
    @Test
    public void testEditPerson() throws Exception {
        PersonDTO pTestCreate = new PersonDTO(p1);
        PersonDTO expResult = new PersonDTO(p1);
        pTestCreate.setfName("Hans");
        expResult.setfName("Hans");
        
        PersonDTO result = facade.editPerson(pTestCreate);
        assertEquals(expResult.getfName(), result.getfName());
    }
}