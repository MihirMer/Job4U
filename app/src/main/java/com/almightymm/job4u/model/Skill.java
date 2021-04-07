package com.almightymm.job4u.model;

import java.util.List;

public class Skill {
    private String id;
    List skills;

    public Skill() {
    }

    public Skill(String id, List skills) {
        this.id = id;
        this.skills = skills;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getSkills() {
        return skills;
    }

    public void setSkills(List skills) {
        this.skills = skills;
    }
}
