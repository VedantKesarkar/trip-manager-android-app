package com.project.tripmanager.tripMain.Expenses.repo;

import static com.project.tripmanager.Core.USERS;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseTypeModel;

import java.util.Objects;

public class ExpenseTypeRepo {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ExpenseTypeRepo";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final String EXPENSE_LIST = "Expense List";


    private String email,groupCode;
    private final MutableLiveData<String> code = new MutableLiveData<>();


    public void createExpenseType(String typeName, String date)
    {
        if(mAuth.getCurrentUser()!=null && groupCode!=null)
        {
            assert email != null;
            DocumentReference docRef = db.collection(groupCode).document(email).collection(EXPENSE_LIST).document();
            ExpenseTypeModel expenseTypeModel = new ExpenseTypeModel();
            expenseTypeModel.setId(docRef.getId());
            expenseTypeModel.setTypeName(typeName);
            expenseTypeModel.setCreationDate(date);
            expenseTypeModel.setLastDate(date);
            expenseTypeModel.setTotalExpense(0L);
            expenseTypeModel.setDaily(0L);
            docRef.set(expenseTypeModel).addOnSuccessListener(res -> Log.d(TAG, "Expense type created "));
        }

    }

    public void setGroupCode() {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                Prof prof = documentSnapshot.toObject(Prof.class);
                assert prof != null;
                groupCode = prof.getGroupCode();
                code.setValue(groupCode);
            }
        });
    }

    public LiveData<String> getCode() {
        return code;
    }
}
