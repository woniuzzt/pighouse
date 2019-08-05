package com.example.timple.zdpigapp.entity;

import java.io.Serializable;

public class PigStatusEntity implements Serializable {
    private String hog_id;
    private String status;
    private String status_desc;
    private String  ear_tag;
    private String  hog_house_name;
    private String  filed;

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getEar_tag() {
        return ear_tag;
    }

    public void setEar_tag(String ear_tag) {
        this.ear_tag = ear_tag;
    }

    public String getHog_house_name() {
        return hog_house_name;
    }

    public void setHog_house_name(String hog_house_name) {
        this.hog_house_name = hog_house_name;
    }

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
