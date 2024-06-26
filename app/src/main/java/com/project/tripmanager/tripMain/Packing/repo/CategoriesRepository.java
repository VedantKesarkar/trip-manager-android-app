package com.project.tripmanager.tripMain.Packing.repo;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.Core.USERS;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.auth.dto.Prof;
import com.project.tripmanager.tripMain.Packing.dto.CategorySelected;

import java.util.ArrayList;
import java.util.Objects;

public class CategoriesRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "CategoriesRepository";
    public static final String PUBLIC_PACKING_LIST = "Public packing list";
    public static final String NAME = "name";
    private String groupCode;
    private  String email="";
    private String name="",title="";
    private String catComboId;
    private final ArrayList<String> mList = new ArrayList<>();
    private final MutableLiveData<String> code = new MutableLiveData<>();


    public CategoriesRepository(){
        getEmail();
    }

    private void getEmail() {
        email  = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        getGroupCode();
    }


    private void getGroupCode() {
        db.collection(USERS).document(email).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                Prof prof = documentSnapshot.toObject(Prof.class);
                assert prof != null;
                groupCode = prof.getGroupCode();
                code.setValue(groupCode);
                getUserName();
            }
        });

    }

    private void getUserName()
    {
        db.collection(groupCode).document(email).get()
                .addOnSuccessListener(documentSnapshot -> name = documentSnapshot.getString(NAME));
    }

    public void setTitle(String title)
    {
        this.title  = title;
        createCategorySelection();
    }

    public void setList(ArrayList<String> list)
    {
        mList.clear();
        mList.addAll(list);

    }

    public void createCategorySelection()
    {
        if(!groupCode.equals("none") && !name.equals("") && !email.equals("") )
        {
            CategorySelected categorySelectedItem = new CategorySelected(mList,title,name);
            DocumentReference docRef = db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST).document();
            catComboId = docRef.getId();
            categorySelectedItem.setId(docRef.getId());
            docRef.set(categorySelectedItem)
                    .addOnSuccessListener(unused -> Log.d(TAG, "Category Item Created !!"))
                    .addOnFailureListener(e -> Log.d(TAG, e.toString()));
        }
    }



    public LiveData<String> fetchGroupCode()
    {
        return code;
    }

    public String getCatComboId() {
        return catComboId;
    }

}
