package com.almightymm.job4u.model;

public class PersonalDetails {

    String firstName;
    String lastName;
    String emailAddress;
    String phone;
    String DOB;
    String address;
    String gender;
    private String role;
    private boolean roleAssigned;

    public PersonalDetails() {
    }

    public PersonalDetails(String firstName, String lastName, String emailAddress, String phone, String DOB, String address, String gender, String role, boolean roleAssigned) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.DOB = DOB;
        this.address = address;
        this.gender = gender;
        this.role = role;
        this.roleAssigned = roleAssigned;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isRoleAssigned() {
        return roleAssigned;
    }

    public void setRoleAssigned(boolean roleAssigned) {
        this.roleAssigned = roleAssigned;
    }
}