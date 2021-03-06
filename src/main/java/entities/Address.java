package entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE FROM Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;
    @Column(name = "street")
    private String Street;
    @Column(name = "additional_info")
    private String additionalInfo;
    
    @OneToMany(mappedBy = "address", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Person> persons = new HashSet();
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "zip_code")
    private CityInfo cityInfo;

    public Address() {
    }

    public Address(String Street) {
        this.Street = Street;
    }

    public Address(String Street, String additionalInfo) {
        this.Street = Street;
        this.additionalInfo = additionalInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String Street) {
        this.Street = Street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }
    
    public void addPerson(Person p) {
        this.persons.add(p);
    }
    
    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }
}
