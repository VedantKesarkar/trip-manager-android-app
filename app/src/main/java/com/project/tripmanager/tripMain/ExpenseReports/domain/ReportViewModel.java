package com.project.tripmanager.tripMain.ExpenseReports.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.ExpenseReports.repo.ReportsRepo;

public class ReportViewModel extends ViewModel {
    private static final String TAG = "ReportViewModel";
    private final ReportsRepo reportsRepo = new ReportsRepo();

    public void setGroupCode()
    {
        reportsRepo.setGroupCode();
    }

    public void setMyReport()
    {
        reportsRepo.getMemberReport();
    }

    public LiveData<String[]> getAllMembers() {
        return reportsRepo.getAllMembers();
    }

    public LiveData<String[]> getExpenseTypes() {
        return reportsRepo.getExpenseTypes();
    }

    public LiveData<Integer[]> getAllMemberTotalExpenses() {
        return reportsRepo.getAllMemberTotalExpenses();
    }

    public LiveData<Integer[]> getAllMemberDailyExpenses() {
        return reportsRepo.getAllMemberDailyExpenses();
    }

    public LiveData<Integer[]> getMemberTotalTypeExpenses() {
        return reportsRepo.getMemberTotalTypeExpenses();
    }

    public LiveData<Integer[]> getMemberDailyTypeExpenses() {
        return reportsRepo.getMemberDailyTypeExpenses();
    }

    public LiveData<Integer> getTotalTeamExpense() {
        return reportsRepo.getTotalTeamExpense();
    }

    public LiveData<Integer> getDailyTeamExpense() {
        return reportsRepo.getDailyTeamExpense();
    }

    public LiveData<Integer> getAvgTeamExpense() {
        return reportsRepo.getAvgTeamExpense();
    }

    public LiveData<Integer> getTotalMemberExpense() {
        return reportsRepo.getTotalMemberExpense();
    }

    public LiveData<Integer> getDailyMemberExpense() {
        return reportsRepo.getDailyMemberExpense();
    }
}
