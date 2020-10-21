
package dto;

import entities.Address;
import entities.Phone;

public class PhoneDTO {
    private Long id;
    private String number;
    private String description;
    private String email;
    private String fName;
    private String lName; 
    private Address address;

    public PhoneDTO(Phone p) {
        this.id = p.getId();
        this.number = p.getNumber();
        this.description = p.getDescription();
        if(p.getPerson() != null) {
            this.email = p.getPerson().getEmail();
            this.fName = p.getPerson().getFirstName();
            this.lName = p.getPerson().getLastName();
            this.address = p.getPerson().getAddress();
        }
    }

    public PhoneDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    
    
    
    
}
