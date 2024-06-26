package com.project.tripmanager.tripMain.Packing.dto;

import com.google.firebase.firestore.Exclude;

public class PackingItem {
    @Exclude
    private String id;
    private String ItemName;
    private boolean checked=false;

    public PackingItem() {}

    public PackingItem(String ItemName, boolean checked) {
        this.ItemName = ItemName;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
