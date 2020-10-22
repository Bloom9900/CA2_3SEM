package dto;

import entities.Person;
import entities.Phone;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PersonDTO {

    private Long id;
    private String fName;
    private String lName;
    private Set<Phone> phoneNumbers = new HashSet();
    private String email;
    private String street;
    private String city;
    private String zipCode;
    private String additionalInfo;

    public PersonDTO(Person p) {
        this.fName = p.getFirstName();
        this.lName = p.getLastName();
        if(this.phoneNumbers != null) {
            this.phoneNumbers = p.getPhoneNumbers();
          
        }
        this.email = p.getEmail();
        this.id = p.getId();
        if (p.getAddress() != null) {
            this.street = p.getAddress().getStreet();
            if(p.getAddress().getCityInfo() != null) {
                this.city = p.getAddress().getCityInfo().getCity();
                this.zipCode = p.getAddress().getCityInfo().getZipCode();
            }
        }
        
    }
    
    public PersonDTO() {
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

    public Set<Phone> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<Phone> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    /*
    public void addPhone(String numbers, String description) {
        Phone number = new Phone(numbers, description);
        number.setPerson(this);
        this.phoneNumbers.add(number);
    }
*/
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonDTO other = (PersonDTO) obj;
        if (!Objects.equals(this.fName, other.fName)) {
            return false;
        }
        if (!Objects.equals(this.lName, other.lName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}