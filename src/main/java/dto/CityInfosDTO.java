package dto;

import entities.CityInfo;
import java.util.HashSet;
import java.util.Set;

public class CityInfosDTO {
    
    Set<CityInfoDTO> all = new HashSet();

    public CityInfosDTO() {
    }
    
    public CityInfosDTO(Set<CityInfo> ciEntities) {
        ciEntities.forEach((ci) -> {
            all.add(new CityInfoDTO(ci));
        });
    }

    public Set<CityInfoDTO> getAll() {
        return all;
    }

    public void setAll(Set<CityInfoDTO> all) {
        this.all = all;
    }
    
}
