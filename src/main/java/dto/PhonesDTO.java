
package dto;

import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PhonesDTO {
    
    List<PhoneDTO> all = new ArrayList();
    
    public PhonesDTO(List<Phone> phoneEntities) {
        phoneEntities.forEach((p) -> {
            all.add(new PhoneDTO(p));
        });
    }

    public List<PhoneDTO> getAll() {
        return all;
    }

    public void setAll(List<PhoneDTO> all) {
        this.all = all;
    }
    
}
