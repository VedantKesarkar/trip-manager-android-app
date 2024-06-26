package com.project.tripmanager.newTrip.domain;

import androidx.lifecycle.LiveData;

public class mViewModel extends androidx.lifecycle.ViewModel {
    private final CreateOrJoinGroup createOrJoinGroup = new CreateOrJoinGroup();
    private final CheckStatus checkStatus = new CheckStatus();



    public void creator(String email, String name, String tripName,String amt)
    {
        createOrJoinGroup.createAction(email,name,tripName,amt);
        createOrJoinGroup.createRoom();

    }

    public void joiner(String email,String name,int groupCode)
    {
        createOrJoinGroup.joinAction(email,name,groupCode);
        createOrJoinGroup.joinRoom();
    }

    public void setUser(String email)
    {
        checkStatus.checkUserStatus(email);
    }


    public  LiveData<Boolean> getUserStatus()
    {
        return  checkStatus.getStatus();
    }


    public LiveData<Boolean> getSuccess()
    {
        return createOrJoinGroup.getSuccess();
    }
}
