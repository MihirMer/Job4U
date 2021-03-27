package com.almightymm.job4u.model;

public class Category {
    private int categoryIcon;
    private String categoryTitle;

    public Category() {
    }

    public Category(int categoryIcon, String categoryTitle) {
        this.categoryIcon = categoryIcon;
        this.categoryTitle = categoryTitle;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
