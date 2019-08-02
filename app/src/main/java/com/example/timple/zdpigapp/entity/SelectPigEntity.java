package com.example.timple.zdpigapp.entity;

public class SelectPigEntity {
    private boolean isSelect;
    private String pigName;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPigName() {
        return pigName;
    }

    public void setPigName(String pigName) {
        this.pigName = pigName;
    }
}
