package dto;

import entities.Hobby;
import entities.Person;
import java.util.List;

public class PersonDTO {

    private Long id;
    private String fName;
    private String lName;
    private String phone;
    private String email;
    private String street;
    private String zipCode;
    private String city;
    private String hobbies;
    
    public PersonDTO() {
    }

    public PersonDTO(Person p) {
        this.fName = p.getFirstName();
        this.lName = p.getLastName();
        this.phone = p.getPhone();
        this.email = p.getEmail();
        this.id = p.getId();
        if (p.getAddress() != null) {
            this.street = p.getAddress().getStreet();
            this.zipCode = p.getAddress().getZipCode();
            this.city = p.getAddress().getCity();
        }
        if (p.getHobbies()!= null) {
            this.hobbies = p.getHobbies().getHobby();
    }
    }

    public String getHobbies() {
        return hobbies;
    }
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
    
    //Lav equals metode hvis du skal lave test sammenligning.
}
