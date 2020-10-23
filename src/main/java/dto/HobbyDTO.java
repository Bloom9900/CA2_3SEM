
package dto;

import entities.Hobby;

public class HobbyDTO {
    
    private String hNames;
    private String hDescription;

    public HobbyDTO(Hobby hobby) {
        this.hNames = hobby.getName();
        this.hDescription = hobby.getDescription();
    }
    
    

    public String gethNames() {
        return hNames;
    }

    public void sethNames(String hNames) {
        this.hNames = hNames;
    }

    public String gethDescription() {
        return hDescription;
    }

    public void sethDescription(String hDescription) {
        this.hDescription = hDescription;
    }
    
    
    
}
