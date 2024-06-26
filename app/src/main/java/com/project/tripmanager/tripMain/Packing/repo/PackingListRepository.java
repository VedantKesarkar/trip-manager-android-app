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
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;
import com.project.tripmanager.tripMain.Packing.dto.PackingItemReference;

import java.util.Objects;

public class PackingListRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  DocumentReference catListRef;
    private  DocumentReference docRef;

    private static final String TAG = "PackingListRepository";
    public static final String PUBLIC_PACKING_LIST = "Public packing list";
    public static final String CHECKED = "checked";

    private String groupCode="none";
    private  String CatId="",email="",tag="";
    private String itemName="";

    private final MutableLiveData<PackingItemReference>  ref = new MutableLiveData<>();
    private final PackingItemReference packingItemReference = new PackingItemReference();


    public PackingListRepository(){
        getEmail();
    }


    //Needed to create path reference
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
                packingItemReference.setCode(groupCode);
                ref.setValue(packingItemReference);
               // getUserName();
            }
        });

    }

    public void setCatId(String catId) {
        CatId = catId;
        packingItemReference.setCatId(catId);
        ref.setValue(packingItemReference);
    }

    public void setTag(String tag) {
        this.tag = tag;
        packingItemReference.setTag(tag);
        ref.setValue(packingItemReference);
    }


    //Need to create Item

    //Creation
    public void setItemName(String itemName)
    {
        this.itemName  = itemName;
        createItem();
    }

    private void createItem()
    {
        if(!groupCode.equals("none") && !itemName.equals("") && !email.equals("") && !CatId.equals(""))
        {
            PackingItem packingItem = new PackingItem(itemName,false);
             catListRef =  db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST).document(CatId);
             docRef = catListRef.collection(tag).document();
            packingItem.setId(docRef.getId());
            docRef.set(packingItem)
                    .addOnSuccessListener(unused -> Log.d(TAG, "Packing Item Created!!"))
                    .addOnFailureListener(e -> Log.d(TAG, e.toString()));


        }
    }

    //Modification
    public void updatePackingItem(PackingItem packingItem) {
        updateItem(packingItem);
    }

    private void updateItem(PackingItem packingItem) {

        if(!groupCode.equals("none")  && !email.equals(""))
        {
            Log.d(TAG, "Should work!");

             catListRef =  db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST).document(CatId);
             docRef = catListRef.collection(tag).document(packingItem.getId());
            docRef.update(CHECKED,packingItem.isChecked()).addOnSuccessListener(unused -> Log.d(TAG, "Update item !!"))
                    .addOnFailureListener(e -> Log.d(TAG, e.toString()));
        }
    }





    //    public void createCategorySelection()
//    {
//        if(!groupCode.equals("none") && !itemName.equals("") && !email.equals("") )
//        {
//            CategorySelected categorySelectedItem = new CategorySelected(mList,itemName,name);
//            DocumentReference docRef = db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST).document();
//            categorySelectedItem.setId(docRef.getId());
//            docRef.set(categorySelectedItem)
//                    .addOnSuccessListener(unused -> Log.d(TAG, "Category Item Created !!"))
//                    .addOnFailureListener(e -> Log.d(TAG, e.toString()));
//        }
//    }



    public LiveData<PackingItemReference> fetchReference()
    {

        return ref;
    }

}
