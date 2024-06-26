package com.project.tripmanager.tripMain.ExpenseReports.repo;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.Core.USERS;
import static com.project.tripmanager.newTrip.domain.CreateOrJoinGroup.BUDGET;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.newTrip.dto.MetaData;
import com.project.tripmanager.tripMain.ExpenseReports.dto.BudgetInfoModel;
import com.project.tripmanager.tripMain.ExpenseReports.dto.BudgetModel;

import java.util.Objects;

public class BudgetInfoRepo {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "BudgetInfoRepo";
    public static final String WARN = " Budget almost exhausted!";
    public static final String BROKE = " You have exhausted your budget!";
    private String groupCode,name,email;
    private final BudgetInfoModel infoModel = new BudgetInfoModel();
    private final MutableLiveData<BudgetInfoModel> info = new MutableLiveData<>();
    private final MutableLiveData<Boolean> Admin = new MutableLiveData<>();


    public void getTheGroupCode()
    {
         email  = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                Prof prof = documentSnapshot.toObject(Prof.class);
                assert prof != null;
                groupCode = prof.getGroupCode();
                getBudgetInfo();
            }
        });
    }
    public void getBudgetInfo()
    {
        db.collection(groupCode).document(BUDGET).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BudgetModel budgetModel = documentSnapshot.toObject(BudgetModel.class);
                assert budgetModel != null;
                long current = budgetModel.getCurrentAmt();
                long original  = budgetModel.getOriginal();
                double result = ((double) current / (double) original) * 100;
                long percent = (long) result;
                infoModel.setSpent((int)current);
                infoModel.setOriginal((int)original);
                infoModel.setLeft((int) (original - current));
                long limit = (original * 20) / 100;

                Log.d(TAG, "percent "+percent);
                infoModel.setPercent(String.valueOf(percent));
                if(current>=original-limit)
                {
                    infoModel.setLimit(true);
                    if(current>=original)
                    {
                        infoModel.setWarningMsg(BROKE);
                        infoModel.setPercent("100");

                    }
                    else {
                        infoModel.setWarningMsg(WARN);

                    }
                }
                else {
                    infoModel.setLimit(false);
                    infoModel.setPercent(String.valueOf(percent));
                }
                Log.d(TAG, "Info obtained");
                info.setValue(infoModel);
            }
        });
    }

    public void checkAdmin()
    {
        assert email != null;
        db.collection(groupCode).document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                db.collection(groupCode).document(METADATA).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MetaData metaData = documentSnapshot.toObject(MetaData.class);
                        assert metaData != null;
                        if(name.equals(metaData.getAdmin()))
                        {
                            Admin.setValue(true);
                        }
                        else {
                            Admin.setValue(false);
                        }
                    }
                });
            }
        });

    }

    public void addToBudget(String amt)
    {
        db.collection(groupCode).document(BUDGET).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BudgetModel budgetModel = documentSnapshot.toObject(BudgetModel.class);
                assert budgetModel != null;
                budgetModel.setOriginal(budgetModel.getOriginal() + Long.parseLong(amt));
                updateBudget(budgetModel);
            }
        });
    }

    private void updateBudget(BudgetModel budgetModel) {
        db.collection(groupCode).document(BUDGET).set(budgetModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Budget updated!");
            }
        });
    }

    public LiveData<BudgetInfoModel> getInfo() {
        return info;
    }

    public LiveData<Boolean> getAdmin() {
        return Admin;
    }
}
