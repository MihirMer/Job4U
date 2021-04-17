package com.almightymm.job4u.model;

public class EducationDetails {
    private String id, degreeName, collegeName, stream, admissionYear, year, gpa;

    public EducationDetails() {
    }

    public EducationDetails(String id, String degreeName, String collegeName, String stream, String admissionYear, String year, String gpa) {
        this.id = id;
        this.degreeName = degreeName;
        this.collegeName = collegeName;
        this.stream = stream;
        this.admissionYear = admissionYear;
        this.year = year;
        this.gpa = gpa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(String admissionYear) {
        this.admissionYear = admissionYear;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }
}
