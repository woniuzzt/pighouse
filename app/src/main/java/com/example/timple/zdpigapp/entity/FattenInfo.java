package com.example.timple.zdpigapp.entity;

import java.io.Serializable;

/**
 * Created by HP on 2019/8/7.
 */

public class FattenInfo implements Serializable{
    private String tag_name;
    private String result;

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
