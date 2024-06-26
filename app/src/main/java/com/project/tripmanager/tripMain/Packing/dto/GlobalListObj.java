package com.project.tripmanager.tripMain.Packing.dto;

import java.util.ArrayList;

public class GlobalListObj {

    private ArrayList<GlobalListItem> list = new ArrayList<>();

    public GlobalListObj() {
    }

    public ArrayList<GlobalListItem> getList() {
        return list;
    }

    public void setList(ArrayList<GlobalListItem> list) {
        this.list = list;
    }
}
