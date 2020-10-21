
package dto;

import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhonesDTO {
    
    Set<PhoneDTO> all = new HashSet();
    
    public PhonesDTO(Set<Phone> phoneEntities) {
        phoneEntities.forEach((p) -> {
            all.add(new PhoneDTO(p));
        });
    }

    public Set<PhoneDTO> getAll() {
        return all;
    }

    public void setAll(Set<PhoneDTO> all) {
        this.all = all;
    }
    
}
