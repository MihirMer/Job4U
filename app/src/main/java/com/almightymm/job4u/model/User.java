package com.almightymm.job4u.model;

public class User {
    private String firstName, lastName, emailAddress, role;
    private boolean roleAssigned;

    public User() {
    }

    public User(String firstName, String lastName, String emailAddress, boolean roleAssigned, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.roleAssigned = roleAssigned;
        this.role = role;
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
