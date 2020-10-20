/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Danie
 */
@Entity
@NamedQueries({
@NamedQuery(name = "CityInfo.getAllRows", query = "SELECT ci FROM CityInfo ci")})
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Column(length = 4)
    private String zipCode;
    @Column(length=35)
    private String city;
    
    @OneToMany(mappedBy = "ci", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Set<Address> adresses = new HashSet();

    public CityInfo(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public CityInfo() {
    }    

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }
    
    public Set<Address> getAddresses() {
        return adresses;
    }
    
    public void addAddress(Address address) {
        adresses.add(address);
    }
    
    
    
    
}
