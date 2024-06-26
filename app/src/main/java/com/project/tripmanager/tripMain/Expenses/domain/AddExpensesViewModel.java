package com.project.tripmanager.tripMain.Expenses.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.Expenses.dto.ExpensePathRef;
import com.project.tripmanager.tripMain.Expenses.repo.AddExpensesRepo;

public class AddExpensesViewModel extends ViewModel {

    private final AddExpensesRepo addExpensesRepo = new AddExpensesRepo();
    public void setGroupCode(String code)
    {
        addExpensesRepo.setGroupCode(code);
    }

    public void setTypeId(String id)
    {
        addExpensesRepo.setTypeId(id);
    }

    public void addExpense(String name,Long amt)
    {
        addExpensesRepo.addExpenses(name, amt);
    }

    public LiveData<ExpensePathRef> getPathRef()
    {
       return  addExpensesRepo.getPathRef();
    }



}
