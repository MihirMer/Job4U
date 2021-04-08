package com.almightymm.job4u.model;

public class ProjectWork {
    private String id, projectName, description, startYear, endYear;

    public ProjectWork() {
    }

    public ProjectWork(String id, String projectName, String description, String startYear, String endYear) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
}
