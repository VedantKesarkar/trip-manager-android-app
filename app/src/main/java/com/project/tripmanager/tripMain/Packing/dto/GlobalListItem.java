package com.project.tripmanager.tripMain.Packing.dto;

public class GlobalListItem {
    private String itemName;
    private Long count;
    public GlobalListItem() {
    }

    public GlobalListItem(String itemName, Long count) {
        this.itemName = itemName;
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
