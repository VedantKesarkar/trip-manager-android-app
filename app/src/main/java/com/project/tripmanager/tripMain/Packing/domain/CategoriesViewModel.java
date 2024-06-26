package com.project.tripmanager.tripMain.Packing.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.Packing.repo.CategoriesRepository;

import java.util.ArrayList;

public class CategoriesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> categoriesSelectedLive = new MutableLiveData<>();

    private final CategoriesRepository repo = new CategoriesRepository();


    public void setCategoriesSelectedLive(ArrayList<String> categories)
    {
        repo.setList(categories);
        categoriesSelectedLive.setValue(categories);
    }

    public void setTitle(String title)
    {
        repo.setTitle(title);
    }

    public String getCatComboId()
    {
        return repo.getCatComboId();
    }



    public LiveData<ArrayList<String>> getCategoriesSelectedLive()
    {
        return categoriesSelectedLive;
    }

    public LiveData<String>  getGroupCode()
    {
        return repo.fetchGroupCode();
    }

}
