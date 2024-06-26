package com.project.tripmanager.tripMain.Info.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.tripmanager.tripMain.Info.dto.TripDetails;

public class DetailsViewModel extends ViewModel {

    private final TripInfo tripInfo = new TripInfo();


    public void loadInfo(String email)
    {
        tripInfo.loadInfo(email);
    }


    public LiveData<TripDetails> getInfo()
    {
        return tripInfo.getDetails();
    }

    public void deleteMember(UserLeftInterface userLeftInterface)
    {
        tripInfo.deleteMember(userLeftInterface);
    }


}
