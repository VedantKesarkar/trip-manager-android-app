package com.project.tripmanager;

public class Core {
    public static final String METADATA = "metadata";
    public static final String USERS = "users";
    private String groupCode="none";

    public Core() {
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
