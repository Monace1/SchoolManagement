package com.SchoolManagement.StudentService.model;
import jakarta.persistence.Embeddable;

import java.util.Map;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String country;
    private String postalCode;

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public Map<Object, Object> trim() {
        return trim();
    }
}
