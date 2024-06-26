package com.project.tripmanager.tripMain.Packing.repo;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.tripMain.Packing.repo.CategoriesRepository.PUBLIC_PACKING_LIST;
import static com.project.tripmanager.tripMain.Packing.repo.GlobalPackingRepository.GLOBAL_PACKING_LIST;
import static com.project.tripmanager.tripMain.Packing.repo.GlobalPackingRepository.ITEMS;
import static com.project.tripmanager.tripMain.Packing.repo.GlobalPackingRepository.ITEM_LIST;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.WriteBatch;
import com.project.tripmanager.tripMain.Packing.dto.GlobalCatCombo;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListObj;
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalPackingInteractionRepo {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference globalDocRef;
    private WriteBatch batch;
    private static final String TAG = "GlobalPackingInteraction";
    private ArrayList<String> catSelected = new ArrayList<>();

    private final ArrayList<GlobalListItem> list = new ArrayList<>();
    private final MutableLiveData<ListenerRegistration> listner = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<GlobalListItem>> retrievedList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> additionOfItems = new MutableLiveData<>();
    private String mTag,id;
    private Context context;
    public GlobalPackingInteractionRepo() {
    }


    public void setCatSelected(ArrayList<String> catSelected) {
        this.catSelected = catSelected;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public void setCont(Context context) {
        this.context = context;
    }

    public void findList()
    {

        list.clear();//Must reset the list to properly display items in the UI
        db.collection(GLOBAL_PACKING_LIST).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                if(catSelected.containsAll(
                        Objects.requireNonNull(queryDocumentSnapshots.getDocuments()
                                .get(i).toObject(GlobalCatCombo.class)).getCatCombo()))
                {
                     id = queryDocumentSnapshots.getDocuments().get(i).getId();
                     fetchList();
                     break;
                }
            }
        });
    }

    private void fetchList() {
        globalDocRef = db.collection(GLOBAL_PACKING_LIST).document(id).collection(mTag).document(ITEMS);

        ListenerRegistration l  = globalDocRef.addSnapshotListener( (docSnapshot, error) -> {
                    assert docSnapshot != null;
                    if(docSnapshot.exists())
                    {
                        list.clear();
                        Log.d(TAG, "Change in list! "+mTag);
                      //  Log.d(TAG, Objects.requireNonNull(docSnapshot.toObject(GlobalListObj.class)).getList().get(0).getCount().toString());
                        list.addAll(Objects.requireNonNull(docSnapshot.toObject(GlobalListObj.class)).getList());

                    }
                    retrievedList.setValue(list);
                    Log.d(TAG, String.valueOf(list.size()));
                });
        listner.setValue(l);
    }




    public LiveData<ArrayList<GlobalListItem>> getRetrievedListLive() {
        return retrievedList;
    }

    public LiveData<ListenerRegistration> getListener()
    {
        return listner;
    }





    //This code is to add selected items to the list

    public void addItemsToPackingList(String roomCode, String catComboId,ArrayList<GlobalListItem> selectedList)
    {
         batch = db.batch();
        for (int i = 0; i < selectedList.size(); i++) {
            PackingItem packingItem = new PackingItem();
            DocumentReference docRef = db.collection(roomCode).document(METADATA).collection(PUBLIC_PACKING_LIST)
                                      .document(catComboId).collection(mTag).document();
            packingItem.setItemName(selectedList.get(i).getItemName());
            packingItem.setId(docRef.getId());
            packingItem.setChecked(false);
            batch.set(docRef,packingItem);
            updateGlobalList(selectedList.get(i));
        }
        if(globalDocRef!=null && list.size()!=0)
            batch.update(globalDocRef,ITEM_LIST,list);

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "All Items added to Packing List!");
                additionOfItems.setValue(true);
                retrievedList.setValue(list);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
                additionOfItems.setValue(false);
            }
        });



    }

    private void updateGlobalList(GlobalListItem globalListItem) {
        if(globalDocRef!=null && list.size()!=0)
        {
            int idx = list.indexOf(globalListItem);
            GlobalListItem item = list.get(idx);
            item.setCount(item.getCount()+1);
            list.set(idx,item);
        }

    }


    public LiveData<Boolean> getAdditionOfItems() {
        return additionOfItems;
    }


}
