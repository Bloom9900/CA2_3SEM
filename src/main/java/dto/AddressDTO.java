/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Address;
import entities.CityInfo;

/**
 *
 * @author Danie
 */
public class AddressDTO {
    
    private Long id;
    private String street;
    private String additionalInfo;
    private CityInfo ci;

    public AddressDTO() {
    }
    
    public AddressDTO(Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.ci = address.getCityInfo();
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public CityInfo getCi() {
        return ci;
    }
    
    
    
    
}
