package com.project.tripmanager.tripMain.Packing.repo;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.tripMain.Packing.repo.CategoriesRepository.PUBLIC_PACKING_LIST;
import static com.project.tripmanager.tripMain.Packing.repo.PackingListRepository.CHECKED;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.tripmanager.tripMain.Packing.dto.CategorySelected;
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;

import java.util.HashMap;
import java.util.Map;

public class UserPackingRepo {

    private Context context;
    private String email,groupCode,catId,tag,title;
    public static final String PRIVATE_PACKING_LIST = "Private Packing List";
    private static final String TAG = "UserPackingRepo";
    private CollectionReference privateItemList;
    private DocumentReference catListRef;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<ListenerRegistration> listener = new MutableLiveData<>();

    public UserPackingRepo() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void listnentolist(String email, String groupCode, String catId, String tag)
    {
        this.email = email;
        this.groupCode = groupCode;
        this.catId = catId;
        this.tag = tag;
         catListRef =  db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST).document(catId);

        catListRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CategorySelected categorySelected = documentSnapshot.toObject(CategorySelected.class);
                assert categorySelected != null;
                title = categorySelected.getTitle();
                setupListener();
            }
        });

    }

    private void setupListener() {

        Map<String,Object> catTitle = new HashMap<>();
        catTitle.put("Title",title);


        db.collection(groupCode).document(email).collection(PRIVATE_PACKING_LIST).document(catId).set(catTitle).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                copyDocuments();
            }
        });

    }

    private void copyDocuments() {
        CollectionReference itemRef = catListRef.collection(tag);
        privateItemList = db.collection(groupCode).document(email).collection(PRIVATE_PACKING_LIST).document(catId).collection(tag);
         ListenerRegistration l = itemRef.addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for (DocumentChange document : value.getDocumentChanges()) {
                    PackingItem item = document.getDocument().toObject(PackingItem.class);
                    if(document.getType().equals(DocumentChange.Type.REMOVED))
                    {

                        removeItem(item);
                    }
                    else if(document.getType().equals(DocumentChange.Type.ADDED))
                    {
                        Log.d(TAG, "Action add");
                        addListItem(item);
                    }
                    else if (document.getType().equals(DocumentChange.Type.MODIFIED))
                    {
                        Log.d(TAG, "Action update");
                    }
                }
            }
        });
         listener.setValue(l);
    }

    private void removeItem(PackingItem item) {
        //TODO fix for deleting all
        privateItemList.document(item.getId()).delete();
    }

    private void addListItem(PackingItem item) {

//        privateItemList.document(item.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists())
//                {
//                    privateItemList.document(item.getId()).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Log.d(TAG, "Change added!");
//                        }
//                    }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
//                }
//            }
//        });

        privateItemList.document(item.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists())
                {
                    privateItemList.document(item.getId()).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Change added!");
                        }
                    }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
                }
            }
        });


    }


    public void updateItem(PackingItem packingItem) {

        if(!groupCode.equals("none")  && !email.equals(""))
        {
            if(privateItemList!=null)
            {
                privateItemList.document(packingItem.getId()).update(CHECKED,packingItem.isChecked())
                        .addOnSuccessListener(unused -> Log.d(TAG, "Update item !!"))
                        .addOnFailureListener(e -> Log.d(TAG, e.toString()));
            }


        }
    }

    public LiveData<ListenerRegistration> getListener()
    {
        return  listener;
    }
}
