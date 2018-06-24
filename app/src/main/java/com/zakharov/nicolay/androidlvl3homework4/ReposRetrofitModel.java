package com.zakharov.nicolay.androidlvl3homework4;

/**
 * Created by 1 on 24.06.2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReposRetrofitModel {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
