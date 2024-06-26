package com.project.tripmanager.tripMain.Packing.dto;

public class PackingItemReference {

    private String code;
    private String catId;
    private String tag;

    public PackingItemReference() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
