package com.project.tripmanager.tripMain.Expenses.dto;

import com.google.firebase.firestore.Exclude;

public class ExpenseModel {
    private String name;
    private long amt;

    @Exclude
    private String id;

    public ExpenseModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmt() {
        return amt;
    }

    public void setAmt(long amt) {
        this.amt = amt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
