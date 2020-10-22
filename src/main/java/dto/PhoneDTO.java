
package dto;

import entities.Address;
import entities.Phone;

public class PhoneDTO {
    private String number;
    private String description;

    public PhoneDTO(Phone p) { 
        this.number = p.getNumber();
        this.description = p.getDescription();
    }

    public PhoneDTO() {
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
    
    
    
    
    
}
