package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    public PersonDTO addPerson(String email, String firstName, String lastName, String phones, String phoneDescs, String street, String additionalInfo, String zipCode, String city) {
        EntityManager em = emf.createEntityManager();
            
        Address address = new Address(street, additionalInfo);
        Phone phone = new Phone(phones, phoneDescs);
        
        Person realPerson = new Person(email, firstName, lastName, address, phone);
       
        /*if(phones != null) {
            String[] phoneSplit = phones.split(",");
            String[] phoneDescSplit = phoneDescs.split(",");
           
            for (int i = 0; i < phoneSplit.length - 1; i++) {
                p.addPhone(phoneSplit[i], phoneDescSplit[i]);  
            }
        }*/
        try {
            CityInfo cityInfo = em.find(CityInfo.class, zipCode);
            if(cityInfo != null) {
                address.setCityInfo(cityInfo);
            }
       
            em.getTransaction().begin();
            em.persist(realPerson);
            em.getTransaction().commit();
            return new PersonDTO(realPerson);
        } finally {
            em.close();
        }
    }
    
     public PersonDTO editPerson(PersonDTO p) throws Exception {
        if ((p.getfName().length() == 0) || (p.getlName().length() == 0)){
           throw new Exception("First Name and/or Last Name is missing"); 
        }
        EntityManager em = getEntityManager();
        
        try {
            em.getTransaction().begin();
                Person person = em.find(Person.class, p.getId());
                if (person == null) {
                    throw new Exception(String.format("Person with id: (%d) not found", p.getId()));
                } else {
                    person.setEmail(p.getEmail());
                    person.setFirstName(p.getfName());
                    person.setLastName(p.getlName());
                    person.getAddress().setStreet(p.getStreet());
                    person.getAddress().setAdditionalInfo(p.getAdditionalInfo());
                    person.getAddress().getCityInfo().setCity(p.getCity());
                    person.getAddress().getCityInfo().setZipCode(p.getZipCode());
                    // ToDo: 
                    // Implement phone edit + hobby edit.
                }
                em.getTransaction().commit();
                return new PersonDTO(person);
        } finally {  
          em.close();
        }
        
        
    }
     
     
}
