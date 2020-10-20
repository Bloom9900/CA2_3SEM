package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String additionalInfo;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    private CityInfo ci;
    
    @OneToMany(mappedBy="address", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Person> persons;

    public Address(String street, String additionalInfo, CityInfo ci) {
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.ci = ci;
    }

    public Address() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addPerson(Person person) {
        if(person != null) {
            persons.add(person);
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public CityInfo getCityInfo() {
        return ci;
    }

    public void setCityInfo(CityInfo ci) {
        this.ci = ci;
    }
    
    
    
    
}
