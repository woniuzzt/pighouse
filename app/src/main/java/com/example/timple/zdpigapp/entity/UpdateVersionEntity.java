package com.example.timple.zdpigapp.entity;

public class UpdateVersionEntity {
    private String id;
    private String version;
    private String download_url;
    private String update_instruction;
    private String created_at;
    private int is_force_update;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getUpdate_instruction() {
        return update_instruction;
    }

    public void setUpdate_instruction(String update_instruction) {
        this.update_instruction = update_instruction;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }
}
