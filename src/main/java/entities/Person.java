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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Danie
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;
    
    @ManyToMany(mappedBy = "persons", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hobby> hobbies = new HashSet();
    
    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Phone> phoneNumbers = new HashSet();

    public Person() {
    }

    public Person(String email, String firstName, String lastName, Address address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public Set<Phone> getPhoneNumbers() {
        return phoneNumbers;
    }
    
    public void addAddress(Address address) {
        if(address != null) {
            this.address = address;
            address.addPerson(this);
        } else {
            this.address = null;
        }
    }
    
    public void addPhone(String number, String description) {
        Phone phone = new Phone(number, description);
        phone.setPerson(this);
        this.phoneNumbers.add(phone);
    }
    
    public void addHobby(String name, String description) {
        Hobby hobby = new Hobby(name, description);
        hobby.addPerson(this);
        this.hobbies.add(hobby);
    }
    
    

    
}
