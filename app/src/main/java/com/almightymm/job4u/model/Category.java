package com.almightymm.job4u.model;

public class Category {
    private int c_Image;
    private String c_Job_name;

    public Category() {
    }

    public Category(int c_Image, String c_Job_name) {
        this.c_Image = c_Image;
        this.c_Job_name = c_Job_name;
    }

    public int getC_Image() {
        return c_Image;
    }

    public void setC_Image(int c_Image) {
        this.c_Image = c_Image;
    }

    public String getC_Job_name() {
        return c_Job_name;
    }

    public void setC_Job_name(String c_Job_name) {
        this.c_Job_name = c_Job_name;
    }
}
