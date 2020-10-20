/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.CityInfo;

/**
 *
 * @author Danie
 */
public class CityInfoDTO {
    
    private String zipCode;
    private String city;

    public CityInfoDTO(CityInfo ci) {
        this.zipCode = ci.getZipCode();
        this.city = ci.getCity();
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }
    
    
    
}
