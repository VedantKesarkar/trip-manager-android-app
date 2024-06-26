package com.project.tripmanager.tripMain.Packing.dto;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class CategorySelected {
    @Exclude
    private String id;
    private ArrayList<String> categories;
    private String title;
    private String createdBy;

    public CategorySelected() {}

    public CategorySelected(ArrayList<String> categories, String title, String createdBy) {
        this.categories = categories;
        this.title = title;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
