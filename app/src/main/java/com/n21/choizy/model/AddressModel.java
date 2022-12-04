package com.n21.choizy.model;

public class AddressModel {

    String houseNo,streetAddress,city;

    public AddressModel(String houseNo, String streetAddress, String city) {
        this.houseNo = houseNo;
        this.streetAddress = streetAddress;
        this.city = city;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
