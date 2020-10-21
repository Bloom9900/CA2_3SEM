
package dto;

import entities.Phone;

public class PhoneDTO {
    private Long id;
    private String number;
    private String description;

    public PhoneDTO(Phone phone) {
        this.id = id;
        this.number = number;
        this.description = description;
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
    
    
    
    
    
}
