package com.project.tripmanager.tripMain.Expenses.domain;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.project.tripmanager.tripMain.Expenses.repo.ExpenseTypeRepo;

public class ExpenseTypeViewModel extends ViewModel {
    private final ExpenseTypeRepo expenseTypeRepo = new ExpenseTypeRepo();

    public void createExpenseType(String typeName, String date)
    {
        expenseTypeRepo.createExpenseType(typeName,date);
    }

    public void setGroupCode()
    {
        expenseTypeRepo.setGroupCode();
    }

    public LiveData<String>  getGroupCode()
    {
        return expenseTypeRepo.getCode();
    }

}
