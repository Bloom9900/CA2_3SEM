package facades;

import dto.CityInfosDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            return new PersonsDTO(em.createNamedQuery("Person.getAllRows").getResultList());
        } finally {
            em.close();
        }
    }

    public PersonDTO getPerson(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new PersonDTO(em.find(Person.class, id));
        } finally {
            em.close();
        }
    }
    
    public String getPhoneNumberByCity(String city) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, city);
            return person.getPhone();
        } finally {
            em.close();
        }
    }
    
    public CityInfosDTO getZipCodes() {
        EntityManager em = emf.createEntityManager();
        try {
            return new CityInfosDTO(em.createNamedQuery("CityInfo.getAllRows", CityInfo.class).getResultList());
        } finally {
            em.close();
        }
    }
    
    public PersonDTO addPerson(String fName, String lName, String phone, String email, Address address ) {
        EntityManager em = getEntityManager();
        Person person = new Person(fName, lName, phone, email, address);

        try {
            em.getTransaction().begin();
                /*Query query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :city");
                query.setParameter("street", address.getStreet());
                query.setParameter("zip", address.getZipCode());
                query.setParameter("city", address.getCity());
                List<Address> addresses = query.getResultList();
                if (addresses.size() > 0){
                    person.setAddress(addresses.get(0)); // The address already exists
                } else {
                    person.setAddress(new Address(address.getStreet(), address.getZipCode(), address.getCity()));
                }*/
                em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }
    
    /*
     @Path("{city}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPhoneNumberByCity(@PathParam("city") String city) {
     //   return gson.toJson(facade.getPhoneNumberByCity(city));
     return "{\"msg\":\"Hello World\"}";
    }
    */

}
