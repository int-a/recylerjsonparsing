package com.inta.anthony.recylerjsonparsing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthony on 6/24/2017.
 */

public class AndroidVersion {

    @SerializedName("ver")
    @Expose
    private String ver;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("api")
    @Expose
    private String api;

    public String getVer(){
        return ver;
    }

    public String getName(){
        return name;
    }

    public String getApi(){
        return api;
    }
}
