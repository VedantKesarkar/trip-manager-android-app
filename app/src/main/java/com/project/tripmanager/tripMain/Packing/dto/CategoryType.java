package com.project.tripmanager.tripMain.Packing.dto;

import java.util.ArrayList;

public class CategoryType {

    private String title;
    private ArrayList<CategorySubType> categoryList = new ArrayList<>();

    public CategoryType(String title, ArrayList<CategorySubType> categoryList) {
        this.title = title;
        this.categoryList = categoryList;
    }

    public String getTitle() {
        return title;
    }


    public ArrayList<CategorySubType> getCategoryList() {
        return categoryList;
    }


}
