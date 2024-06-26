package com.project.tripmanager.tripMain.Expenses.dto;

import java.util.ArrayList;

public class ExpenditureModel {
    private String  currentDate;
    private long allTimeExpense,daily, days;
    private ArrayList<MemberExpenseModel> allMembersExpenses = new ArrayList<>();

    public ExpenditureModel() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public long getAllTimeExpense() {
        return allTimeExpense;
    }

    public void setAllTimeExpense(long allTimeExpense) {
        this.allTimeExpense = allTimeExpense;
    }

    public long getDaily() {
        return daily;
    }

    public void setDaily(long daily) {
        this.daily = daily;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public ArrayList<MemberExpenseModel> getAllMembersExpenses() {
        return allMembersExpenses;
    }

    public void setAllMembersExpenses(ArrayList<MemberExpenseModel> allMembersExpenses) {
        this.allMembersExpenses = allMembersExpenses;
    }
}
