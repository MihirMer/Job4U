package com.almightymm.job4u.model;

public class WorkExperience {
    private String id, designation, yearsOfExperience, fromYear, toYear, companyName, city;

    public WorkExperience() {
    }

    public WorkExperience(String id, String designation, String yearsOfExperience, String fromYear, String toYear, String companyName, String city) {
        this.id = id;
        this.designation = designation;
        this.yearsOfExperience = yearsOfExperience;
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.companyName = companyName;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
