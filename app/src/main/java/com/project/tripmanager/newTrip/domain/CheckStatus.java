package com.project.tripmanager.newTrip.domain;

import static com.project.tripmanager.Core.USERS;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.Core;
import com.project.tripmanager.auth.dto.Prof;

public class CheckStatus {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Core core = new Core();
    private final MutableLiveData<Boolean> status = new MutableLiveData<>();
    public CheckStatus() {
    }

    public void checkUserStatus(String email)
    {
        db.collection(USERS).document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Prof  prof = documentSnapshot.toObject(Prof.class);
                    assert prof != null;
                    if(prof.isStatus())core.setGroupCode(prof.getGroupCode());
                    status.setValue(prof.isStatus());
                }
                else
                {
                    status.setValue(false);
                }



            }
        });
    }



    public LiveData<Boolean> getStatus() {
        return status;
    }

}
