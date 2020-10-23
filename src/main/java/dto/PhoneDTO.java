
package dto;

import entities.Address;
import entities.Phone;

public class PhoneDTO {
    private String pNumbers;
    private String pDescription;

    public PhoneDTO(Phone p) { 
        this.pNumbers = p.getNumber();
        this.pDescription = p.getDescription();
    }

    public PhoneDTO() {
    }

    public String getpNumbers() {
        return pNumbers;
    }

    public void setpNumbers(String pNumbers) {
        this.pNumbers = pNumbers;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }
    
    
    
    
    
}
