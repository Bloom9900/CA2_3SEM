package facades;

import dto.CityInfosDTO;
import dto.HobbiesDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import dto.PhoneDTO;
import dto.PhonesDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.MissingInputException;
import exceptions.DublicateException;
import exceptions.ObjectNotFoundException;
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

    public PersonsDTO getAllPersons() throws ObjectNotFoundException {
        EntityManager em = getEntityManager();
        try {
            PersonsDTO allPersons = new PersonsDTO(em.createNamedQuery("Person.getAllRows").getResultList());
            if(allPersons.getAll().isEmpty() || allPersons == null) {
                throw new ObjectNotFoundException("No persons found.");
            }
            return allPersons;
        } finally {
            em.close();
        }
    }
    
    public CityInfosDTO getAllCityInfos() throws ObjectNotFoundException {
        EntityManager em = getEntityManager();
        try {
            List<CityInfo> ciList = em.createNamedQuery("CityInfo.getAllRows").getResultList();
            Set<CityInfo> ciSet = new HashSet();
            for(CityInfo ci : ciList) {
                ciSet.add(ci);
            }
            if(ciSet.isEmpty()) {
                throw new ObjectNotFoundException("No cities or zipcodes found.");
            }
            return new CityInfosDTO(ciSet);
        } finally {
            em.close();
        }   
    }

    public PersonDTO getPerson(Long id) throws ObjectNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person p = em.find(Person.class, id);
            if(p == null) {
                throw new ObjectNotFoundException(String.format("Person with the provided id: %d not found.", id));
            }
            return new PersonDTO(p);
        } finally {
            em.close();
        }
    }

    public PersonDTO addPerson
        (
            String email, 
            String firstName, 
            String lastName, 
            String phones, 
            String phoneDescs, 
            String street, 
            String additionalInfo, 
            String zipCode, 
            String city, 
            String hobbies, 
            String hobbyDescs
        ) throws MissingInputException, ObjectNotFoundException, DublicateException {
        
        EntityManager em = getEntityManager();

        Address address = new Address(street, additionalInfo);

        Person realPerson = new Person(email, firstName, lastName, address);
        
        if(street == null || street.isEmpty()) 
        {
            throw new MissingInputException("Field \"street\" is required.");
        } 
        else if(firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) 
        {
            throw new MissingInputException("Field \"fName\" AND \"lName\" is required.");
        } 
        else if(phones == null || phones.isEmpty()) 
        {
            throw new MissingInputException("Field \"pNumbers\" is required.");
        } 
        else if(zipCode == null || zipCode.isEmpty()) 
        {
            throw new MissingInputException("Field \"zipCode\" is required.");
        }
        
        List<String> failedNumbers = new ArrayList();

        try {
            if (phones != null) {
                List<Phone> phonesToValidate = createPhoneListFromJSON(phones, phoneDescs);
                for (Phone phone : phonesToValidate) {
                    TypedQuery<Phone> phoneQuery = em.createQuery("SELECT p FROM Phone p WHERE p.number = :numbers", Phone.class);
                    phoneQuery.setParameter("numbers", phone.getNumber());
                    Phone phoneFromDB = new Phone();

                    if (!phoneQuery.getResultList().isEmpty()) {
                        phoneFromDB = (Phone) phoneQuery.getSingleResult();
                    }
                    if (!(phone.getDescription().isEmpty())) {
                        if (phone.getNumber().equals(phoneFromDB.getNumber())) {
                            failedNumbers.add(phone.getNumber());
                        } else {
                            realPerson.addPhone(new Phone(phone.getNumber(), phone.getDescription()));
                        }
                    } else {
                        if (phone.getNumber().equals(phoneFromDB.getNumber())) {
                            failedNumbers.add(phone.getNumber());
                        } else {
                            realPerson.addPhone(new Phone(phone.getNumber(), "No description"));
                        }
                    }
                }
            }

            if (hobbies != null) {
                List<Hobby> hobbiesToValidate = createHobbyListFromJSON(hobbies, hobbyDescs);
                for (Hobby hobby : hobbiesToValidate) {
                    TypedQuery<Hobby> hobbyQuery = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobbyNames", Hobby.class);
                    hobbyQuery.setParameter("hobbyNames", hobby.getName());
                    Hobby hobbyFromDB = new Hobby();

                    if (!hobbyQuery.getResultList().isEmpty()) {
                        hobbyFromDB = (Hobby) hobbyQuery.getSingleResult();
                    }
                    if (!(hobby.getDescription().isEmpty())) {
                        if (hobby.getName().equals(hobbyFromDB.getName())) {
                            realPerson.addHobby(hobbyFromDB);
                        } else {
                            realPerson.addHobby(new Hobby(hobby.getName(), hobby.getDescription()));
                        }
                    } else {
                        if (hobby.getName().equals(hobbyFromDB.getName())) {
                            realPerson.addHobby(hobbyFromDB);
                        } else {
                            realPerson.addHobby(new Hobby(hobby.getName(), "No description"));
                        }
                    }
                }
            }
            CityInfo cityInfo = em.find(CityInfo.class, zipCode);
            if (cityInfo != null) {
                address.setCityInfo(cityInfo);
            } else {
                throw new ObjectNotFoundException(String.format("City with provided zip code: %s not found.", zipCode));
            }
            em.getTransaction().begin();
            em.persist(realPerson);
            em.getTransaction().commit();
            return new PersonDTO(realPerson);
        } finally {
            // The reason why it's in finally, is because want the code to still persist and continue.
            // In an ideal world, this would be handled separately, so it doesn't hide for other exceptions.
            if (failedNumbers.size() > 0) {
                String failedNumberResult = "";
                int count = 1;
                for (String failedNumber : failedNumbers) {
                    if(count < failedNumbers.size()) 
                    {
                        failedNumberResult += failedNumber + ", ";
                    } else 
                    {
                        failedNumberResult += failedNumber;
                    }
                    count++;
                }
                throw new DublicateException(failedNumbers.size() + " phone number(s) already in use. They were: " + failedNumberResult);
            }
            em.close();
        }
    }
        
    private List<Phone> createPhoneListFromJSON(String numbers, String description) {
        List<Phone> resultAllPhones = new ArrayList();
        
        String[] numbersSplit = numbers.split(",");
        String[] descSplit = {""};
        if(description != null) {
            descSplit = description.split(",");
        }
        for (int i = 0; i < numbersSplit.length; i++) {
            if (descSplit.length > i) {
                resultAllPhones.add(new Phone(numbersSplit[i].trim(), descSplit[i].trim()));
            } else {
                resultAllPhones.add(new Phone(numbersSplit[i].trim(), "No description"));
            }
        }
        
        return resultAllPhones;
    }
    
    private List<Hobby> createHobbyListFromJSON(String names, String description) {
        List<Hobby> resultAllHobbies = new ArrayList();
        
        String[] namesSplit = names.split(",");
        String[] descSplit = {""};
        if(description != null) {
            descSplit = description.split(",");
        }
        for (int i = 0; i < namesSplit.length; i++) {
            if (descSplit.length > i) {
                resultAllHobbies.add(new Hobby(namesSplit[i].trim().toLowerCase(), descSplit[i].trim()));
            } else {
                resultAllHobbies.add(new Hobby(namesSplit[i].trim().toLowerCase(), "No description"));
            }
        }
        
        return resultAllHobbies;
    }

    public PersonDTO editPerson(PersonDTO p) throws MissingInputException, ObjectNotFoundException {
        if ((p.getfName().isEmpty() || p.getfName() == null) || (p.getlName().isEmpty() || p.getlName() == null)) {
            throw new MissingInputException("Field \"fName\" AND \"lName\" is required.");
        }
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            Person person = em.find(Person.class,
                    p.getId());
            if (person == null) {
                throw new ObjectNotFoundException(String.format("Update failed: Person with provided id: %d doesn't exist", p.getId()));
            } else {
                person.setEmail(p.getEmail());
                person.setFirstName(p.getfName());
                person.setLastName(p.getlName());
                person.getAddress().setStreet(p.getStreet());
                person.getAddress().setAdditionalInfo(p.getAdditionalInfo());
                person.getAddress().getCityInfo().setCity(p.getCity());
                person.getAddress().getCityInfo().setZipCode(p.getZipCode());
                // Delete old phones.
                Query queryPhone = em.createQuery("DELETE FROM Phone ph WHERE ph.person = :person", Phone.class);
                queryPhone.setParameter("person", person);
                // Add new phone numbers.
                Set<Phone> phoneNumbers = new HashSet();
                PhonesDTO phoneNumbersDTO = p.getPhoneNumbers();
                for(PhoneDTO phoneDTO : phoneNumbersDTO.getAll()) {
                    phoneNumbers.add(new Phone(phoneDTO.getpNumbers(), phoneDTO.getpDescription()));
                }
                person.setPhonesNumbers(phoneNumbers);
                
                // Delete old hobbies.
                Query queryHobby = em.createQuery("DELETE FROM Person p WHERE p.hobbies = :hobbies", Person.class);
                queryHobby.setParameter("hobbies", person.getHobbies()).executeUpdate();
                // Add new hobbies.
                Set<Hobby> hobbies = new HashSet();
                HobbiesDTO hobbiesDTO = p.getHobbies();
                for(HobbyDTO hobbyDTO : hobbiesDTO.getAll()) {
                    hobbies.add(new Hobby(hobbyDTO.gethNames(), hobbyDTO.gethDescription()));
                }
                person.setHobbies(hobbies);
            }
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }

    }

    public PersonsDTO getPersonsByHobby(String hobbyName) throws ObjectNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        TypedQuery<Hobby> hobbyQuery = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobby", Hobby.class
        );
        hobbyQuery.setParameter("hobby", hobbyName);
        List<Hobby> hobbyList = hobbyQuery.getResultList();
        try {
            if (hobbyList != null || !hobbyList.isEmpty()) {
                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :hobbies", Person.class
                );
                query.setParameter("hobbies", hobbyName);
                List<Person> persons = query.getResultList();
                return new PersonsDTO(persons);
            } else {
                throw new ObjectNotFoundException(String.format("Hobby with provided name: %s not found.", hobbyName));
            }
        } finally {
            em.close();
        }
    }
    
    public PersonsDTO getPersonsByCity(String cityName) throws ObjectNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        TypedQuery<CityInfo> cityInfoQuery = em.createQuery("SELECT ci FROM CityInfo ci WHERE ci.city = :cityName", CityInfo.class);
        cityInfoQuery.setParameter("cityName", cityName);
        List<CityInfo> cityInfoList = cityInfoQuery.getResultList();
        try {
            if (cityInfoList != null) {
                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a WHERE a.cityInfo.city = :cityName", Person.class);
                query.setParameter("cityName", cityName);
                List<Person> persons = query.getResultList();
                return new PersonsDTO(persons);
            } else {
                throw new ObjectNotFoundException(String.format("City with provided name: %s not found.", cityName));
            }
        } finally {
            em.close();
        }
    }
}
