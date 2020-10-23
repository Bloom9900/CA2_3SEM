/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.CityInfo;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Danie
 */
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
