package com.project.tripmanager.newTrip.domain;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.Core.USERS;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.Core;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.newTrip.dto.MetaData;
import com.project.tripmanager.newTrip.dto.User;
import com.project.tripmanager.tripMain.ExpenseReports.dto.BudgetModel;

import java.util.Random;

public class CreateOrJoinGroup {
    private  String email, name;
    private  String tripName;
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();

    private int groupCode;
    private long budget;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String BUDGET = "Budget";
    private final Core core = new Core();

    public CreateOrJoinGroup() {
    }

    public void createAction(String email, String name, String tripName,String amt) {
        this.email = email;
        this.name = name;
        this.tripName = tripName;
        this.budget = Long.parseLong(amt);
    }

    public void joinAction(String email, String name, int groupCode) {
        this.email = email;
        this.name = name;
        this.groupCode = groupCode;
    }

    public void createRoom()
    {

        groupCode = new Random().nextInt(1000)+4321;
        core.setGroupCode(String.valueOf(groupCode));

        MetaData meta = new MetaData(name,tripName);
        User user = new User(name);
        BudgetModel budgetModel = new BudgetModel();
        budgetModel.setCurrentAmt(0L);
        budgetModel.setOriginal(budget);

        db.collection(String.valueOf(groupCode)).document(email).set(user)
                .addOnSuccessListener(unused -> success.setValue(true)).addOnFailureListener(e -> success.setValue(true));
        setProfile();
        db.collection(String.valueOf(groupCode)).document(METADATA).set(meta);

        db.collection(String.valueOf(groupCode)).document(BUDGET).set(budgetModel);

    }

    private void setProfile() {
        Prof prof = new Prof(true,String.valueOf(groupCode));
        db.collection(USERS).document(email).set(prof);
    }

    public void joinRoom()
    {

        db.collection(String.valueOf(groupCode)).document(METADATA).get().addOnCompleteListener(task -> {
            if(task.getResult().exists())
            {
                setProfile();
                User user = new User(name);
                db.collection(String.valueOf(groupCode)).document(email).set(user)
                        .addOnSuccessListener(unused -> success.setValue(true));
            }
            else
            {
                success.setValue(false);
            }
        });


    }

    public LiveData<Boolean> getSuccess()
    {
        return success;
    }



}
