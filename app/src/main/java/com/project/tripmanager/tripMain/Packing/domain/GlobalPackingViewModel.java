package com.project.tripmanager.tripMain.Packing.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;
import com.project.tripmanager.tripMain.Packing.repo.GlobalPackingRepository;

import java.util.ArrayList;

public class GlobalPackingViewModel extends ViewModel {

    private final GlobalPackingRepository globalPackingRepo = new GlobalPackingRepository();

    public void setItemName(String name)
    {
        globalPackingRepo.setItemName(name);
    }

    public void setTag(String tag)
    {
        globalPackingRepo.setTag(tag);
    }

    public void setCatSelected(ArrayList<String> catSelected)
    {
        globalPackingRepo.setCategorySelected(catSelected);
    }

    public void findGlobalList()
    {

    }

    public void checkGlobalListAvail(ArrayList<String> catList)
    {
        globalPackingRepo.checkGlobalListAvail(catList);
    }

    public LiveData<ArrayList<GlobalListItem>> fetchGlobalList()
    {
        return globalPackingRepo.getRetrievedListLive();
    }

    public LiveData<Boolean> isGlobalListAvail()
    {
        return globalPackingRepo.IsGlobalListAvail();
    }

}
