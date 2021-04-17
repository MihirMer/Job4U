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
    private boolean roleAssigned ,areaOfInterestSelected ,personalDetailsAdded ,keySkillAdded ,educationAdded ,projectWorkAdded ,experienceAdded, companyDetailsAdded;

    public PersonalDetails() {
    }

    public PersonalDetails(String firstName, String lastName, String emailAddress, String phone, String DOB, String address, String gender, String role, boolean roleAssigned, boolean areaOfInterestSelected, boolean personalDetailsAdded, boolean keySkillAdded, boolean educationAdded, boolean projectWorkAdded, boolean experienceAdded, boolean companyDetailsAdded) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.DOB = DOB;
        this.address = address;
        this.gender = gender;
        this.role = role;
        this.roleAssigned = roleAssigned;
        this.areaOfInterestSelected = areaOfInterestSelected;
        this.personalDetailsAdded = personalDetailsAdded;
        this.keySkillAdded = keySkillAdded;
        this.educationAdded = educationAdded;
        this.projectWorkAdded = projectWorkAdded;
        this.experienceAdded = experienceAdded;
        this.companyDetailsAdded = companyDetailsAdded;
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

    public boolean isAreaOfInterestSelected() {
        return areaOfInterestSelected;
    }

    public void setAreaOfInterestSelected(boolean areaOfInterestSelected) {
        this.areaOfInterestSelected = areaOfInterestSelected;
    }

    public boolean isPersonalDetailsAdded() {
        return personalDetailsAdded;
    }

    public void setPersonalDetailsAdded(boolean personalDetailsAdded) {
        this.personalDetailsAdded = personalDetailsAdded;
    }

    public boolean isKeySkillAdded() {
        return keySkillAdded;
    }

    public void setKeySkillAdded(boolean keySkillAdded) {
        this.keySkillAdded = keySkillAdded;
    }

    public boolean isEducationAdded() {
        return educationAdded;
    }

    public void setEducationAdded(boolean educationAdded) {
        this.educationAdded = educationAdded;
    }

    public boolean isProjectWorkAdded() {
        return projectWorkAdded;
    }

    public void setProjectWorkAdded(boolean projectWorkAdded) {
        this.projectWorkAdded = projectWorkAdded;
    }

    public boolean isExperienceAdded() {
        return experienceAdded;
    }

    public void setExperienceAdded(boolean experienceAdded) {
        this.experienceAdded = experienceAdded;
    }

    public boolean isCompanyDetailsAdded() {
        return companyDetailsAdded;
    }

    public void setCompanyDetailsAdded(boolean companyDetailsAdded) {
        this.companyDetailsAdded = companyDetailsAdded;
    }
}