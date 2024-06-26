package com.project.tripmanager.tripMain.ExpenseReports.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.ExpenseReports.dto.BudgetInfoModel;
import com.project.tripmanager.tripMain.ExpenseReports.repo.BudgetInfoRepo;

public class BudgetViewModel extends ViewModel {

    private final BudgetInfoRepo budgetInfoRepo = new BudgetInfoRepo();

    public LiveData<BudgetInfoModel> getBudgetInfo()
    {
        return budgetInfoRepo.getInfo();
    }

    public void checkAdmin()
    {
        budgetInfoRepo.checkAdmin();
    }

    public void addToBudget(String amt)
    {
        budgetInfoRepo.addToBudget(amt);
    }

    public void setBudgetInfo()
    {
        budgetInfoRepo.getTheGroupCode();
    }

    public LiveData<Boolean> getAdmin()
    {
        return budgetInfoRepo.getAdmin();
    }
}
