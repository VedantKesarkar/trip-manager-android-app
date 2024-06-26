package com.project.tripmanager.tripMain.Packing.dto;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class GlobalCatCombo {
    @Exclude
    private String id;

    private ArrayList<String> catCombo  = new ArrayList<>();

    public ArrayList<String> getCatCombo() {
        return catCombo;
    }

    public GlobalCatCombo(ArrayList<String> catCombo) {
        this.catCombo = catCombo;
    }

    public void setCatCombo(ArrayList<String> catCombo) {
        this.catCombo = catCombo;
    }

    public String getId() {
        return id;
    }

    public GlobalCatCombo() {
    }

    public void setId(String id) {
        this.id = id;
    }
}
