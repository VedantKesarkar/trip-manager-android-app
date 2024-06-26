package com.project.tripmanager.newTrip.dto;

public class MetaData {

    private String admin;
    private String tripName;

    public MetaData(){}
    public MetaData(String admin, String tripName) {
        this.admin = admin;
        this.tripName = tripName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}
