package com.pafex.zscs;

public class Pincode {

    public String branch_name;
    public String tdd;
    public String pincode;
    public String city;
    public String state;
    public String district_name;
    public String country;

    public Pincode(String branch_name, String pincode, String city, String state, String country, String district_name, String tdd)
    {
        this.branch_name = branch_name;
        this.tdd = tdd;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.district_name = district_name;
        this.country = country;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getTdd() {
        return tdd;
    }

    public void setTdd(String tdd) {
        this.tdd = tdd;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
