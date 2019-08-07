package com.example.timple.zdpigapp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP on 2019/8/7.
 */

public class PigAllInfoEntity implements Serializable {

    private PigStatusEntity current_info;
    private PigBaseInfo base_info;
    private List<FattenInfo> fatten_info;
    private List<BreedInfo> breed_info;
    private List<FattenInfo> litter_info;
    private List<FattenInfo> breeding_info;

    public PigStatusEntity getCurrent_info() {
        return current_info;
    }

    public void setCurrent_info(PigStatusEntity current_info) {
        this.current_info = current_info;
    }

    public PigBaseInfo getBase_info() {
        return base_info;
    }

    public void setBase_info(PigBaseInfo base_info) {
        this.base_info = base_info;
    }

    public List<FattenInfo> getFatten_info() {
        return fatten_info;
    }

    public void setFatten_info(List<FattenInfo> fatten_info) {
        this.fatten_info = fatten_info;
    }

    public List<BreedInfo> getBreed_info() {
        return breed_info;
    }

    public void setBreed_info(List<BreedInfo> breed_info) {
        this.breed_info = breed_info;
    }

    public List<FattenInfo> getLitter_info() {
        return litter_info;
    }

    public void setLitter_info(List<FattenInfo> litter_info) {
        this.litter_info = litter_info;
    }

    public List<FattenInfo> getBreeding_info() {
        return breeding_info;
    }

    public void setBreeding_info(List<FattenInfo> breeding_info) {
        this.breeding_info = breeding_info;
    }
}


