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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo"),
@NamedQuery(name = "CityInfo.getAllRows", query = "SELECT ci FROM CityInfo ci")})
public class CityInfo implements Serializable {

    @Id
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "city")
    private String city;
    
    @OneToMany(mappedBy = "cityInfo", cascade = {CascadeType.MERGE})
    private Set<Address> addresses = new HashSet();

    public CityInfo() {
    }

    public CityInfo(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }
    
    public void setAddress(String street, String additionalInfo) {
        Address a = new Address(street, additionalInfo);
        a.setCityInfo(this);
        this.addresses.add(a);
    }
}
