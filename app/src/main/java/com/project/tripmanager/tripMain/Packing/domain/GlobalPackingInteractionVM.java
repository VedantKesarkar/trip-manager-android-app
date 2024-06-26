package com.project.tripmanager.tripMain.Packing.domain;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.ListenerRegistration;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;
import com.project.tripmanager.tripMain.Packing.repo.GlobalPackingInteractionRepo;

import java.util.ArrayList;

public class GlobalPackingInteractionVM extends ViewModel {
    private final GlobalPackingInteractionRepo packingInteractionRepo = new GlobalPackingInteractionRepo();

    private String id;
    public void setCatList(ArrayList<String> catList)
    {
        packingInteractionRepo.setCatSelected(catList);
    }

    public void setTag(String tag)
    {
        packingInteractionRepo.setmTag(tag);
    }



    public void setContext(Context context)
    {
        packingInteractionRepo.setCont(context);
    }

    public void findGlobalList()
    {
        packingInteractionRepo.findList();
    }

    public void addSelectedItemsToPackingList(String roomCode, String catComboId,ArrayList<GlobalListItem> selectedList)
    {
        packingInteractionRepo.addItemsToPackingList(roomCode, catComboId,  selectedList);
    }

    public LiveData<ListenerRegistration> getListener()
    {
        return packingInteractionRepo.getListener();
    }

    public LiveData<ArrayList<GlobalListItem>> getGlobalList()
    {
        return packingInteractionRepo.getRetrievedListLive();
    }

    public LiveData<Boolean> getAdditionOfItemsResult()
    {
        return packingInteractionRepo.getAdditionOfItems();
    }

    public void setCatComboId(String id)
    {
        this.id = id;
    }

    public String getCatComboId()
    {
        return id;
    }


}
