package com.project.tripmanager.tripMain.Packing.dto;

public class CategorySubType {
    private String descp;
    private int img;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public CategorySubType(String descp, int img) {
        this.descp = descp;
        this.img = img;
        selected = false;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
