package com.project.tripmanager.tripMain.Info.dto;

import java.util.ArrayList;

public class TripDetails {
    private ArrayList<String> members = new ArrayList<>();
    private String tripName;
    private String code;

    public TripDetails(){}
    public TripDetails(ArrayList<String> members, String tripName, String code) {
        this.members = members;
        this.tripName = tripName;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}
