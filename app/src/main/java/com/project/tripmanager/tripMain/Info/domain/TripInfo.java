package com.project.tripmanager.tripMain.Info.domain;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.Core.USERS;
import static com.project.tripmanager.newTrip.domain.CreateOrJoinGroup.BUDGET;
import static com.project.tripmanager.tripMain.Expenses.repo.AddExpensesRepo.EXPENDITURE;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.tripmanager.Core;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.newTrip.dto.MetaData;
import com.project.tripmanager.newTrip.dto.User;
import com.project.tripmanager.tripMain.Info.dto.TripDetails;

import java.util.ArrayList;

public class TripInfo {

    private static final String TAG = "TripInfo";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<TripDetails> tripDetails = new MutableLiveData<>();
    private final ArrayList<String> members = new ArrayList<>();
    private String tripName, groupCode,email="";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserLeftInterface userLeftInterface;



    public TripInfo() {
    }
    public void loadInfo(String email) {
        db.collection(USERS).document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Prof prof = documentSnapshot.toObject(Prof.class);
                    assert prof != null;
                    groupCode = prof.getGroupCode();
                    loadDetails();
                }
            }
        });



    }

    private void loadDetails() {
        if(!groupCode.equals("none"))
        {
            db.collection(groupCode).get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<String> temp = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (!documentSnapshot.getId().equals(METADATA) && !documentSnapshot.getId().equals(EXPENDITURE) && !documentSnapshot.getId().equals(BUDGET)) {
                        User user = documentSnapshot.toObject(User.class);
                        temp.add(user.getName());
                    } else if(documentSnapshot.getId().equals(METADATA)){
                        MetaData metaData = documentSnapshot.toObject(MetaData.class);
                        tripName = metaData.getTripName();
                    }

                }
                members.clear();
                members.addAll(temp);
                tripDetails.setValue(new TripDetails(members,tripName,groupCode));

            });
        }
    }


    public void deleteMember(UserLeftInterface userLeftInterface)
    {
        this.userLeftInterface = userLeftInterface;
        if(!groupCode.equals("none") && mAuth.getCurrentUser()!=null)
        {
            email = mAuth.getCurrentUser().getEmail();

            db.collection(groupCode).document(email).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Deleted !");
                            updateUserProf();
                        }
                    });




        }
    }

    private void updateUserProf() {
        db.collection(USERS).document(email)
                .update("groupCode","none","status",false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Profile updated!");
                        userLeftInterface.onLeave();
                    }
                });
    }

    public LiveData<TripDetails> getDetails()
    {
        return tripDetails;
    }


}

