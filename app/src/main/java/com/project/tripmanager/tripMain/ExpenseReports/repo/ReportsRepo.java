package com.project.tripmanager.tripMain.ExpenseReports.repo;

import static com.project.tripmanager.Core.USERS;
import static com.project.tripmanager.tripMain.Expenses.repo.AddExpensesRepo.EXPENDITURE;
import static com.project.tripmanager.tripMain.Expenses.repo.ExpenseTypeRepo.EXPENSE_LIST;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenditureModel;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseTypeModel;
import com.project.tripmanager.tripMain.Expenses.dto.MemberExpenseModel;

import java.util.ArrayList;
import java.util.Objects;

public class ReportsRepo {
    private static final String TAG = "ReportsRepo";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String groupCode,email;
    private  ExpenditureModel expenditureModel;

    MutableLiveData<String[]> allMembers = new MutableLiveData<>();
    MutableLiveData<String[]> expenseTypes = new MutableLiveData<>();
    MutableLiveData<Integer[]> allMemberTotalExpenses = new MutableLiveData<>();
    MutableLiveData<Integer[]> allMemberDailyExpenses = new MutableLiveData<>();

    MutableLiveData<Integer[]> memberTotalTypeExpenses = new MutableLiveData<>();
    MutableLiveData<Integer[]> memberDailyTypeExpenses = new MutableLiveData<>();

    MutableLiveData<Integer> totalTeamExpense = new MutableLiveData<>();
    MutableLiveData<Integer> dailyTeamExpense = new MutableLiveData<>();
    MutableLiveData<Integer> avgTeamExpense = new MutableLiveData<>();

    MutableLiveData<Integer> totalMemberExpense = new MutableLiveData<>();
    MutableLiveData<Integer> dailyMemberExpense = new MutableLiveData<>();

    public ReportsRepo() {
    }

    public void setGroupCode()
    {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                Prof prof = documentSnapshot.toObject(Prof.class);
                assert prof != null;
                groupCode = prof.getGroupCode();
                getTeamReport();
                getMemberReport();
            }
        });
    }

    public void getTeamReport()
    {

        db.collection(groupCode).document(EXPENDITURE).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                expenditureModel = documentSnapshot.toObject(ExpenditureModel.class);
                assert expenditureModel != null;
                totalTeamExpense.setValue((int) expenditureModel.getAllTimeExpense());
                dailyTeamExpense.setValue((int) expenditureModel.getDaily());
                avgTeamExpense.setValue((int) (expenditureModel.getAllTimeExpense()/expenditureModel.getDays()));
                populateTeamGraphs();
            }
        });
    }

    private void populateTeamGraphs() {
        ArrayList<String> members = new ArrayList<>();
        ArrayList<Integer> memberTotal = new ArrayList<>();
        ArrayList<Integer> memberDaily = new ArrayList<>();
        ArrayList<MemberExpenseModel> expenseData = expenditureModel.getAllMembersExpenses();

        for (int i = 0; i <expenseData.size() ; i++) {
            members.add(expenseData.get(i).getMemberName());
            memberTotal.add((int) expenseData.get(i).getTotal());
            memberDaily.add((int) expenseData.get(i).getDaily());
            if (expenseData.get(i).getEmail().equals(email))
            {
                totalMemberExpense.setValue((int) expenseData.get(i).getTotal());
                dailyMemberExpense.setValue((int) expenseData.get(i).getDaily());
            }
        }
        allMembers.setValue(members.toArray(new String[0]));
        allMemberDailyExpenses.setValue(memberDaily.toArray(new Integer[0]));
        allMemberTotalExpenses.setValue(memberTotal.toArray(new Integer[0]));

    }


    public void getMemberReport()
    {
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Integer> eTotalType = new ArrayList<>();
        ArrayList<Integer> eDailyType = new ArrayList<>();
        db.collection(groupCode).document(email).collection(EXPENSE_LIST).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot docSnapShot:queryDocumentSnapshots.getDocuments()) {
                ExpenseTypeModel expenseTypeModel = docSnapShot.toObject(ExpenseTypeModel.class);
                assert expenseTypeModel != null;

                types.add(expenseTypeModel.getTypeName());
                eTotalType.add((int)expenseTypeModel.getTotalExpense());
                eDailyType.add((int)expenseTypeModel.getDaily());
            }

            expenseTypes.setValue(types.toArray(new String[0]));
            memberTotalTypeExpenses.setValue(eTotalType.toArray(new Integer[0]));
            memberDailyTypeExpenses.setValue(eDailyType.toArray(new Integer[0]));
        });

    }

    public LiveData<String[]> getAllMembers() {
        return allMembers;
    }

    public LiveData<String[]> getExpenseTypes() {
        return expenseTypes;
    }

    public LiveData<Integer[]> getAllMemberTotalExpenses() {
        return allMemberTotalExpenses;
    }

    public LiveData<Integer[]> getAllMemberDailyExpenses() {
        return allMemberDailyExpenses;
    }

    public LiveData<Integer[]> getMemberTotalTypeExpenses() {
        return memberTotalTypeExpenses;
    }

    public LiveData<Integer[]> getMemberDailyTypeExpenses() {
        return memberDailyTypeExpenses;
    }

    public LiveData<Integer> getTotalTeamExpense() {
        return totalTeamExpense;
    }

    public LiveData<Integer> getDailyTeamExpense() {
        return dailyTeamExpense;
    }

    public LiveData<Integer> getAvgTeamExpense() {
        return avgTeamExpense;
    }

    public LiveData<Integer> getTotalMemberExpense() {
        return totalMemberExpense;
    }

    public LiveData<Integer> getDailyMemberExpense() {
        return dailyMemberExpense;
    }
}