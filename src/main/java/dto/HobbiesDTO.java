package dto;

import entities.Hobby;
import entities.Phone;
import java.util.HashSet;
import java.util.Set;

public class HobbiesDTO {

    Set<HobbyDTO> all = new HashSet();

    public HobbiesDTO() {
    }

    public HobbiesDTO(Set<Hobby> hobbyEntities) {
        hobbyEntities.forEach((h) -> {
            all.add(new HobbyDTO(h));
        });
    }

    public Set<HobbyDTO> getAll() {
        return all;
    }

    public void setAll(Set<HobbyDTO> all) {
        this.all = all;
    }

}
