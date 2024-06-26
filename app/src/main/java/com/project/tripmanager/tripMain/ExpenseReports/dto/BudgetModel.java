package com.project.tripmanager.tripMain.ExpenseReports.dto;

public class BudgetModel {
    private long original,currentAmt;

    public BudgetModel() {
    }

    public long getOriginal() {
        return original;
    }

    public void setOriginal(long original) {
        this.original = original;
    }

    public long getCurrentAmt() {
        return currentAmt;
    }

    public void setCurrentAmt(long currentAmt) {
        this.currentAmt = currentAmt;
    }
}
