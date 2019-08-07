package com.example.timple.zdpigapp.entity;

import java.io.Serializable;

/**
 * Created by HP on 2019/8/7.
 */

public class PigBaseInfo implements Serializable{
    private String id;
    private String ear_tag;
    private String birth;
    private String category;
    private String owner_hog_farm;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEar_tag() {
        return ear_tag;
    }

    public void setEar_tag(String ear_tag) {
        this.ear_tag = ear_tag;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwner_hog_farm() {
        return owner_hog_farm;
    }

    public void setOwner_hog_farm(String owner_hog_farm) {
        this.owner_hog_farm = owner_hog_farm;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
