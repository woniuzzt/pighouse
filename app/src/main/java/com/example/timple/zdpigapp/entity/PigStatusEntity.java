package com.example.timple.zdpigapp.entity;

import java.io.Serializable;

public class PigStatusEntity implements Serializable {
    private String hog_id;
    private String status;
    private String status_desc;

    public String getHog_id() {
        return hog_id;
    }

    public void setHog_id(String hog_id) {
        this.hog_id = hog_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }
}
