package com.almightymm.job4u.model;

public class CompanyDetails {
    private String companyName, about, location, phone, website;

    public CompanyDetails() {

    }

    public CompanyDetails(String companyName, String about, String location, String phone, String website) {
        this.companyName = companyName;
        this.about = about;
        this.location = location;
        this.phone = phone;
        this.website = website;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
