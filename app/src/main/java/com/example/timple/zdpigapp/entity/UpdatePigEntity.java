package com.example.timple.zdpigapp.entity;

public class UpdatePigEntity {
    private String result;
    private String status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdatePigEntity(String status, String result) {
        this.result = result;
        this.status = status;
    }
}
