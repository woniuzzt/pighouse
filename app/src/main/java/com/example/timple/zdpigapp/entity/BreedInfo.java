package com.example.timple.zdpigapp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP on 2019/8/7.
 */

public class BreedInfo implements Serializable{
    private List<FattenInfo>  detail;
    private String base_tag_name;

    public List<FattenInfo> getDetail() {
        return detail;
    }

    public void setDetail(List<FattenInfo> detail) {
        this.detail = detail;
    }

    public String getBase_tag_name() {
        return base_tag_name;
    }

    public void setBase_tag_name(String base_tag_name) {
        this.base_tag_name = base_tag_name;
    }
}
