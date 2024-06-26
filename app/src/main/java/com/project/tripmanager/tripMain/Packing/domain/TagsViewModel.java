package com.project.tripmanager.tripMain.Packing.domain;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.ListenerRegistration;
import com.project.tripmanager.tripMain.Packing.dto.CategorySelected;
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;
import com.project.tripmanager.tripMain.Packing.dto.PackingItemReference;
import com.project.tripmanager.tripMain.Packing.repo.PackingListRepository;
import com.project.tripmanager.tripMain.Packing.repo.UserPackingRepo;

public class TagsViewModel extends ViewModel {
    private final MutableLiveData<CategorySelected>  selection = new MutableLiveData<>();

    private final PackingListRepository repo = new PackingListRepository();
    private final UserPackingRepo userPackingRepo = new UserPackingRepo();

    private MutableLiveData<String> tagLive = new MutableLiveData<>();


    public void setSelection(CategorySelected categorySelected)
    {
        selection.setValue(categorySelected);

    }


    public void setTag(String tag)
    {
            repo.setTag(tag);
            tagLive.setValue(tag);

    }



    public void setCatId(String id)
    {
        repo.setCatId(id);

    }

    public void setItemName(String iName)
    {
        repo.setItemName(iName);

    }


    public LiveData<String> getTag()
    {
        return tagLive;
    }

    public void attachListener(Context context, String email, String groupCode, String CatId, String tag)
    {
        userPackingRepo.setContext(context);
        userPackingRepo.listnentolist(email,groupCode,CatId,tag);
    }



    public void updateItem(PackingItem packingItem)
    {
        userPackingRepo.updateItem(packingItem);
    }

    public MutableLiveData<CategorySelected> getSelection()
    {
        return selection;
    }
    public LiveData<PackingItemReference> getItemPathRef()
    {
        return repo.fetchReference();

    }

    public LiveData<ListenerRegistration> getListener()
    {
        return userPackingRepo.getListener();
    }


}
