package com.project.tripmanager.auth.dto;

public class Prof {
    private boolean Status;
    private String groupCode;


    public Prof()
    {

    }

    public Prof(boolean status, String groupCode) {
        Status = status;
        this.groupCode = groupCode;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
