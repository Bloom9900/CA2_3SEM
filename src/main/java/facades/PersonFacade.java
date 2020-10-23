package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import dto.PhoneDTO;
import dto.PhonesDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public PersonDTO addPerson(String email, String firstName, String lastName, String phones, String phoneDescs, String street, String additionalInfo, String zipCode, String city, String hobbies, String hobbyDescs) {
        EntityManager em = emf.createEntityManager();

        Address address = new Address(street, additionalInfo);
        //Phone phone = new Phone(phones, phoneDescs);

        Person realPerson = new Person(email, firstName, lastName, address);
        //realPerson.addPhone(phone);

        //addP(realPerson, phones, phoneDescs, Phone.class);
        //addP(realPerson, hobbies, hobbyDescs, Hobby.class);
        if (phones != null) {
            String[] aSplit = phones.split(",");
            String[] descSplit = phoneDescs.split(",");

            for (int i = 0; i < aSplit.length; i++) {
                if (descSplit.length > i) {
                    realPerson.addPhone(new Phone(aSplit[i], descSplit[i]));
                } else {
                    realPerson.addPhone(new Phone(aSplit[i], "No description"));

                }
            }

        }

        if (hobbies != null) {
            String[] hSplit = hobbies.split(",");
            String[] hDescSplit = hobbyDescs.split(",");

            for (int i = 0; i < hSplit.length; i++) {
                if (hDescSplit.length > i) {
                    realPerson.addHobby(new Hobby(hSplit[i], hDescSplit[i]));
                } else {
                    realPerson.addHobby(new Hobby(hSplit[i], "No description"));

                }
            }

        }
        try {
            CityInfo cityInfo = em.find(CityInfo.class, zipCode);
            if (cityInfo != null) {
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
        if ((p.getfName().length() == 0) || (p.getlName().length() == 0)) {
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
                PhonesDTO phones = new PhonesDTO(person.getPhoneNumbers());
                Set<Phone> newPhones = new HashSet();
                for (PhoneDTO phone : phones.getAll()) {
                    newPhones.add(new Phone(phone.getpNumbers(), phone.getpDescription()));
                }
                person.setPhonesNumbers(newPhones);
            }
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }

    }

    private static void addP(Person person, String desc, String a, Object aObj) {
        if (a != null) {
            String[] aSplit = a.split(",");
            String[] descSplit = desc.split(",");

            for (int i = 0; i < aSplit.length; i++) {
                if (descSplit.length > i) {
                    if (aObj.getClass().equals(Phone.class)) {
                        person.addPhone(new Phone(aSplit[i], descSplit[i]));
                    } else if (aObj.getClass().equals(Hobby.class)) {
                        person.addHobby(new Hobby(aSplit[i], descSplit[i]));
                    }
                } else {
                    if (aObj.getClass().equals(Phone.class)) {
                        person.addPhone(new Phone(aSplit[i], "No description"));
                    } else if (aObj.getClass().equals(Hobby.class)) {
                        person.addHobby(new Hobby(aSplit[i], "No description"));
                    }
                }
            }

        }
    }

}
