package com.project.tripmanager.tripMain.Packing.repo;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.tripmanager.tripMain.Packing.dto.GlobalCatCombo;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListObj;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalPackingRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GlobalCatCombo globalCatCombo;
    private DocumentReference itemRef;
    private static final String TAG = "GlobalPackingRepository";
    private GlobalListItem listItem;

    private final ArrayList<GlobalListItem> itemList = new ArrayList<>();
   private   ArrayList<GlobalListItem> retrivedList = new ArrayList<>();
    public static final String GLOBAL_PACKING_LIST = "Global Packing List";
    public static final String ITEMS = "Items";
    public static final String ITEM_LIST = "list";
    private String entryId;
    private boolean avail = false;
    private String tag,itemName;
    private  ArrayList<String> categorySelected = new ArrayList<>();

    private MutableLiveData<ArrayList<GlobalListItem>> retrivedListLive = new MutableLiveData<>();
    private MutableLiveData<Boolean> isGlobalListAvail = new MutableLiveData<>();

    public GlobalPackingRepository() {
    }

    public void setCategorySelected(ArrayList<String> categorySelected) {
        this.categorySelected = categorySelected;

    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
        if(categorySelected.size()!=0)
        {
            addToGlobalList();
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void addToGlobalList()
    {
        db.collection(GLOBAL_PACKING_LIST).get().addOnSuccessListener(this::filterList);

    }

    private void filterList(QuerySnapshot queryDocumentSnapshots) {
        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
            if(categorySelected.containsAll(
                            Objects.requireNonNull(queryDocumentSnapshots.getDocuments()
                                    .get(i).toObject(GlobalCatCombo.class)).getCatCombo()))
            {
                Log.d(TAG, "An Entry exists");
                entryId = queryDocumentSnapshots.getDocuments().get(i).getId();
                addItems();
                return;

            }
        }
        globalCatCombo = new GlobalCatCombo(categorySelected);
        DocumentReference docRef = db.collection(GLOBAL_PACKING_LIST).document();
        entryId = docRef.getId();
        globalCatCombo.setId(entryId);
        docRef.set(globalCatCombo).addOnSuccessListener(unused -> {
            Log.d(TAG, "Created first entry");
            addItems();
        });
    }


    private void addItems() {
         listItem  = new GlobalListItem();
        listItem.setItemName(itemName);
         itemRef = db.collection(GLOBAL_PACKING_LIST).document(entryId).collection(tag).document(ITEMS);
        itemRef.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                itemList.clear();
                itemList.addAll(Objects.requireNonNull(documentSnapshot.toObject(GlobalListObj.class)).getList());

                checkList();
                itemRef.update(ITEM_LIST, itemList)
                       .addOnFailureListener(e -> Log.d(TAG, e.toString()));
            }
            else {
                createListWithItems();
            }
        });
    }

    private void checkList() {
        Log.d(TAG, "list-> "+itemList);
        for (int i = 0; i < itemList.size(); i++)
        {
            if(itemList.get(i).getItemName().toLowerCase().trim().equals(itemName))
            {
                Log.d(TAG, "duplicate ");
                long count = itemList.get(i).getCount()+1;
                listItem.setCount(count);
                itemList.set(i,listItem);
                return;
            }
        }

            listItem.setCount(1L);
            itemList.add(listItem);

    }

    private void createListWithItems() {
        listItem.setCount(1L);
        itemList.clear();
        itemList.add(listItem);
        GlobalListObj obj = new GlobalListObj();
        obj.setList(itemList);
        itemRef.set(obj).addOnSuccessListener(unused ->
                Log.d(TAG, "New Tag added !"));
    }



    //Retrieving Items
    public void checkGlobalListAvail(ArrayList<String> catSelected)
    {
        ArrayList<String> mCatList = new ArrayList<>(catSelected);
        avail = false;
        db.collection(GLOBAL_PACKING_LIST).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                if (mCatList.containsAll(
                        Objects.requireNonNull(queryDocumentSnapshots.getDocuments()
                                .get(i).toObject(GlobalCatCombo.class)).getCatCombo()))
                {
                    avail = true;
                }
            }
            isGlobalListAvail.setValue(avail);

        });
    }

    public LiveData<ArrayList<GlobalListItem>> getRetrievedListLive() {
        return retrivedListLive;
    }

    public LiveData<Boolean> IsGlobalListAvail() {
        return isGlobalListAvail;
    }
}

