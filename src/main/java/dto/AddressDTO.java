package dto;

import entities.Address;

public class AddressDTO {
    private Long id;
    private String street;
    private String additionalInfo;
    private String zipCode;
    private String city;

    public AddressDTO(Address a) {
        this.street = a.getStreet();
        this.additionalInfo = a.getAdditionalInfo();
        if(a.getCityInfo() != null) {
            this.zipCode = a.getCityInfo().getZipCode();
            this.city = a.getCityInfo().getCity();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
