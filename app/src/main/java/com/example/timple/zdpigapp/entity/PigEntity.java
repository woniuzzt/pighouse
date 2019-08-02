package com.example.timple.zdpigapp.entity;

import java.util.List;

public class PigEntity {
    private String hog_id;
    private String ear_tag;
    private List<DetailEntity> detail;

    public String getHog_id() {
        return hog_id;
    }

    public void setHog_id(String hog_id) {
        this.hog_id = hog_id;
    }

    public String getEar_tag() {
        return ear_tag;
    }

    public void setEar_tag(String ear_tag) {
        this.ear_tag = ear_tag;
    }

    public List<DetailEntity> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailEntity> detail) {
        this.detail = detail;
    }
}
